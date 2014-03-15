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

import com.xinzhubang.weixin.repository.UserCardRepository;
import com.xinzhubang.weixin.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.CompositeFilter;
import org.b3log.latke.repository.CompositeFilterOperator;
import org.b3log.latke.repository.Filter;
import org.b3log.latke.repository.FilterOperator;
import org.b3log.latke.repository.PropertyFilter;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.json.JSONObject;

/**
 * 用户服务.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 6, 2014
 * @since 1.0.0
 */
@Service
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserCardRepository userCardRepository;

    /**
     * 根据用户 id 与类型（老师或学生）获取用户名片。
     *
     * @param userId 用户 id
     * @param type 类型，老师：t；学生：s；e：企业，其他情况默认老师
     * @return
     * @throws ServiceException
     */
    public JSONObject getUserCard(final int userId, final String type) throws ServiceException {
        final List<Filter> filters = new ArrayList<Filter>();
        filters.add(new PropertyFilter("t_user_id", FilterOperator.EQUAL, userId));

        int property;
        if ("t".equals(type)) {
            property = 1;
        } else if ("s".equals(type)) {
            property = 0;
        } else if ("e".equals(type)) {
            property = 2;
        } else {
            property = 0;
        }

        filters.add(new PropertyFilter("property", FilterOperator.EQUAL, property));

        final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));

        try {
            final JSONObject result = userCardRepository.get(query);

            return result.getJSONArray(Keys.RESULTS).optJSONObject(0);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "查询用户名片 [userId=" + userId + ", type=" + type + "] 异常", e);

            return null;
        }
    }

    public JSONObject getUserByName(final String userName) throws ServiceException {
        final Query query = new Query().setFilter(new PropertyFilter("user_name", FilterOperator.EQUAL, userName));

        try {
            final JSONObject result = userRepository.get(query);

            return result.getJSONArray(Keys.RESULTS).optJSONObject(0);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "根据用户名 [" + userName + "] 获取用户异常", e);

            return null;
        }
    }
    public JSONObject getUserByEmailOrUsername(final String email,final String userName){
         final Query query = new Query().setFilter(new PropertyFilter("user_name", FilterOperator.EQUAL, userName)).setFilter(new PropertyFilter("email",FilterOperator.EQUAL,userName));
         try {
            final JSONObject result = userRepository.get(query);

            return result.getJSONArray(Keys.RESULTS).optJSONObject(0);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "根据用户名和邮箱 [" + email + "][" + userName + "] 获取用户异常", e);

            return null;
        }
    }
    @Transactional
    public String addUser(final JSONObject user){
        String id = null;
        System.out.println(user.toString());
        try {
            id = userRepository.add(user);
        } catch (RepositoryException ex) {
            LOGGER.log(Level.ERROR, "保存用户出错！", ex);
            return null;       
        }
        return id;
    }
}
