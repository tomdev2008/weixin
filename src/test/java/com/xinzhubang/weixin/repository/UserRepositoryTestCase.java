/*
 * Copyright (c) 2014, XinZhuBang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinzhubang.weixin.repository;

import java.util.Collection;
import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.ioc.LatkeBeanManager;
import org.b3log.latke.ioc.Lifecycle;
import org.b3log.latke.ioc.config.Discoverer;
import org.b3log.latke.repository.FilterOperator;
import org.b3log.latke.repository.PropertyFilter;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.jdbc.util.JdbcRepositories;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 用户数据存取单元测试.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.0, Mar 14, 2014
 * @since 1.0.0
 */
public class UserRepositoryTestCase {

    private UserRepository userRepository;

    @BeforeClass
    public void beforeClass() throws Exception {
        Latkes.initRuntimeEnv();

        // 数据库表主键名为 id
        JdbcRepositories.setDefaultKeyName("id");

        final Collection<Class<?>> classes = Discoverer.discover("com.xinzhubang.weixin");
        Lifecycle.startApplication(classes);

        final LatkeBeanManager beanManager = Lifecycle.getBeanManager();

        userRepository = beanManager.getReference(UserRepository.class);
    }

    @Test
    public void getUser() throws Exception {
        System.out.println("getUser");
        
        final Query query = new Query();
        query.setFilter(new PropertyFilter("user_name", FilterOperator.EQUAL, "88250"));

        final JSONObject result = userRepository.get(query);
        System.out.println(result.toString());
    }
    
     @Test
    public void getUserById() throws Exception {
         System.out.println("getUserById");
        
        final JSONObject result = userRepository.get("1475");
        
        System.out.println(result.toString());
    }
}
