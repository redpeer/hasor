/*
 * Copyright 2008-2009 the original author or authors.
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
package net.hasor.db.jdbc.lambda;
import net.hasor.core.AppContext;
import net.hasor.core.Hasor;
import net.hasor.db.jdbc.core.JdbcTemplate;
import net.hasor.db.jdbc.extractor.RowMapperResultSetExtractor;
import net.hasor.db.jdbc.mapper.ColumnMapRowMapper;
import net.hasor.test.db.AbstractDbTest;
import net.hasor.test.db.SingleDsModule;
import net.hasor.test.db.dto.TB_User;
import net.hasor.test.db.dto.TbUser;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.hasor.test.db.utils.TestUtils.*;

/***
 * 批量Insert语句执行
 * @version : 2014-1-13
 * @author 赵永春 (zyc@hasor.net)
 */
public class LambdaTest extends AbstractDbTest {
    @Test
    public void lambda_select_1() throws SQLException {
        try (AppContext appContext = Hasor.create().build(new SingleDsModule(true))) {
            JdbcTemplate jdbcTemplate = appContext.getInstance(JdbcTemplate.class);
            //
            List<TbUser> tbUsers1 = jdbcTemplate.lambda(TbUser.class).queryForList();
            List<String> collect1 = tbUsers1.stream().map(TbUser::getName).collect(Collectors.toList());
            assert collect1.size() == 3;
            assert collect1.contains(beanForData1().getName());
            assert collect1.contains(beanForData2().getName());
            assert collect1.contains(beanForData3().getName());
            //
            List<Map<String, Object>> tbUsers2 = jdbcTemplate.lambda(TbUser.class).queryForMapList();
            List<String> collect2 = tbUsers2.stream().map(tbUser -> {
                return (String) tbUser.get("name");
            }).collect(Collectors.toList());
            assert collect2.size() == 3;
            assert collect2.contains(beanForData1().getName());
            assert collect2.contains(beanForData2().getName());
            assert collect2.contains(beanForData3().getName());
            //
            List<String> collect3 = new ArrayList<>();
            jdbcTemplate.lambda(TbUser.class).query((rs, rowNum) -> {
                collect3.add(rs.getString("name"));
            });
            assert collect3.size() == 3;
            assert collect3.contains(beanForData1().getName());
            assert collect3.contains(beanForData2().getName());
            assert collect3.contains(beanForData3().getName());
            //
            List<Map<String, Object>> tbUsers4 = jdbcTemplate.lambda(TbUser.class)//
                    .query(new RowMapperResultSetExtractor<>(new ColumnMapRowMapper()));
            List<String> collect4 = tbUsers4.stream().map(tbUser -> {
                return (String) tbUser.get("name");
            }).collect(Collectors.toList());
            assert collect4.size() == 3;
            assert collect4.contains(beanForData1().getName());
            assert collect4.contains(beanForData2().getName());
            assert collect4.contains(beanForData3().getName());
            //
            List<Map<String, Object>> tbUsers5 = jdbcTemplate.lambda(TbUser.class).query(new ColumnMapRowMapper());
            List<String> collect5 = tbUsers5.stream().map(tbUser -> {
                return (String) tbUser.get("name");
            }).collect(Collectors.toList());
            assert collect5.size() == 3;
            assert collect5.contains(beanForData1().getName());
            assert collect5.contains(beanForData2().getName());
            assert collect5.contains(beanForData3().getName());
        }
    }

    @Test
    public void lambda_select_2() throws SQLException {
        try (AppContext appContext = Hasor.create().build(new SingleDsModule(true))) {
            JdbcTemplate jdbcTemplate = appContext.getInstance(JdbcTemplate.class);
            //
            List<TbUser> tbUsers1 = jdbcTemplate.lambda(TbUser.class).selectAll().queryForList();
            List<TbUser> tbUsers2 = jdbcTemplate.lambda(TbUser.class).queryForList();
            assert tbUsers1.size() == 3;
            assert tbUsers2.size() == 3;
            //
            Map<String, Object> forData1 = mapForData1();
            List<TbUser> tbUsers3 = jdbcTemplate.lambda(TbUser.class)//
                    .eq(TbUser::getAccount, forData1.get("loginName")).queryForList();
            assert tbUsers3.size() == 1;
            assert tbUsers3.get(0).getAccount().equals("muhammad");
            assert tbUsers3.get(0).getAccount().equals(forData1.get("loginName"));
            assert tbUsers3.get(0).getUid().equals(forData1.get("userUUID"));
        }
    }

    @Test
    public void lambda_select_3() throws SQLException {
        try (AppContext appContext = Hasor.create().build(new SingleDsModule(true))) {
            JdbcTemplate jdbcTemplate = appContext.getInstance(JdbcTemplate.class);
            //
            List<TB_User> tbUsers1 = jdbcTemplate.lambda(TB_User.class).selectAll().queryForList();
            List<TB_User> tbUsers2 = jdbcTemplate.lambda(TB_User.class).queryForList();
            assert tbUsers1.size() == 3;
            assert tbUsers2.size() == 3;
            //
            Map<String, Object> forData1 = mapForData1();
            List<TB_User> tbUsers3 = jdbcTemplate.lambda(TB_User.class)//
                    .eq(TB_User::getLoginName, forData1.get("loginName")).queryForList();
            assert tbUsers3.size() == 1;
            assert tbUsers3.get(0).getLoginName().equals("muhammad");
            assert tbUsers3.get(0).getLoginName().equals(forData1.get("loginName"));
            assert tbUsers3.get(0).getUserUUID().equals(forData1.get("userUUID"));
        }
    }

    @Test
    public void lambda_select_4() throws SQLException {
        try (AppContext appContext = Hasor.create().build(new SingleDsModule(true))) {
            JdbcTemplate jdbcTemplate = appContext.getInstance(JdbcTemplate.class);
            //
            TbUser tbUser = jdbcTemplate.lambda(TbUser.class)//
                    .eq(TbUser::getAccount, "muhammad").apply("limit 1").queryForObject();
            //
            assert tbUser.getName().equals("默罕默德");
        }
    }

    @Test
    public void lambda_select_5() throws SQLException {
        try (AppContext appContext = Hasor.create().build(new SingleDsModule(true))) {
            JdbcTemplate jdbcTemplate = appContext.getInstance(JdbcTemplate.class);
            //
            TB_User tbUser = jdbcTemplate.lambda(TbUser.class)//
                    .eq(TbUser::getAccount, "muhammad").apply("limit 1")//
                    .wrapperType(TB_User.class).queryForObject();
            //
            assert tbUser.getName().equals("默罕默德");
            assert tbUser.getLoginName().equals("muhammad");
        }
    }

    @Test
    public void lambda_select_6() throws SQLException {
        try (AppContext appContext = Hasor.create().build(new SingleDsModule(true))) {
            JdbcTemplate jdbcTemplate = appContext.getInstance(JdbcTemplate.class);
            //
            Map<String, Object> tbUser = jdbcTemplate.lambda(TbUser.class)//
                    .eq(TbUser::getAccount, "muhammad").apply("limit 1")//
                    .queryForMap();
            //
            assert tbUser.get("name").equals("默罕默德");
            assert tbUser.get("loginName").equals("muhammad");
        }
    }
}