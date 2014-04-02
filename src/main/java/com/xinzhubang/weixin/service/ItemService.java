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
import com.xinzhubang.weixin.repository.ItemRepository;
import com.xinzhubang.weixin.repository.UserRepository;
import com.xinzhubang.weixin.repository.WhisperRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.b3log.latke.repository.SortDirection;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目服务.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.6.0.0, Apr 2, 2014
 * @since 1.0.0
 */
@Service
public class ItemService {

    private static final Logger LOGGER = Logger.getLogger(ItemService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private WhisperRepository whisperRepository;

    @Inject
    private UserService userService;

    /**
     * 发送指定的悄悄话.
     *
     * @param whisper 指定的悄悄话，例如：
     * <pre>
     * {
     *     "KeyID": int,
     *     "FromID": int,
     *     "ToID": int,
     *     "CONTENT": ""
     * }
     * </pre>
     *
     * @return
     * @throws ServiceException
     */
    @Transactional
    public boolean sendWhisper(final JSONObject whisper) throws ServiceException {
        try {
            whisper.put("Category", 1);
            whisper.put("CreateTime", new Timestamp(System.currentTimeMillis()));

            whisperRepository.add(whisper);

            return true;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "发悄悄话 [" + whisper.toString() + "] 异常", e);

            return false;
        }
    }

