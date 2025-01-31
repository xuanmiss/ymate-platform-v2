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
package net.ymate.platform.webmvc.annotation;

import java.lang.annotation.*;

/**
 * 注册异常类型的错误码及描述
 *
 * @author 刘镇 (suninformation@163.com) on 2018-12-12 22:10
 * @since 2.0.6
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionProcessor {

    /**
     * @return 异常错误码，非0数字
     */
    int code();

    /**
     * @return 默认错误描述
     */
    String msg();
}
