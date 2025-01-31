/*
 * Copyright 2007-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.platform.webmvc.support;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.core.beans.intercept.InterceptException;
import net.ymate.platform.webmvc.*;
import net.ymate.platform.webmvc.annotation.ResponseBody;
import net.ymate.platform.webmvc.annotation.ResponseHeader;
import net.ymate.platform.webmvc.annotation.ResponseView;
import net.ymate.platform.webmvc.annotation.SignatureValidate;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.exception.ParameterSignatureException;
import net.ymate.platform.webmvc.util.WebResult;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.impl.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * WebMVC请求执行器
 *
 * @author 刘镇 (suninformation@163.com) on 2012-12-14 下午4:30:27
 */
public final class RequestExecutor {

    private final IWebMvc owner;

    private final RequestMeta requestMeta;

    private IRequestProcessor requestProcessor;

    public static RequestExecutor bind(IWebMvc owner, RequestMeta requestMeta) {
        return new RequestExecutor(owner, requestMeta);
    }

    private RequestExecutor(IWebMvc owner, RequestMeta requestMeta) {
        this.owner = owner;
        this.requestMeta = requestMeta;
        if (requestMeta.getProcessor() != null) {
            requestProcessor = ClassUtils.impl(requestMeta.getProcessor(), IRequestProcessor.class);
        }
        if (requestProcessor == null) {
            requestProcessor = this.owner.getConfig().getRequestProcessor();
        }
    }

    private void doSignatureValidate() {
        SignatureValidate signatureValidate = requestMeta.getSignatureValidate();
        if (signatureValidate != null && !signatureValidate.disabled()) {
            ISignatureValidator signatureValidator = ClassUtils.impl(signatureValidate.validatorClass(), ISignatureValidator.class);
            if (!signatureValidator.validate(owner, requestMeta, signatureValidate)) {
                throw new ParameterSignatureException("Parameter signature mismatch.");
            }
        }
    }