    /**
     * 根据用户 id 获取用户的悄悄话.
     *
     * @param userId
     * @param pageNum
     * @return
     */
    public List<JSONObject> getWhispersByUserId(final String userId, final int pageNum) {
        try {
            final Query query = new Query().setFilter(new PropertyFilter("ToID", FilterOperator.EQUAL, userId));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);

            final JSONObject result = whisperRepository.get(query);

            final List<JSONObject> results = CollectionUtils.jsonArrayToList(result.getJSONArray(Keys.RESULTS));
            final List<JSONObject> ret = new ArrayList<JSONObject>();

            for (final JSONObject j : results) {
                boolean duplicated = false;

                for (final JSONObject k : ret) { // 按项目去重
                    if (j.optInt("KeyID") == k.optInt("KeyID")) {
                        duplicated = true;

                        break;
                    }
                }

                if (!duplicated) {
                    final int keyId = j.getInt("KeyID");
                    final JSONObject item = itemRepository.get(keyId + "");
                    j.put("item", item);

                    j.put("toUser", userRepository.get(j.getString("ToID")));
                    j.put("fromUser", userRepository.get(j.getString("FromID")));

                    List<Filter> filters = new ArrayList<Filter>();
                    filters.add(new PropertyFilter("KeyID", FilterOperator.EQUAL, keyId));
                    filters.add(new PropertyFilter("FromID", FilterOperator.EQUAL, j.getString("FromID")));
                    filters.add(new PropertyFilter("ToID", FilterOperator.EQUAL, j.getString("ToID")));

                    Query q = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
                    final long c1 = whisperRepository.count(q);

                    filters = new ArrayList<Filter>();
                    filters.add(new PropertyFilter("KeyID", FilterOperator.EQUAL, keyId));
                    filters.add(new PropertyFilter("FromID", FilterOperator.EQUAL, j.getString("ToID")));
                    filters.add(new PropertyFilter("ToID", FilterOperator.EQUAL, j.getString("FromID")));

                    q = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
                    final long c2 = whisperRepository.count(q);

                    j.put("count", c1 + c2);
                    j.put("type", "w"); // 类型是悄悄话

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
     * 根据消息 id 获取悄悄话详情.
     *
     * @param id
     * @return
     * @throws RepositoryException
     * @throws JSONException
     */
    public JSONObject getWhisper(final String id) throws RepositoryException, JSONException {
        final JSONObject result = whisperRepository.get(id);

        final int keyId = result.getInt("KeyID");
        final JSONObject item = itemRepository.get(keyId + "");
        result.put("item", item);

        final List<JSONObject> list = new ArrayList<JSONObject>();

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new PropertyFilter("KeyID", FilterOperator.EQUAL, keyId));
        filters.add(new PropertyFilter("FromID", FilterOperator.EQUAL, result.getString("FromID")));
        filters.add(new PropertyFilter("ToID", FilterOperator.EQUAL, result.getString("ToID")));

        Query q = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
        JSONArray array = whisperRepository.get(q).optJSONArray(Keys.RESULTS);
        list.addAll(CollectionUtils.<JSONObject>jsonArrayToList(array));

        filters = new ArrayList<Filter>();
        filters.add(new PropertyFilter("KeyID", FilterOperator.EQUAL, keyId));
        filters.add(new PropertyFilter("FromID", FilterOperator.EQUAL, result.getString("ToID")));
        filters.add(new PropertyFilter("ToID", FilterOperator.EQUAL, result.getString("FromID")));

        q = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
        array = whisperRepository.get(q).optJSONArray(Keys.RESULTS);
        list.addAll(CollectionUtils.<JSONObject>jsonArrayToList(array));

        result.put("toUser", userRepository.get(result.getString("ToID")));
        result.put("fromUser", userRepository.get(result.getString("FromID")));
        result.put("type", "w");
        result.put("list", (Object) list);

        Collections.sort(list, new Comparator<JSONObject>() {

            @Override
            public int compare(final JSONObject o1, final JSONObject o2) {
                final String t1 = o1.optString("CreateTime");
                final String t2 = o2.optString("CreateTime");

                return t2.compareTo(t1);
            }
        });

        for (final JSONObject j : list) {
            j.put("toUser", userRepository.get(j.getString("ToID")));
            j.put("fromUser", userRepository.get(j.getString("FromID")));
            j.put("type", "w");
        }

        return result;
    }

    /**
     * 发布指定的服务.
     *
     * @param sale 指定的服务，例如：
     * <pre>
     * {
     *     "Name": "",
     *     "ItemType": int,
     *     "MemberID": int,
     *     "ItemContent": "",
     *     "Price": int,
     *     "Area": "",
     *     "AreaCode": "",
     *     "University": "",
     *     "UniversityCode": "",
     *     "College": "",
     *     "CollegeCode": ""
     * }
     * </pre>
     *
     * @return
     * @throws ServiceException
     */
    @Transactional
    public boolean publishSale(final JSONObject sale) throws ServiceException {
        try {
            sale.put("DemandOrService", 1); // 服务

            final String userId = sale.getString("MemberID");

            final JSONObject user = userRepository.get(userId);

            final String realName = user.optString("RealName");
            final String mobile = user.optString("mobile");
            final String email = user.optString("email");

            sale.put("RealName", realName);
            sale.put("Email", email);
            sale.put("LinkMan", realName);
            sale.put("Mobile", mobile);

            itemRepository.add(sale);

            // 查询用户是否设置过老师名片
            final List<JSONObject> userCards = userService.getUserCard(userId, "teacher");

            if (userCards.isEmpty()) { // 没有设置过老师名片
                // 默认创建
                final JSONObject userCard = new JSONObject();
                userCard.put("nickName", user.getString("user_name"));
                userCard.put("PropertyTitle", "");
                userCard.put("PropertyRemark", "");
                userCard.put("Property", 1);

                userCard.put("T_User_ID", Integer.valueOf(userId));

                userService.setUserCard(userCard);
            }

            return true;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "发布出售 [" + sale.toString() + "] 异常", e);

            return false;
        }
    }

    /**
     * 发布指定的需求.
     *
     * @param demand 指定的需求，例如：
     * <pre>
     * {
     *     "Name": "",
     *     "ItemType": int,
     *     "MemberID": int,
     *     "ItemContent": "",
     *     "Price": int,
     *     "Area": "",
     *     "AreaCode": "",
     *     "University": "",
     *     "UniversityCode": "",
     *     "College": "",
     *     "CollegeCode": ""
     * }
     * </pre>
     *
     * @return
     * @throws ServiceException
     */
    @Transactional
    public boolean publishDemand(final JSONObject demand) throws ServiceException {
        try {
            demand.put("DemandOrService", 0); // 需求

            final String userId = demand.getString("MemberID");

            final JSONObject user = userRepository.get(userId);

            final String realName = user.optString("RealName");
            final String mobile = user.optString("mobile");
            final String email = user.optString("email");

            demand.put("RealName", realName);
            demand.put("Email", email);
            demand.put("LinkMan", realName);
            demand.put("Mobile", mobile);

            itemRepository.add(demand);

            // 查询用户是否设置过学生名片
            final List<JSONObject> userCards = userService.getUserCard(userId, "student");

            if (userCards.isEmpty()) { // 没有设置过学生名片
                // 默认创建
                final JSONObject userCard = new JSONObject();
                userCard.put("nickName", user.getString("user_name"));
                userCard.put("PropertyTitle", "");
                userCard.put("PropertyRemark", "");
                userCard.put("Property", 0);

                userCard.put("T_User_ID", Integer.valueOf(userId));

                userService.setUserCard(userCard);
            }

            return true;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "发布需求 [" + demand.toString() + "] 异常", e);

            return false;
        }
    }

