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
 * Update语句对象
 *
 * @author 刘镇 (suninformation@163.com) on 15/5/12 下午6:02
 */
public final class Update extends Query<Update> {

    private final List<String> tables = new ArrayList<>();

    private final Fields fields = Fields.create();

    private final List<Join> joins = new ArrayList<>();

    private Where where;

    public static Update create() throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder());
    }

    public static Update create(String prefix, Class<? extends IEntity> entityClass, String alias) throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder()).table(prefix, entityClass, alias);
    }

    public static Update create(String prefix, Class<? extends IEntity> entityClass) throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder()).table(prefix, entityClass, null);
    }

    public static Update create(Class<? extends IEntity> entityClass) throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder()).table(entityClass, null);
    }

    public static Update create(String prefix, String tableName, String alias) throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder(), prefix, tableName, alias, true);
    }

    public static Update create(String prefix, String tableName, String alias, boolean safePrefix) throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder(), prefix, tableName, alias, safePrefix);
    }

    public static Update create(String tableName, String alias) throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder(), null, tableName, alias, true);
    }

    public static Update create(String tableName, String alias, boolean safePrefix) throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder(), null, tableName, alias, safePrefix);
    }

    public static Update create(String tableName) throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder(), null, tableName, null, true);
    }

    public static Update create(String tableName, boolean safePrefix) throws Exception {
        return new Update(JDBC.get().getDefaultConnectionHolder(), null, tableName, null, safePrefix);
    }

    public static Update create(IDatabase owner) throws Exception {
        return new Update(owner.getDefaultConnectionHolder());
    }

    public static Update create(IDatabase owner, String prefix, Class<? extends IEntity> entityClass, String alias) throws Exception {
        return new Update(owner.getDefaultConnectionHolder()).table(prefix, entityClass, alias);
    }

    public static Update create(IDatabase owner, String prefix, Class<? extends IEntity> entityClass) throws Exception {
        return new Update(owner.getDefaultConnectionHolder()).table(prefix, entityClass, null);
    }

    public static Update create(IDatabase owner, Class<? extends IEntity> entityClass) throws Exception {
        return new Update(owner.getDefaultConnectionHolder()).table(entityClass, null);
    }

    public static Update create(IDatabase owner, String prefix, String tableName, String alias) throws Exception {
        return new Update(owner.getDefaultConnectionHolder(), prefix, tableName, alias, true);
    }

    public static Update create(IDatabase owner, String prefix, String tableName, String alias, boolean safePrefix) throws Exception {
        return new Update(owner.getDefaultConnectionHolder(), prefix, tableName, alias, safePrefix);
    }

    public static Update create(IDatabase owner, String tableName, String alias) throws Exception {
        return new Update(owner.getDefaultConnectionHolder(), null, tableName, alias, true);
    }

    public static Update create(IDatabase owner, String tableName, String alias, boolean safePrefix) throws Exception {
        return new Update(owner.getDefaultConnectionHolder(), null, tableName, alias, safePrefix);
    }

    public static Update create(IDatabase owner, String tableName) throws Exception {
        return new Update(owner.getDefaultConnectionHolder(), null, tableName, null, true);
    }

    public static Update create(IDatabase owner, String tableName, boolean safePrefix) throws Exception {
        return new Update(owner.getDefaultConnectionHolder(), null, tableName, null, safePrefix);
    }

    //

    public static Update create(IDatabaseConnectionHolder connectionHolder) {
        return new Update(connectionHolder);
    }

    public static Update create(IDatabaseConnectionHolder connectionHolder, String prefix, Class<? extends IEntity> entityClass, String alias) {
        return new Update(connectionHolder).table(prefix, entityClass, alias);
    }

    public static Update create(IDatabaseConnectionHolder connectionHolder, String prefix, Class<? extends IEntity> entityClass) {
        return new Update(connectionHolder).table(prefix, entityClass, null);
    }

    public static Update create(IDatabaseConnectionHolder connectionHolder, Class<? extends IEntity> entityClass) {
        return new Update(connectionHolder).table(entityClass, null);
    }

    public static Update create(IDatabaseConnectionHolder connectionHolder, String prefix, String tableName, String alias) {
        return new Update(connectionHolder, prefix, tableName, alias, true);
    }

    public static Update create(IDatabaseConnectionHolder connectionHolder, String prefix, String tableName, String alias, boolean safePrefix) {
        return new Update(connectionHolder, prefix, tableName, alias, safePrefix);
    }

    public static Update create(IDatabaseConnectionHolder connectionHolder, String tableName, String alias) {
        return new Update(connectionHolder, null, tableName, alias, true);
    }

    public static Update create(IDatabaseConnectionHolder connectionHolder, String tableName, String alias, boolean safePrefix) {
        return new Update(connectionHolder, null, tableName, alias, safePrefix);
    }

    public static Update create(IDatabaseConnectionHolder connectionHolder, String tableName) {
        return new Update(connectionHolder, null, tableName, null, true);
    }

    public static Update create(IDatabaseConnectionHolder connectionHolder, String tableName, boolean safePrefix) {
        return new Update(connectionHolder, null, tableName, null, safePrefix);
    }

    private Update(IDatabaseConnectionHolder connectionHolder) {
        super(connectionHolder);
    }

    private Update(IDatabaseConnectionHolder connectionHolder, String prefix, String tableName, String alias, boolean safePrefix) {
        super(connectionHolder);
        //
        table(prefix, tableName, alias, safePrefix);
    }

    public Update table(Class<? extends IEntity> entityClass) {
        return table(null, buildSafeTableName(null, EntityMeta.createAndGet(entityClass), true), null, false);
    }

    public Update table(Class<? extends IEntity> entityClass, String alias) {
        return table(null, buildSafeTableName(null, EntityMeta.createAndGet(entityClass), true), alias, false);
    }

    public Update table(String prefix, Class<? extends IEntity> entityClass, String alias) {
        return table(null, buildSafeTableName(prefix, EntityMeta.createAndGet(entityClass), true), alias, false);
    }

    public Update table(String tableName, String alias) {
        return table(null, tableName, alias, true);
    }

    public Update table(String tableName) {
        return table(null, tableName, null, true);
    }

    public Update table(String prefix, String from, String alias) {
        return table(prefix, from, alias, true);
    }

    public Update table(String prefix, String from, String alias, boolean safePrefix) {
        from = buildSafeTableName(prefix, from, safePrefix);
        if (StringUtils.isNotBlank(alias)) {
            from = String.format("%s %s", from, alias);
        }
        this.tables.add(from);
        return this;
    }

    public Fields fields() {
        return this.fields;
    }

    public Update field(String field) {
        return field(field, true);
    }

    public Update field(String field, boolean wrapIdentifier) {
        this.fields.add(wrapIdentifier ? wrapIdentifierField(field) : field);
        return this;
    }

    public Update field(String prefix, String field) {
        return field(prefix, field, true);
    }

    public Update field(String prefix, String field, boolean wrapIdentifier) {
        this.fields.add(prefix, wrapIdentifier ? wrapIdentifierField(field) : field);
        return this;
    }

    public Update field(String prefix, String field, String alias) {
        return field(prefix, field, alias, true);
    }

    public Update field(String prefix, String field, String alias, boolean wrapIdentifier) {
        this.fields.add(prefix, wrapIdentifier ? wrapIdentifierField(field) : field, alias);
        return this;
    }

    public Update field(Fields fields) {
        return field(fields, true);
    }

    public Update field(Fields fields, boolean wrapIdentifier) {
        Fields newFields = checkFieldExcluded(fields);
        this.fields.add(wrapIdentifier ? wrapIdentifierFields(newFields.toArray()) : newFields);
        return this;
    }

    public Update field(String prefix, Fields fields) {
        return field(prefix, fields, true);
    }

    public Update field(String prefix, Fields fields, boolean wrapIdentifier) {
        checkFieldExcluded(fields).fields().forEach((field) -> this.fields.add(prefix, wrapIdentifier ? wrapIdentifierField(field) : field));
        return this;
    }

    public Update join(Join join) {
        joins.add(join);
        where().param(join.params());
        return this;
    }

    public Update innerJoin(Select select, String alias, Cond on) {
        return join(Join.inner(select).alias(alias).on(on));
    }

    public Update leftJoin(Select select, String alias, Cond on) {
        return join(Join.left(select).alias(alias).on(on));
    }

    public Update rightJoin(Select select, String alias, Cond on) {
        return join(Join.right(select).alias(alias).on(on));
    }

    //

    public Update innerJoin(String from, Cond on) {
        return join(Join.inner(connectionHolder(), from).on(on));
    }

    public Update leftJoin(String from, Cond on) {
        return join(Join.left(connectionHolder(), from).on(on));
    }

    public Update rightJoin(String from, Cond on) {
        return join(Join.right(connectionHolder(), from).on(on));
    }

    //

    public Update innerJoin(String prefix, String from, Cond on) {
        return join(Join.inner(connectionHolder(), prefix, from).on(on));
    }

    public Update leftJoin(String prefix, String from, Cond on) {
        return join(Join.left(connectionHolder(), prefix, from).on(on));
    }

    public Update rightJoin(String prefix, String from, Cond on) {
        return join(Join.right(connectionHolder(), prefix, from).on(on));
    }

    //

    public Update innerJoin(String prefix, String from, String alias, Cond on) {
        return join(Join.inner(connectionHolder(), prefix, from).alias(alias).on(on));
    }

    public Update leftJoin(String prefix, String from, String alias, Cond on) {
        return join(Join.left(connectionHolder(), prefix, from).alias(alias).on(on));
    }

    public Update rightJoin(String prefix, String from, String alias, Cond on) {
        return join(Join.right(connectionHolder(), prefix, from).alias(alias).on(on));
    }

    //

    public Update innerJoin(String prefix, String from, boolean safePrefix, String alias, Cond on) {
        return join(Join.inner(connectionHolder(), prefix, from, safePrefix).alias(alias).on(on));
    }

    public Update leftJoin(String prefix, String from, boolean safePrefix, String alias, Cond on) {
        return join(Join.left(connectionHolder(), prefix, from, safePrefix).alias(alias).on(on));
    }

    public Update rightJoin(String prefix, String from, boolean safePrefix, String alias, Cond on) {
        return join(Join.right(connectionHolder(), prefix, from, safePrefix).alias(alias).on(on));
    }

    public Update where(Where where) {
        where().where(where);
        return this;
    }

    public Params params() {
        return where().params();
    }

    public Update param(Object param) {
        where().param(param);
        return this;
    }

    public Update param(Params params) {
        where().param(params);
        return this;
    }

    public Where where() {
        if (this.where == null) {
            this.where = Where.create(connectionHolder());
        }
        return where;
    }

    public Update where(Cond cond) {
        where().cond().cond(cond);
        return this;
    }

    public Update orderBy(OrderBy orderBy) {
        where().orderBy().orderBy(orderBy);
        return this;
    }

    @Override
    public String toString() {
        ExpressionUtils expression = ExpressionUtils.bind(getExpressionStr("UPDATE ${tableNames} ${joins} SET ${fields} ${where}"));
        if (queryHandler() != null) {
            queryHandler().beforeBuild(expression, this);
        }
        List<String> variables = expression.getVariables();
        //
        expression.set("tableNames", StringUtils.join(tables, LINE_END_FLAG));
        expression.set("fields", StringUtils.join(fields.fields(), "=?,"));
        if (variables.contains("joins")) {
            expression.set("joins", StringUtils.join(joins, StringUtils.SPACE));
        }
        if (where != null && variables.contains("where")) {
            expression.set("where", where.toString());
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
