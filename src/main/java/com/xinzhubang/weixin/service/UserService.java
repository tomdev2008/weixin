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

import com.xinzhubang.weixin.repository.UserAttentionRepository;
import com.xinzhubang.weixin.repository.UserCardRepository;
import com.xinzhubang.weixin.repository.UserInfoRepository;
import com.xinzhubang.weixin.repository.UserRepository;
import com.xinzhubang.weixin.util.Sessions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.b3log.latke.util.Strings;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 用户服务.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.0, Mar 18, 2014
 * @since 1.0.0
 */
@Service
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserCardRepository userCardRepository;

    @Inject
    private UserInfoRepository userInfoRepository;

    @Inject
    private UserAttentionRepository userAttentionRepository;

    /**
     * 获取指定的社区圈子里指定类型（学生/老师）的名片列表。
     *
     * @param community 指定的社区圈子，例如：
     * <pre>
     * {
     *     "areaCode": "",
     *     "universityCode": "",
     *     "collegeCode": "", // 可选的
     *     "type": "" // 类型：teacher, student
     * }
     * </pre>
     *
     * @param pageNum
     * @return
     */
    public List<JSONObject> getUserCards(final JSONObject community, final int pageNum) {
        try {
            final String areaCode = community.getString("areaCode");
            final String universityCode = community.getString("universityCode");
            final String collegeCode = community.optString("collegeCode", "-1");

            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("AreaCode", FilterOperator.EQUAL, areaCode));
            filters.add(new PropertyFilter("UniversityCode", FilterOperator.EQUAL, universityCode));
            filters.add(new PropertyFilter("CollegeCode", FilterOperator.EQUAL, collegeCode));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(50);
            final JSONObject userInfoResult = userInfoRepository.get(query);

            final JSONArray userInfos = userInfoResult.optJSONArray(Keys.RESULTS);

            final String typeArg = "teacher".equals(community.optString("type", "teacher")) ? "teacher" : "student";

            final List<JSONObject> ret = new ArrayList<JSONObject>();

            for (int i = 0; i < userInfos.length(); i++) {
                final JSONObject userInfo = userInfos.optJSONObject(i);
                final String memberId = userInfo.optString("MemberID");
                final JSONObject userCard = getUserCard(memberId, typeArg);
                if (null != userCard) {
                    ret.add(userCard);
                }
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取社区圈子中的出售项目异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 获取指定的用户 id 的关注列表.
     *
     * @param userId 指定的用户 id
     * @return
     */
    public List<JSONObject> getFollowers(final String userId, final int pageNum) {
        try {
            final Query query = new Query().setFilter(new PropertyFilter("MemberID", FilterOperator.EQUAL, userId));
            query.setCurrentPageNum(pageNum).setPageSize(10);

            final JSONObject result = userAttentionRepository.get(query);

            final List<JSONObject> ret = new ArrayList<JSONObject>();

            final JSONArray attentions = result.optJSONArray(Keys.RESULTS);
            for (int i = 0; i < attentions.length(); i++) {
                final JSONObject attention = attentions.optJSONObject(i);
                final String followerId = attention.optString("AttentionMemberID");

                final JSONObject userCard = getUserCard(followerId, "a");

                ret.add(userCard);
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取用户关注列表异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 根据用户 id 与类型（老师或学生）获取用户名片。
     *
     * @param userId 用户 id
     * @param type 类型，老师：teacher；学生：student；e：企业；a：不区分，其他情况默认老师
     * @return
     * @throws ServiceException
     */
    public JSONObject getUserCard(final String userId, final String type) throws ServiceException {
        final List<Filter> filters = new ArrayList<Filter>();
        filters.add(new PropertyFilter("T_User_ID", FilterOperator.EQUAL, userId));

        int property;
        if ("a".equals(type)) {
            property = -1;
        } else if ("teacher".equals(type)) {
            property = 1;
        } else if ("student".equals(type)) {
            property = 0;
        } else if ("e".equals(type)) {
            property = 2;
        } else {
            property = 1;
        }

        if (-1 != property) {
            filters.add(new PropertyFilter("property", FilterOperator.EQUAL, property));
        } else {
            filters.add(new PropertyFilter("property", FilterOperator.NOT_EQUAL, property));
        }

        final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));

        try {
            final JSONObject result = userCardRepository.get(query);

            final JSONObject ret = result.getJSONArray(Keys.RESULTS).optJSONObject(0);
            if (null == ret) {
                return null;
            }

            final JSONObject user = userRepository.get(userId);
            ret.put("userName", user.getString("user_name"));

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "查询用户名片 [userId=" + userId + ", type=" + type + "] 异常", e);

            return null;
        }
    }

    /**
     * 设置用户名片。
     *
     * @param userCard
     * <pre>
     * {
     *     "T_User_ID": int,
     *     "Property": int,
     *     "PropertyTitle": "",
     *     "PropertyRemark": ""
     * }
     * </pre>
     *
     * @return
     */
    @Transactional
    public boolean setUserCard(final JSONObject userCard) {
        try {
            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("T_User_ID", FilterOperator.EQUAL, userCard.optInt("T_User_ID")));
            filters.add(new PropertyFilter("Property", FilterOperator.EQUAL, userCard.optInt("Property")));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            final JSONObject result = userCardRepository.get(query);
            JSONObject property = result.optJSONArray(Keys.RESULTS).optJSONObject(0);

            if (null == property) {
                property = userCard;
            } else {
                userCardRepository.remove(property.optString("ID"));
            }

            userCardRepository.add(userCard);

            return true;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "设置用户名片 [" + userCard + "] 异常", e);

            return false;
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

    public JSONObject getUserByEmailOrUsername(final String email, final String userName) {

        final List<Filter> filters = new ArrayList<Filter>();
        filters.add(new PropertyFilter("user_name", FilterOperator.EQUAL, userName));
        filters.add(new PropertyFilter("email", FilterOperator.EQUAL, userName));
        final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.OR, filters));
        try {
            final JSONObject result = userRepository.get(query);
            if (result != null) {
                return result.getJSONArray(Keys.RESULTS).optJSONObject(0);
            } else {
                return null;
            }
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "根据用户名和邮箱 [" + email + "][" + userName + "] 获取用户异常", e);
        }
        return null;
    }

    /**
     * Gets the current user.
     *
     * @param request the specified request
     * @return the current user, {@code null} if not found
     * @throws ServiceException service exception
     */
    public JSONObject getCurrentUser(final HttpServletRequest request) throws ServiceException {
        final JSONObject currentUser = Sessions.currentUser(request);

        if (null == currentUser) {
            return null;
        }

        final String userId = currentUser.optString("userId");

        try {
            return userRepository.get(userId);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取当前登录用户异常", e);

            return null;
        }
    }

    @Transactional
    public String addUser(final JSONObject user) {
        String id = null;
        user.put("group_id", 1);
        try {
            id = userRepository.add(user);
        } catch (RepositoryException ex) {
            LOGGER.log(Level.ERROR, "保存用户出错！", ex);
            return null;
        }
        return id;
    }

    public boolean isFollow(final String memberId, final String attentionMemberId) {
        try {
            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, memberId));
            filters.add(new PropertyFilter("AttentionMemberID", FilterOperator.EQUAL, attentionMemberId));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));

            final JSONObject result = userAttentionRepository.get(query);

            return null != result.optJSONArray(Keys.RESULTS).optJSONObject(0);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "查询用户关注 [memberId=" + memberId + ", attentionMemberId=" + attentionMemberId + "] 异常", e);

            return false;
        }
    }

    @Transactional
    public boolean addUserAttention(final JSONObject userAttention) {
        try {
            userAttentionRepository.add(userAttention);

            return true;
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "添加用户关注 [" + userAttention.toString() + "] 异常", e);

            return false;
        }
    }

    @Transactional
    public boolean removeUserAttention(final int memberId, final int attentionMemberId) {
        try {
            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, memberId));
            filters.add(new PropertyFilter("AttentionMemberID", FilterOperator.EQUAL, attentionMemberId));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));

            final JSONObject result = userAttentionRepository.get(query);
            final JSONObject attention = result.optJSONArray(Keys.RESULTS).optJSONObject(0);

            userAttentionRepository.remove(attention.optString("ID"));

            return true;
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "移除用户关注 [memberId=" + memberId + ", attentionMemberId=" + attentionMemberId + "] 异常", e);

            return false;
        }
    }

    /**
     * Tries to login with cookie.
     *
     * @param request the specified request
     * @param response the specified response
     * @return returns {@code true} if logged in, returns {@code false} otherwise
     */
    public boolean tryLogInWithCookie(final HttpServletRequest request, final HttpServletResponse response) {
        final Cookie[] cookies = request.getCookies();
        if (null == cookies || 0 == cookies.length) {
            return false;
        }

        try {
            for (final Cookie cookie : cookies) {
                if (!"b3log-latke".equals(cookie.getName())) {
                    continue;
                }

                final JSONObject cookieJSONObject = new JSONObject(cookie.getValue());

                final String userId = cookieJSONObject.optString("userId");
                if (Strings.isEmptyOrNull(userId)) {
                    break;
                }

                final JSONObject user = userRepository.get(userId);
                if (null == user) {
                    break;
                }

                final int id = user.optInt("id");
                final String userPassword = user.optString("password");
                final String password = cookieJSONObject.optString("password");
                if (userPassword.equals(password)) {
                    Sessions.login(request, response, user);
                    LOGGER.log(Level.DEBUG, "Logged in with cookie[id={0}]", userId);

                    return true;
                }
            }
        } catch (final Exception e) {
            LOGGER.log(Level.WARN, "Parses cookie failed, clears the cookie[name=b3log-latke]", e);

            final Cookie cookie = new Cookie("b3log-latke", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");

            response.addCookie(cookie);
        }

        return false;
    }
}