    /**
     * 获取指定用户 id 的用户最近发布的一个项目（需求/服务）.
     *
     * @param userId 指定用户 id
     * @return 项目（需求/服务）
     */
    public JSONObject getLatestItem(final String userId) {
        final Query query = new Query();

        query.setFilter(new PropertyFilter("MemberID", FilterOperator.EQUAL, userId)).addSort("CreateTime", SortDirection.DESCENDING).
                setPageSize(1);

        try {
            final JSONObject result = itemRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);

            return results.optJSONObject(0);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取用户 [id=" + userId + "] 最新项目异常", e);

            return null;
        }
    }

    /**
     * 获取指定的用户的服务项目.
     *
     * @param userId 指定的用户的 Id
     * @param pageNum 指定的分页页号
     *
     * @return
     */
    public List<JSONObject> getUserSales(final String userId, final int pageNum) {
        try {
            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, userId));
            filters.add(new PropertyFilter("DemandOrService", FilterOperator.EQUAL, 1));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);
            final JSONObject result = itemRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取用户 [" + userId + "] 的服务项目异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 获取指定的用户的需求项目.
     *
     * @param userId 指定的用户的 Id
     * @param pageNum 指定的分页页号
     *
     * @return
     */
    public List<JSONObject> getUserDemands(final String userId, final int pageNum) {
        try {
            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, userId));
            filters.add(new PropertyFilter("DemandOrService", FilterOperator.EQUAL, 0));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);
            final JSONObject result = itemRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取用户 [" + userId + "] 的需求项目异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 获取指定的社区圈子中的出售项目.
     *
     * @param community 指定的社区圈子，例如：
     * <pre>
     * {
     *     "AreaCode": "",
     *     "UniversityCode": "",
     *     "CollegeCode": "", // 可选的
     *     "type": int // 项目类型：1：资料2：答疑3：授课4：公开课
     * }
     * </pre>
     *
     * @param pageNum 指定的分页页号
     *
     * @return
     */
    public List<JSONObject> getSales(final JSONObject community, final int pageNum) {
        try {
            final String areaCode = community.getString("AreaCode");
            final String universityCode = community.getString("UniversityCode");
            final String collegeCode = community.optString("CollegeCode", "-1");
            final int type = community.getInt("type");

            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("AreaCode", FilterOperator.EQUAL, areaCode));
            filters.add(new PropertyFilter("UniversityCode", FilterOperator.EQUAL, universityCode));
            if (!"-1".equals(collegeCode)) {
                filters.add(new PropertyFilter("CollegeCode", FilterOperator.EQUAL, collegeCode));
            }
            filters.add(new PropertyFilter("ItemType", FilterOperator.EQUAL, type));
            filters.add(new PropertyFilter("DemandOrService", FilterOperator.EQUAL, 1)); // 服务

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);
            final JSONObject result = itemRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);

            for (final JSONObject sale : ret) {
                final String userId = sale.getString("MemberID");
                final JSONObject user = userRepository.get(userId);
                sale.put("userName", user.getString("user_name"));
                sale.put("IDCardStatus", user.optInt("IDCardStatus"));
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取社区圈子中的出售项目异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 获取指定的社区圈子中的需求项目.
     *
     * @param community 指定的社区圈子，例如：
     * <pre>
     * {
     *     "AreaCode": "",
     *     "UniversityCode": "",
     *     "CollegeCode": "", // 可选的
     *     "type": int // 项目类型：1：资料2：答疑3：授课4：公开课
     * }
     * </pre>
     *
     * @param pageNum 指定的分页页号
     *
     * @return
     */
    public List<JSONObject> getDemands(final JSONObject community, final int pageNum) {
        try {
            final String areaCode = community.getString("AreaCode");
            final String universityCode = community.getString("UniversityCode");
            final String collegeCode = community.optString("CollegeCode", "-1");
            final int type = community.getInt("type");

            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("AreaCode", FilterOperator.EQUAL, areaCode));
            filters.add(new PropertyFilter("UniversityCode", FilterOperator.EQUAL, universityCode));
            if (!"-1".equals(collegeCode)) {
                filters.add(new PropertyFilter("CollegeCode", FilterOperator.EQUAL, collegeCode));
            }
            filters.add(new PropertyFilter("ItemType", FilterOperator.EQUAL, type));
            filters.add(new PropertyFilter("DemandOrService", FilterOperator.EQUAL, 0)); // 需求

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);
            final JSONObject result = itemRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);

            for (final JSONObject demand : ret) {
                final String userId = demand.getString("MemberID");
                final JSONObject user = userRepository.get(userId);
                demand.put("userName", user.getString("user_name"));
                demand.put("IDCardStatus", user.optInt("IDCardStatus"));
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取社区圈子中的需求项目异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 获取指定的 Id 的出售项目.
     *
     * @param id 指定的 Id
     *
     * @return
     */
    public JSONObject getSale(final String id) {
        try {
            final JSONObject ret = itemRepository.get(id);

            final String userId = ret.getString("MemberID");
            final JSONObject user = userRepository.get(userId);

            ret.put("user", user);

            ret.put("userName", user.getString("user_name"));
            ret.put("IDCardStatus", user.optInt("IDCardStatus"));

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取出售项目 [id=" + id + "] 异常", e);

            return null;
        }
    }

    /**
     * 获取指定的 Id 的需求项目.
     *
     * @param id 指定的 Id
     *
     * @return
     */
    public JSONObject getDemand(final String id) {
        try {
            final JSONObject ret = itemRepository.get(id);

            final String userId = ret.getString("MemberID");
            final JSONObject user = userRepository.get(userId);

            ret.put("user", user);

            ret.put("userName", user.getString("user_name"));
            ret.put("IDCardStatus", user.optInt("IDCardStatus"));

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取需求项目 [id=" + id + "] 异常", e);

            return null;
        }
    }
}
