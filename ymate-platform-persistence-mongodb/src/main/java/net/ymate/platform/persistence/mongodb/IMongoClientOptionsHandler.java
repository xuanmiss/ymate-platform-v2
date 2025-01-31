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
package net.ymate.platform.persistence.mongodb;

import com.mongodb.MongoClientOptions;
import net.ymate.platform.core.beans.annotation.Ignored;

/**
 * 自定义MongoDB客户端初始化参数配置处理器
 *
 * @author 刘镇 (suninformation@163.com) on 15/11/22 上午3:08
 */
@Ignored
public interface IMongoClientOptionsHandler {

    /**
     * 初始化MongoDB客户端参数配置
     *
     * @param dataSourceName 数据源名称
     * @return 返回客户端参数配置构建器对象
     */
    MongoClientOptions.Builder handler(String dataSourceName);
}
