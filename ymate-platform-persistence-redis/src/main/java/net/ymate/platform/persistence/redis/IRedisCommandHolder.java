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
package net.ymate.platform.persistence.redis;

import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.persistence.IConnectionHolder;

/**
 * Redis连接对象持有者接口
 *
 * @author 刘镇 (suninformation@163.com) on 15/12/4 上午12:59
 */
@Ignored
public interface IRedisCommandHolder extends IConnectionHolder<IRedis, IRedisCommander, IRedisDataSourceConfig> {

    /**
     * 获取数据源适配器
     *
     * @return 返回数据源适配器对象
     */
    IRedisDataSourceAdapter getDataSourceAdapter();
}
