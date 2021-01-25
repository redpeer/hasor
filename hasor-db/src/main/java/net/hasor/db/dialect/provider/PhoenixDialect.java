/*
 * Copyright 2002-2010 the original author or authors.
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
package net.hasor.db.dialect.provider;
import net.hasor.db.dialect.BoundSql;
import net.hasor.db.dialect.SqlDialect;
import net.hasor.db.types.mapping.FieldInfo;
import net.hasor.db.types.mapping.TableInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PhoenixDialect 对象名有大小写敏感不敏感的问题
 * @version : 2020-10-31
 * @author 赵永春 (zyc@hasor.net)
 */
public class PhoenixDialect implements SqlDialect {
    @Override
    public String buildSelect(TableInfo tableInfo, FieldInfo fieldInfo) {
        return "\"" + fieldInfo.getColumnName() + "\"";
    }

    @Override
    public String buildTableName(TableInfo tableInfo) {
        return "\"" + tableInfo.getTableName() + "\"";
    }

    @Override
    public String buildConditionName(TableInfo tableInfo, FieldInfo fieldInfo) {
        return "\"" + fieldInfo.getColumnName() + "\"";
    }

    @Override
    public BoundSql getPageSql(BoundSql boundSql, int start, int limit) {
        List<Object> paramArrays = new ArrayList<>(Arrays.asList(boundSql.getArgs()));
        //
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(boundSql.getSqlString());
        if (limit > 0) {
            sqlBuilder.append(" LIMIT ? ");
            paramArrays.add(limit);
        }
        if (start > 0) {
            sqlBuilder.append(" OFFSET ? ");
            paramArrays.add(start);
        }
        //
        return new BoundSql.BoundSqlObj(sqlBuilder.toString(), paramArrays.toArray());
    }
}