    public IView execute() throws Exception {
        // 尝试处理参数签名验证
        doSignatureValidate();
        // 取得当前控制器方法参数的名称集合
        List<String> methodParamNames = requestMeta.getMethodParamNames();
        // 根据参数名称, 从请求中提取对应的参数值
        Map<String, Object> paramValues = requestProcessor.processRequestParams(owner, requestMeta);
        // 将当前RequestMeta对象和参数映射放入WebContext中, 便于其它环节中获取并使用
        WebContext context = WebContext.getContext();
        context.addAttribute(RequestMeta.class.getName(), requestMeta);
        context.addAttribute(RequestParametersProxy.class.getName(), paramValues);
        // 提取控制器类实例
        Object targetObj = owner.getOwner().getBeanFactory().getBean(requestMeta.getTargetClass());
        if (!requestMeta.isSingleton() && !paramValues.isEmpty()) {
            // 为非单例控制器类成员赋值
            ClassUtils.wrapper(targetObj).fromMap(paramValues);
        }
        Object resultObj;
        try {
            if (!methodParamNames.isEmpty()) {
                // 组装方法所需参数
                Object[] methodParamValues = methodParamNames.stream().map(paramValues::get).toArray();
                resultObj = requestMeta.getMethod().invoke(targetObj, methodParamValues);
            } else {
                resultObj = requestMeta.getMethod().invoke(targetObj);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Throwable unwrapThrow = RuntimeUtils.unwrapThrow(e);
            if (unwrapThrow instanceof InterceptException) {
                resultObj = ((InterceptException) unwrapThrow).getReturnValue();
            } else {
                throw e;
            }
        }
        //
        IView resultView = null;
        ResponseBody responseBody = requestMeta.getResponseBody();
        if (responseBody != null) {
            if (resultObj instanceof IView || resultObj instanceof String) {
                resultView = doProcessResultToView(owner, requestMeta, resultObj);
            } else if (resultObj instanceof IWebResult) {
                resultView = WebResult.formatView((IWebResult<?>) resultObj, Type.Const.FORMAT_JSON);
            } else if (resultObj instanceof IWebResultBuilder) {
                IWebResult<?> result = ((IWebResultBuilder) resultObj).build();
                if (responseBody.keepNull()) {
                    result.keepNullValue();
                }
                if (responseBody.snakeCase()) {
                    result.snakeCase();
                }
                if (responseBody.contentType()) {
                    result.withContentType();
                }
                resultView = WebResult.formatView(result, Type.Const.FORMAT_JSON);
            } else {
                IResponseBodyProcessor responseBodyProcessor;
                if (IResponseBodyProcessor.class.equals(responseBody.value())) {
                    responseBodyProcessor = IResponseBodyProcessor.DEFAULT;
                } else {
                    responseBodyProcessor = ClassUtils.impl(responseBody.value(), IResponseBodyProcessor.class);
                }
                if (responseBodyProcessor != null) {
                    resultView = responseBodyProcessor.processBody(owner, resultObj, responseBody.contentType(), responseBody.keepNull(), responseBody.snakeCase());
                }
            }
        } else {
            resultView = doProcessResultToView(owner, requestMeta, resultObj);
        }
        if (resultView != null) {
            for (ResponseHeader header : requestMeta.getResponseHeaders()) {
                switch (header.type()) {
                    case DATE:
                        resultView.addDateHeader(header.name(), BlurObject.bind(header.value()).toLongValue());
                        break;
                    case INT:
                        resultView.addIntHeader(header.name(), BlurObject.bind(header.value()).toIntValue());
                        break;
                    default:
                        resultView.addHeader(header.name(), header.value());
                }
            }
        }
        return resultView;
    }

    public static IView doSwitchView(IWebMvc owner, Type.View viewType, String[] viewParts) throws Exception {
        IView view;
        switch (viewType) {
            case BINARY:
                BinaryView binaryView = BinaryView.bind(new File(viewParts[0]));
                if (binaryView != null) {
                    view = binaryView.useAttachment(viewParts.length > 1 ? viewParts[1] : null);
                } else {
                    view = HttpStatusView.NOT_FOUND;
                }
                break;
            case FORWARD:
                view = ForwardView.bind(viewParts[0]);
                break;
            case FREEMARKER:
                view = FreemarkerView.bind(owner, viewParts[0]);
                break;
            case VELOCITY:
                view = VelocityView.bind(owner, viewParts[0]);
                break;
            case HTML:
                view = HtmlView.bind(owner, viewParts[0]);
                break;
            case HTTP_STATES:
                view = HttpStatusView.bind(Integer.parseInt(viewParts[0]), viewParts.length > 1 ? viewParts[1] : null);
                break;
            case JSON:
                view = JsonView.bind(viewParts[0]);
                break;
            case JSP:
                view = JspView.bind(owner, viewParts[0]);
                break;
            case REDIRECT:
                view = RedirectView.bind(viewParts[0]);
                break;
            case TEXT:
                view = TextView.bind(viewParts[0]);
                break;
            default:
                view = NullView.bind();
        }
        return view;
    }

    public static IView doProcessResultToView(IWebMvc owner, RequestMeta requestMeta, Object result) throws Exception {
        IView view;
        if (result == null) {
            if (requestMeta != null && requestMeta.getResponseView() != null) {
                ResponseView responseView = requestMeta.getResponseView();
                String[] viewParts = StringUtils.split(responseView.value(), ":");
                view = doSwitchView(owner, responseView.type(), viewParts);
            } else {
                view = JspView.bind(owner);
            }
        } else if (result instanceof IView) {
            view = (IView) result;
        } else if (result instanceof String) {
            String[] parts = StringUtils.split((String) result, ":");
            if (ArrayUtils.isNotEmpty(parts) && parts.length > 1) {
                view = doSwitchView(owner, Type.View.valueOf(parts[0].toUpperCase()), parts);
            } else {
                view = HtmlView.bind((String) result);
            }
        } else {
            view = IResponseBodyProcessor.DEFAULT.processBody(owner, result, true, true, false);
        }
        return view;
    }
}
