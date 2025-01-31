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
package net.ymate.platform.validation.validate;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.*;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/24 下午6:11
 * @since 2.0.6
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VRSAData {

    /**
     * @return 自定参数名称
     */
    String value() default StringUtils.EMPTY;

    /**
     * @return RSA密钥数据提供者类
     * @since 2.1.0
     */
    Class<? extends IRSAKeyProvider> providerClass();

    /**
     * @return 自定义验证消息
     */
    String msg() default StringUtils.EMPTY;
}
