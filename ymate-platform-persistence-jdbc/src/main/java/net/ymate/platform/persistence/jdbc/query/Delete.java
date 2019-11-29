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

import net.ymate.platform.commons.util.ExpressionUtils;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.Params;
import net.ymate.platform.core.persistence.base.EntityMeta;
import net.ymate.platform.core.persistence.base.IEntity;
import net.ymate.platform.persistence.jdbc.IDatabase;
import net.ymate.platform.persistence.jdbc.IDatabaseConnectionHolder;
import net.ymate.platform.persistence.jdbc.JDBC;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DELETE语句对象
 *
 * @author 刘镇 (suninformation@163.com) on 15/5/12 下午6:03
 */
public final class Delete extends Query<Delete> {

    private final List<String> froms = new ArrayList<>();

    private final Fields fields = Fields.create();

    private final List<Join> joins = new ArrayList<>();

    private Where where;

    public static Delete create() throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder());
    }

    public static Delete create(Class<? extends IEntity> entityClass) throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder()).from(entityClass);
    }

    public static Delete create(String prefix, Class<? extends IEntity> entityClass) throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder()).from(prefix, entityClass, null);
    }

    public static Delete create(Class<? extends IEntity> entityClass, String alias) throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder()).from(null, entityClass, alias);
    }

    public static Delete create(String prefix, Class<? extends IEntity> entityClass, String alias) throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder()).from(prefix, entityClass, alias);
    }

    public static Delete create(String prefix, String tableName, String alias) throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder(), prefix, tableName, alias, true);
    }

    public static Delete create(String tableName, String alias) throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder(), null, tableName, alias, true);
    }

    public static Delete create(String tableName, String alias, boolean safePrefix) throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder(), null, tableName, alias, safePrefix);
    }

    public static Delete create(String tableName) throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder(), null, tableName, null, true);
    }

    public static Delete create(String tableName, boolean safePrefix) throws Exception {
        return new Delete(JDBC.get().getDefaultConnectionHolder(), null, tableName, null, safePrefix);
    }

    public static Delete create(Select select) {
        Delete target = new Delete(select.connectionHolder(), null, select.toString(), null, false);
        target.where().param(select.params());
        return target;
    }

    public static Delete create(IDatabase owner) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder());
    }

    public static Delete create(IDatabase owner, Class<? extends IEntity> entityClass) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder()).from(entityClass);
    }

    public static Delete create(IDatabase owner, String prefix, Class<? extends IEntity> entityClass) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder()).from(prefix, entityClass, null);
    }

    public static Delete create(IDatabase owner, Class<? extends IEntity> entityClass, String alias) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder()).from(null, entityClass, alias);
    }

    public static Delete create(IDatabase owner, String prefix, Class<? extends IEntity> entityClass, String alias) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder()).from(prefix, entityClass, alias);
    }

    public static Delete create(IDatabase owner, String prefix, String tableName, String alias) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder(), prefix, tableName, alias, true);
    }

    public static Delete create(IDatabase owner, String tableName, String alias) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder(), null, tableName, alias, true);
    }

    public static Delete create(IDatabase owner, String tableName, String alias, boolean safePrefix) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder(), null, tableName, alias, safePrefix);
    }

    public static Delete create(IDatabase owner, String tableName) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder(), null, tableName, null, true);
    }

    public static Delete create(IDatabase owner, String tableName, boolean safePrefix) throws Exception {
        return new Delete(owner.getDefaultConnectionHolder(), null, tableName, null, safePrefix);
    }

    //

    public static Delete create(IDatabaseConnectionHolder connectionHolder) {
        return new Delete(connectionHolder);
    }

    public static Delete create(IDatabaseConnectionHolder connectionHolder, Class<? extends IEntity> entityClass) {
        return new Delete(connectionHolder).from(entityClass);
    }

    public static Delete create(IDatabaseConnectionHolder connectionHolder, String prefix, Class<? extends IEntity> entityClass) {
        return new Delete(connectionHolder).from(prefix, entityClass, null);
    }

    public static Delete create(IDatabaseConnectionHolder connectionHolder, Class<? extends IEntity> entityClass, String alias) {
        return new Delete(connectionHolder).from(null, entityClass, alias);
    }

    public static Delete create(IDatabaseConnectionHolder connectionHolder, String prefix, Class<? extends IEntity> entityClass, String alias) {
        return new Delete(connectionHolder).from(prefix, entityClass, alias);
    }

    public static Delete create(IDatabaseConnectionHolder connectionHolder, String prefix, String tableName, String alias) {
        return new Delete(connectionHolder, prefix, tableName, alias, true);
    }

    public static Delete create(IDatabaseConnectionHolder connectionHolder, String tableName, String alias) {
        return new Delete(connectionHolder, null, tableName, alias, true);
    }

    public static Delete create(IDatabaseConnectionHolder connectionHolder, String tableName, String alias, boolean safePrefix) {
        return new Delete(connectionHolder, null, tableName, alias, safePrefix);
    }

    public static Delete create(IDatabaseConnectionHolder connectionHolder, String tableName) {
        return new Delete(connectionHolder, null, tableName, null, true);
    }

    public static Delete create(IDatabaseConnectionHolder connectionHolder, String tableName, boolean safePrefix) {
        return new Delete(connectionHolder, null, tableName, null, safePrefix);
    }

    private Delete(IDatabaseConnectionHolder connectionHolder) {
        super(connectionHolder);
    }

    private Delete(IDatabaseConnectionHolder connectionHolder, String prefix, String from, String alias, boolean safePrefix) {
        super(connectionHolder);
        //
        if (safePrefix) {
            from(null, buildSafeTableName(prefix, from, true), alias);
        } else {
            from(prefix, from, alias);
        }
    }

    public Delete from(Class<? extends IEntity> entityClass) {
        return from(null, buildSafeTableName(null, EntityMeta.createAndGet(entityClass), true), null);
    }

    public Delete from(Class<? extends IEntity> entityClass, String alias) {
        return from(null, buildSafeTableName(null, EntityMeta.createAndGet(entityClass), true), alias);
    }

    public Delete from(String prefix, Class<? extends IEntity> entityClass, String alias) {
        return from(null, buildSafeTableName(prefix, EntityMeta.createAndGet(entityClass), true), alias);
    }

    public Delete from(Select select) {
        Delete target = from(null, select.toString(), null);
        target.where().param(select.params());
        return target;
    }

    public Delete from(String tableName, String alias) {
        return from(null, buildSafeTableName(null, tableName, true), alias);
    }

    public Delete from(String tableName) {
        return from(null, buildSafeTableName(null, tableName, true), null);
    }

    public Delete from(String prefix, String from, String alias) {
        from = buildSafeTableName(prefix, from, false);
        if (StringUtils.isNotBlank(alias)) {
            from = from.concat(StringUtils.SPACE).concat(alias);
        }
        this.froms.add(from);
        return this;
    }

    public Delete table(String tableName) {
        this.fields.add(tableName);
        return this;
    }

    public Delete table(String prefix, String tableName) {
        this.fields.add(prefix, tableName);
        return this;
    }

    public Delete join(Join join) {
        joins.add(join);
        where().param(join.params());
        return this;
    }

    public Delete where(Where where) {
        where().where(where);
        return this;
    }

    public Params params() {
        return where().params();
    }

    public Delete param(Object param) {
        where().param(param);
        return this;
    }

    public Delete param(Params params) {
        where().param(params);
        return this;
    }

    public Where where() {
        if (this.where == null) {
            this.where = Where.create(connectionHolder());
        }
        return where;
    }

    public Delete where(Cond cond) {
        where().cond().cond(cond);
        return this;
    }

    public Delete orderBy(OrderBy orderBy) {
        where().orderBy().orderBy(orderBy);
        return this;
    }

    @Override
    public String toString() {
        ExpressionUtils expression = ExpressionUtils.bind(getExpressionStr("DELETE ${fields} FROM ${froms} ${joins} ${where}"));
        if (queryHandler() != null) {
            queryHandler().beforeBuild(expression, this);
        }
        List<String> variables = expression.getVariables();
        //
        if (!fields.fields().isEmpty() && variables.contains("fields")) {
            expression.set("fields", StringUtils.join(fields.fields(), LINE_END_FLAG));
        }
        expression.set("forms", StringUtils.join(froms, LINE_END_FLAG));
        if (where != null && variables.contains("where")) {
            expression.set("where", where.toString());
        }
        if (variables.contains("joins")) {
            expression.set("joins", StringUtils.join(joins, StringUtils.SPACE));
        }
        if (queryHandler() != null) {
            queryHandler().afterBuild(expression, this);
        }
        return StringUtils.trimToEmpty(expression.clean().getResult());
    }

    public SQL toSQL() {
        return SQL.create(this);
    }

    public int execute() throws Exception {
        return toSQL().execute(connectionHolder());
    }
}
