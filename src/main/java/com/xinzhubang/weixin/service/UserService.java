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

import com.xinzhubang.weixin.XZBServletListener;
import com.xinzhubang.weixin.repository.GuestBookRepository;
import com.xinzhubang.weixin.repository.SchoolRepository;
import com.xinzhubang.weixin.repository.UserCollectionRepository;
import com.xinzhubang.weixin.repository.UserCardRepository;
import com.xinzhubang.weixin.repository.UserInfoRepository;
import com.xinzhubang.weixin.repository.UserRepository;
import com.xinzhubang.weixin.util.Sessions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
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
import org.b3log.latke.util.CollectionUtils;
import org.b3log.latke.util.Strings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户服务.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.3.5.0, Apr 2, 2014
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
    private UserCollectionRepository userCollectionRepository;

    @Inject
    private SchoolRepository schoolRepository;

    @Inject
    private GuestBookRepository guestBookRepository;

    /**
     * 根据用户 id 获取用户的留言.
     *
     * @param userId
     * @param pageNum
     * @return
     */
    public List<JSONObject> getGuestBooksByUserId(final String userId, final int pageNum) {
        try {
            final Query query = new Query().setFilter(new PropertyFilter("MemberID", FilterOperator.EQUAL, userId));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);

            final JSONObject result = guestBookRepository.get(query);

            final List<JSONObject> results = CollectionUtils.jsonArrayToList(result.getJSONArray(Keys.RESULTS));
            final List<JSONObject> ret = new ArrayList<JSONObject>();

            for (final JSONObject j : results) {
                boolean duplicated = false;

                for (final JSONObject k : ret) { // 按发送者去重
                    if (j.optInt("SendID") == k.optInt("SendID")) {
                        duplicated = true;

                        break;
                    }
                }

                if (!duplicated) {
                    j.put("toUser", userRepository.get(j.getString("MemberID")));
                    j.put("fromUser", userRepository.get(j.getString("SendID")));
                    j.put("CreateTime", j.opt("PostTime"));

                    j.put("type", "gb"); // 类型是留言

                    final String memberId = j.getString("MemberID");
                    final String sendId = j.getString("SendID");

                    List<Filter> filters = new ArrayList<Filter>();
                    filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, memberId));
                    filters.add(new PropertyFilter("SendID", FilterOperator.EQUAL, sendId));

                    Query q = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
                    final long c1 = guestBookRepository.count(q);

                    long c2 = 0;
                    // 自己留言给自己，查一次就行，互相留言要反过来再查一次
                    if (!sendId.equals(memberId)) {
                        filters = new ArrayList<Filter>();
                        filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, sendId));
                        filters.add(new PropertyFilter("SendID", FilterOperator.EQUAL, memberId));

                        q = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
                        c2 = guestBookRepository.count(q);
                    }

                    j.put("count", c1 + c2);

                    ret.add(j);
                }
            }

            return ret;

        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取用户 [id=" + userId + "] 悄悄话列表异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 根据留言 id 获取留言详情.
     *
     * @param id
     * @return
     * @throws RepositoryException
     * @throws JSONException
     */
    public JSONObject getGuestBook(final String id) throws RepositoryException, JSONException {
        final JSONObject result = guestBookRepository.get(id);

        final String sendId = result.getString("SendID");
        final String memberId = result.getString("MemberID");

        final List<JSONObject> list = new ArrayList<JSONObject>();

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new PropertyFilter("SendID", FilterOperator.EQUAL, sendId));
        filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, memberId));

        Query q = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
        JSONArray array = guestBookRepository.get(q).optJSONArray(Keys.RESULTS);
        list.addAll(CollectionUtils.<JSONObject>jsonArrayToList(array));

        // 自己留言给自己，查一次就行，互相留言要反过来再查一次
        if (!sendId.equals(memberId)) {
            filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("SendID", FilterOperator.EQUAL, memberId));
            filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, sendId));

            q = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            array = guestBookRepository.get(q).optJSONArray(Keys.RESULTS);
            list.addAll(CollectionUtils.<JSONObject>jsonArrayToList(array));
        }

        result.put("toUser", userRepository.get(memberId));
        result.put("fromUser", userRepository.get(sendId));
        result.put("type", "gb");
        result.put("list", (Object) list);

        for (final JSONObject j : list) {
            j.put("toUser", userRepository.get(j.getString("MemberID")));
            j.put("fromUser", userRepository.get(j.getString("SendID")));
            j.put("type", "gb");
            j.put("CreateTime", j.opt("PostTime"));
        }

        Collections.sort(list, new Comparator<JSONObject>() {

            @Override
            public int compare(final JSONObject o1, final JSONObject o2) {
                final String t1 = o1.optString("CreateTime");
                final String t2 = o2.optString("CreateTime");

                return t2.compareTo(t1);
            }
        });

        result.put("CreateTime", result.opt("PostTime"));

        return result;
    }

    /**
     * 发送指定的留言.
     *
     * @param guestBook 指定的悄悄话，例如：
     * <pre>
     * {
     *     "MemberID": int,
     *     "SendID": int,
     *     "GBookContent": ""
     * }
     * </pre>
     *
     * @return
     * @throws ServiceException
     */
    @Transactional
    public boolean sendGuestBook(final JSONObject guestBook) throws ServiceException {
        try {
            guestBookRepository.add(guestBook);

            return true;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "发留言 [" + guestBook.toString() + "] 异常", e);

            return false;
        }
    }

    /**
     * 获取指定的社区圈子里指定类型（学生/老师）的名片列表。
     *
     * @param community 指定的社区圈子，例如：
     * <pre>
     * {
     *     "AreaCode": "",
     *     "UniversityCode": "",
     *     "CollegeCode": "", // 可选的
     *     "type": "" // 类型：teacher, student
     * }
     * </pre>
     *
     * @param pageNum
     * @return
     */
    public List<JSONObject> getUserCards(final JSONObject community, final int pageNum) {
        try {
            final String areaCode = community.getString("AreaCode");
            final String universityCode = community.getString("UniversityCode");
            final String collegeCode = community.optString("CollegeCode", "-1");

            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("AreaCode", FilterOperator.EQUAL, areaCode));
            filters.add(new PropertyFilter("UniversityCode", FilterOperator.EQUAL, universityCode));

            if (!"-1".equals(collegeCode)) {
                filters.add(new PropertyFilter("CollegeCode", FilterOperator.EQUAL, collegeCode));
            }

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE * 3);
            final JSONObject userInfoResult = userInfoRepository.get(query);

            final JSONArray userInfos = userInfoResult.optJSONArray(Keys.RESULTS);

            final String typeArg = "teacher".equals(community.optString("type", "teacher")) ? "teacher" : "student";

            final List<JSONObject> ret = new ArrayList<JSONObject>();

            for (int i = 0; i < userInfos.length(); i++) {
                final JSONObject userInfo = userInfos.optJSONObject(i);
                final String memberId = userInfo.optString("MemberID");
                final List<JSONObject> userCard = getUserCard(memberId, typeArg);

                for (final JSONObject card : userCard) {
                    ret.add(card);
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
     * @param pageNum
     * @return
     */
    public List<JSONObject> getFollowingUsers(final String userId, final int pageNum) {
        try {
            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, userId));
            filters.add(new PropertyFilter("CollectionType", FilterOperator.EQUAL, 3));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);

            final JSONObject result = userCollectionRepository.get(query);

            final List<JSONObject> ret = new ArrayList<JSONObject>();

            final JSONArray attentions = result.optJSONArray(Keys.RESULTS);
            for (int i = 0; i < attentions.length(); i++) {
                final JSONObject attention = attentions.optJSONObject(i);
                final String followingCardId = attention.optString("ItemID");

                final JSONObject card = getUserCard(followingCardId);
                if (null != card) {
                    ret.add(card);
                }
            }

            for (final JSONObject followingUser : ret) {
                final String followingUserId = followingUser.optString("T_User_ID");
                followingUser.put("isFollow", true);

                final JSONObject community = getUserInfo(followingUserId);

                followingUser.put("Area", community.optString("Area"));
                followingUser.put("University", community.optString("University"));
                followingUser.put("CollegeCode", community.optString("CollegeCode"));
                followingUser.put("College", community.optString("College"));
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取用户关注列表异常", e);

            return Collections.emptyList();
        }
    }

    public JSONObject getUserCard(final String cardId) {
        try {
            final JSONObject ret = userCardRepository.get(cardId);

            if (null == ret) {
                return null;
            }

            final String userId = ret.optString("T_User_ID");

            final JSONObject user = userRepository.get(userId);
            ret.put("userName", user.optString("user_name"));
            ret.put("nickName", user.optString("nick_name"));
            ret.put("Memberlevels", user.optString("Memberlevels"));

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "查询用户名片 [cardId=" + cardId + "] 异常", e);

            return null;
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
    public List<JSONObject> getUserCard(final String userId, final String type) throws ServiceException {
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

            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(result.getJSONArray(Keys.RESULTS));
            if (null == ret || ret.isEmpty()) {
                return Collections.emptyList();
            }

            final JSONObject user = userRepository.get(userId);
            for (final JSONObject card : ret) {
                card.put("userName", user.optString("user_name"));
                card.put("nickName", user.optString("nick_name"));
                card.put("Memberlevels", user.optString("Memberlevels"));
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "查询用户名片 [userId=" + userId + ", type=" + type + "] 异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 设置用户名片。
     *
     * @param userCard
     * <pre>
     * {
     *     "nickName": "",
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
            final String userId = userCard.optString("T_User_ID");

            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("T_User_ID", FilterOperator.EQUAL, userId));
            filters.add(new PropertyFilter("Property", FilterOperator.EQUAL, userCard.optInt("Property")));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            final JSONObject result = userCardRepository.get(query);
            JSONObject property = result.optJSONArray(Keys.RESULTS).optJSONObject(0);

            // 1. 更新用户名片
            if (null != property) {
                property.remove("rownum");

                property.put("PropertyTitle", userCard.optString("PropertyTitle"));
                property.put("PropertyRemark", userCard.optString("PropertyRemark"));

                userCardRepository.update(property.optString("ID"), property);
            } else {
                property = new JSONObject();

                property.put("PropertyTitle", userCard.optString("PropertyTitle"));
                property.put("PropertyRemark", userCard.optString("PropertyRemark"));
                property.put("Property", userCard.optInt("Property"));
                property.put("T_User_ID", userId);

                userCardRepository.add(property);
            }

            // 2. 更新用户昵称
            final JSONObject user = userRepository.get(userId);
            user.put("nick_name", userCard.optString("nickName"));

            user.remove("rownum");

            userRepository.update(userId, user);

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

            return result.getJSONArray(Keys.RESULTS).optJSONObject(0);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "根据用户名和邮箱 [" + email + "][" + userName + "] 获取用户异常", e);

            return null;
        }
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

    public boolean isFollow(final String memberId, final String attentionCardId) {
        try {
            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, memberId));
            filters.add(new PropertyFilter("ItemID", FilterOperator.EQUAL, attentionCardId));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));

            final JSONObject result = userCollectionRepository.get(query);

            return null != result.optJSONArray(Keys.RESULTS).optJSONObject(0);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "查询用户名片关注 [memberId=" + memberId + ", attentionCardId=" + attentionCardId + "] 异常", e);

            return false;
        }
    }

    /**
     * 用户名片关注（名片收藏）.
     *
     * @param userCardAttention
     * @return
     */
    @Transactional
    public boolean addUserCardAttention(final JSONObject userCardAttention) {
        try {
            final String userId = userCardAttention.optString("MemberID");
            final String cardId = userCardAttention.optString("ItemID");

            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, userId));
            filters.add(new PropertyFilter("ItemID", FilterOperator.EQUAL, cardId));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));

            final JSONObject result = userCollectionRepository.get(query);
            if (null != result.optJSONArray(Keys.RESULTS).optJSONObject(0)) { // 已经关注过了
                return true;
            }

            userCardAttention.put("CollectionType", 3);

            userCollectionRepository.add(userCardAttention);

            return true;
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "添加用户名片关注 [" + userCardAttention.toString() + "] 异常", e);

            return false;
        }
    }

    @Transactional
    public boolean removeUserAttention(final String memberId, final String attentionCardId) {
        try {
            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, memberId));
            filters.add(new PropertyFilter("ItemID", FilterOperator.EQUAL, attentionCardId));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));

            final JSONObject result = userCollectionRepository.get(query);
            final JSONObject attention = result.optJSONArray(Keys.RESULTS).optJSONObject(0);

            userCollectionRepository.remove(attention.optString("ID"));

            return true;
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "移除用户关注 [memberId=" + memberId + ", attentionCardId=" + attentionCardId + "] 异常", e);

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

    /**
     * 获得学校学科
     *
     * @param pid
     * @return
     * @throws org.json.JSONException
     */
    public List<JSONObject> getSchool(String pid) throws JSONException {
        try {
            final Query query = new Query();
            if (StringUtils.isNotEmpty(pid)) {
                query.setFilter(new PropertyFilter("ParentID", FilterOperator.EQUAL, Integer.parseInt(pid)));
            } else {
                query.setFilter(new PropertyFilter("Type", FilterOperator.EQUAL, 1));
            }
            final JSONObject result = schoolRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);
            return ret;
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "获取用户关注列表异常", e);
            return Collections.emptyList();
        }
    }

    /**
     * 获取指定用户 id 的用户扩展信息（圈子）。
     *
     * @param userId 指定用户 id
     * @return
     */
    public JSONObject getUserInfo(final String userId) {
        final Query query = new Query();
        query.setFilter(new PropertyFilter("MemberID", FilterOperator.EQUAL, Integer.parseInt(userId)));

        try {
            final JSONObject result = userInfoRepository.get(query);

            JSONObject ret = result.optJSONArray(Keys.RESULTS).optJSONObject(0);

            if (null == ret) {
                // PC 端的已有用户可能没设置圈子，这里给出默认值，避免 NPE
                ret = new JSONObject();

                ret.put("Area", "");
                ret.put("AreaCode", "");
                ret.put("University", "");
                ret.put("UniversityCode", "");
                ret.put("CollegeCode", "");
                ret.put("College", "");
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取用户 [userId=" + userId + "] 圈子异常", e);

            return null;
        }
    }

    /**
     * 添加或修改圈子
     *
     * @param userId
     * @param provinceId
     * @param schoolId
     * @param collegId
     * @throws java.lang.Exception
     */
    @Transactional
    public void saveOrUpdateUserInfo(String userId, String provinceId, String schoolId, String collegId) throws Exception {
        JSONObject province = schoolRepository.get(new Query().setFilter(new PropertyFilter("Did", FilterOperator.EQUAL, Integer.parseInt(provinceId))));
        province = province.optJSONArray(Keys.RESULTS).optJSONObject(0);

        JSONObject school = schoolRepository.get(new Query().setFilter(new PropertyFilter("Did", FilterOperator.EQUAL, Integer.parseInt(schoolId))));
        school = school.optJSONArray(Keys.RESULTS).optJSONObject(0);

        JSONObject colleg = schoolRepository.get(new Query().setFilter(new PropertyFilter("Did", FilterOperator.EQUAL, Integer.parseInt(collegId))));
        colleg = colleg.optJSONArray(Keys.RESULTS).optJSONObject(0);

        final Query query = new Query();
        query.setFilter(new PropertyFilter("MemberID", FilterOperator.EQUAL, Integer.parseInt(userId)));
        JSONObject userInfo = userInfoRepository.get(query);
        userInfo = userInfo.getJSONArray(Keys.RESULTS).optJSONObject(0);

        if (null == userInfo) {
            userInfo = new JSONObject();
            userInfo.put("MemberID", userId);
            userInfo.put("Area", province.get("Name"));
            userInfo.put("AreaCode", province.get("Did"));
            userInfo.put("University", school.get("Name"));
            userInfo.put("UniversityCode", school.get("Did"));
            if ("-1".equals(collegId)) {
                userInfo.put("College", "无限制");
                userInfo.put("CollegeCode", -1);
            } else {
                userInfo.put("College", colleg.get("Name"));
                userInfo.put("CollegeCode", colleg.get("Did"));
            }
            userInfo.put("Major", "无限制");
            userInfo.put("MajorCode", -1);

            userInfoRepository.add(userInfo);
        } else {
            userInfo.put("MemberID", userId);
            userInfo.put("Area", province.get("Name"));
            userInfo.put("AreaCode", province.get("Did"));
            userInfo.put("University", school.get("Name"));
            userInfo.put("UniversityCode", school.get("Did"));
            if ("-1".equals(collegId)) {
                userInfo.put("College", "无限制");
                userInfo.put("CollegeCode", -1);
            } else {
                userInfo.put("College", colleg.get("Name"));
                userInfo.put("CollegeCode", colleg.get("Did"));
            }

            userInfo.remove("rownum");

            userInfoRepository.update(userInfo.getString("ID"), userInfo);
        }
    }
}
