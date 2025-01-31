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
package net.ymate.platform.persistence.jdbc.scaffold;

/**
 * 实体及属性命名过滤器接口
 *
 * @author 刘镇 (suninformation@163.com) on 17/4/18 上午8:43
 */
public interface INamedFilter {

    enum Type {

        /**
         * 表
         */
        TABLE,

        /**
         * 字段
         */
        COLUMN
    }

    /**
     * 处理表/字段名称
     *
     * @param type     被过滤值类型
     * @param original 原始名称
     * @return 返回经过处理的名称字符串
     */
    String filter(Type type, String original);
}
