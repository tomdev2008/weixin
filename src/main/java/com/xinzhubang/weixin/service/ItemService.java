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

import com.xinzhubang.weixin.repository.ItemRepository;
import com.xinzhubang.weixin.repository.UserCardRepository;
import com.xinzhubang.weixin.repository.UserRepository;
import com.xinzhubang.weixin.repository.WhisperRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
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
import org.b3log.latke.util.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目服务.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.3.0.0, Mar 20, 2014
 * @since 1.0.0
 */
@Service
public class ItemService {

    private static final Logger LOGGER = Logger.getLogger(ItemService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserCardRepository userCardRepository;

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private WhisperRepository whisperRepository;

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
     * 根据用户ID获取用户的消息
     * @param userId
     * @return
     * @throws RepositoryException
     * @throws JSONException 
     */
    public List<JSONObject> queryWhisperByUserId(int userId) throws RepositoryException, JSONException{
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new PropertyFilter("ToID", FilterOperator.EQUAL, userId));
        filters.add(new PropertyFilter("KeyID", FilterOperator.EQUAL, 0));
        final Query q = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
        final JSONObject result = whisperRepository.get(q);
        final JSONArray results = result.getJSONArray(Keys.RESULTS);
        final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);
        for(JSONObject j:ret){
            j.put("toUser", userRepository.get(j.getString("ToID")));
            j.put("fromUser", userRepository.get(j.getString("FromID")));
            final JSONObject subResult = whisperRepository.get(new Query().setFilter(new PropertyFilter("KeyID", FilterOperator.EQUAL, j.getInt("ID"))));
            j.put("count", subResult.getJSONArray(Keys.RESULTS).length());
        }
        
        return ret;
    }
    /**
     * 根据消息ID获取消息详情
     * @param id
     * @return
     * @throws RepositoryException
     * @throws JSONException 
     */
    public JSONObject queryWhisperById(int id) throws RepositoryException, JSONException{
        final JSONObject result = whisperRepository.get(""+id);
        final JSONObject subResult = whisperRepository.get(new Query().setFilter(new PropertyFilter("KeyID", FilterOperator.EQUAL, id)));
        result.put("toUser", userRepository.get(result.getString("ToID")));
        result.put("fromUser", userRepository.get(result.getString("FromID")));
        final JSONArray results = subResult.getJSONArray(Keys.RESULTS);
        final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);
        result.put("list", ret);
        for(int i=0;i<ret.size();i++){
            JSONObject j = ret.get(i);
            j.put("toUser", userRepository.get(j.getString("ToID")));
            j.put("fromUser", userRepository.get(j.getString("FromID")));
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
     *     "Price": int
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
     *     "Price": int
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

            return true;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "发布需求 [" + demand.toString() + "] 异常", e);

            return false;
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
            query.setCurrentPageNum(pageNum).setPageSize(10);
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
            query.setCurrentPageNum(pageNum).setPageSize(10);
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
            filters.add(new PropertyFilter("CollegeCode", FilterOperator.EQUAL, collegeCode));
            filters.add(new PropertyFilter("ItemType", FilterOperator.EQUAL, type));
            filters.add(new PropertyFilter("DemandOrService", FilterOperator.EQUAL, 1)); // 服务

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(10);
            final JSONObject result = itemRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);

            for (final JSONObject sale : ret) {
                final String userId = sale.getString("MemberID");
                final JSONObject user = userRepository.get(userId);
                sale.put("userName", user.getString("user_name"));
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
            filters.add(new PropertyFilter("CollegeCode", FilterOperator.EQUAL, collegeCode));
            filters.add(new PropertyFilter("ItemType", FilterOperator.EQUAL, type));
            filters.add(new PropertyFilter("DemandOrService", FilterOperator.EQUAL, 0)); // 需求

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(10);
            final JSONObject result = itemRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);

            for (final JSONObject sale : ret) {
                final String userId = sale.getString("MemberID");
                final JSONObject user = userRepository.get(userId);
                sale.put("userName", user.getString("user_name"));
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
            ret.put("userName", user.getString("user_name"));

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
            ret.put("userName", user.getString("user_name"));

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取需求项目 [id=" + id + "] 异常", e);

            return null;
        }
    }
}
