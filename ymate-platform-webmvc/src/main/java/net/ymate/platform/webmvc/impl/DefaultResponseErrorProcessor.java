/*
 * Copyright 2007-2021 the original author or authors.
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
package net.ymate.platform.webmvc.impl;

import net.ymate.platform.webmvc.AbstractResponseErrorProcessor;
import net.ymate.platform.webmvc.IWebMvc;
import net.ymate.platform.webmvc.util.WebUtils;
import net.ymate.platform.webmvc.view.IView;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2021/12/29 8:37 下午
 * @since 2.1.0
 */
public class DefaultResponseErrorProcessor extends AbstractResponseErrorProcessor {

    public DefaultResponseErrorProcessor() {
        setErrorDefaultViewFormat(StringUtils.EMPTY);
    }

    @Override
    public String getErrorDefaultViewFormat(IWebMvc owner) {
        return StringUtils.EMPTY;
    }

    @Override
    public IView showErrorMsg(IWebMvc owner, String code, String msg, Map<String, Object> dataMap) {
        return WebUtils.buildErrorView(owner, null, code, msg, null, 0, dataMap);
    }
}
