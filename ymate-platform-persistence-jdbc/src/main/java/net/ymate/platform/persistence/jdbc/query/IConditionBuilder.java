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
package net.ymate.platform.persistence.jdbc.query;

/**
 * 条件对象构建器接口
 *
 * @author 刘镇 (suninformation@163.com) on 2019-11-16 11:31
 * @since 2.1.0
 */
public interface IConditionBuilder {

    /**
     * 构建条件对象
     *
     * @return 返回构建的条件对象
     */
    Cond build();
}
