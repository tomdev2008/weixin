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
package com.xinzhubang.weixin.service;

import java.util.Collection;
import org.b3log.latke.Latkes;
import org.b3log.latke.ioc.LatkeBeanManager;
import org.b3log.latke.ioc.Lifecycle;
import org.b3log.latke.ioc.config.Discoverer;
import org.b3log.latke.repository.jdbc.util.JdbcRepositories;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 用户服务存取单元测试.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.1, Mar 15, 2014
 * @since 1.0.0
 */
public class UserServiceTestCase {

    private UserService userService;

    @BeforeClass
    public void beforeClass() throws Exception {
        Latkes.initRuntimeEnv();

        // 数据库表主键名为 id
        JdbcRepositories.setDefaultKeyName("id");

        final Collection<Class<?>> classes = Discoverer.discover("com.xinzhubang.weixin");
        Lifecycle.startApplication(classes);

        final LatkeBeanManager beanManager = Lifecycle.getBeanManager();

        userService = beanManager.getReference(UserService.class);
    }

    @Test
    public void getUserByName() throws Exception {
        System.out.println("getUserByName");

        JSONObject user = userService.getUserByName("summer1");
        System.out.println(user);
    }

    @Test
    public void getUserCard() throws Exception {
        System.out.println("getUserCard");

        JSONObject userCard = userService.getUserCard("1475", "s");
        System.out.println(userCard);

        userCard = userService.getUserCard("1475", "t");
        System.out.println(userCard);
    }
}
