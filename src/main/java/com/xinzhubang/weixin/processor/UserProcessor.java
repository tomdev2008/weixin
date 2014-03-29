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
package com.xinzhubang.weixin.processor;

import com.xinzhubang.weixin.processor.advice.LoginCheck;
import com.xinzhubang.weixin.service.ItemService;
import com.xinzhubang.weixin.service.UserService;
import com.xinzhubang.weixin.util.Filler;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JSONRenderer;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.latke.servlet.renderer.freemarker.FreeMarkerRenderer;
import org.b3log.latke.util.Requests;
import org.b3log.latke.util.Strings;
import org.json.JSONObject;

/**
 * 用户处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.3.2.3, Mar 27, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class UserProcessor {

    @Inject
    private Filler filler;

    @Inject
    private UserService userService;

    @Inject
    private ItemService itemService;

    /**
     * 展示圈子设置页面，设置圈子.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/set-community", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showSetCommunity(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();

        String id = null;
        int type = 0;
        String provinceId = request.getParameter("province_id");
        String schoolId = request.getParameter("school_id");
        String collegId = request.getParameter("college_id");

        if (StringUtils.isNotEmpty(provinceId)) {
            id = provinceId;
            type = 1;
        }

        if (StringUtils.isNotEmpty(schoolId)) {
            id = schoolId;
            type = 2;
        }

        if (StringUtils.isNotEmpty(collegId)) {
            id = collegId;
            type = 3;
        } else {
            collegId = "-1";
        }

        if (type <= 2) {
            context.setRenderer(renderer);
            renderer.setTemplateName("/admin/set-community.ftl");
            final Map<String, Object> dataModel = renderer.getDataModel();
            dataModel.put("provinces", userService.getSchool(id));
            dataModel.put("type", type);
            dataModel.put("provinceId", provinceId);
            dataModel.put("schoolId", schoolId);

            filler.fillHeader(request, response, dataModel);
            filler.fillFooter(dataModel);

            if (2 == type) { // 选择到学校的时候也需要保存
                final JSONObject user = (JSONObject) request.getAttribute("user");
                final String memberId = user.optString("id");

                userService.saveOrUpdateUserInfo(memberId, provinceId, schoolId, collegId);
            }
        } else {
            //保存圈子设置
            final JSONObject user = (JSONObject) request.getAttribute("user");
            final String memberId = user.optString("id");

            userService.saveOrUpdateUserInfo(memberId, provinceId, schoolId, collegId);

            response.sendRedirect("/user-list");
        }

    }

    /**
     * 展示用户列表页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/user-list", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showUserList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/user-list.ftl");

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }
        final int pageNum = Integer.valueOf(pageStr);

        String type = request.getParameter("type"); // teacher/student
        if (Strings.isEmptyOrNull(type)) {
            type = "teacher";
        }

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String userId = user.optString("id");

        final JSONObject community = userService.getUserInfo(userId);
        community.put("type", type);

        final List<JSONObject> userCards = userService.getUserCards(community, pageNum);

        final Map<String, Object> dataModel = renderer.getDataModel();

        final JSONObject currUser = userService.getCurrentUser(request);
        if (null != currUser) {
            dataModel.put("user", currUser);
        }

        for (final JSONObject userCard : userCards) {
            final String uId = userCard.optString("T_User_ID");
            if (null != currUser) {
                userCard.put("isFollow", userService.isFollow(currUser.optString("id"), uId));
            } else {
                userCard.put("isFollow", false);
            }

            userCard.put("Area", community.optString("Area"));
            userCard.put("University", community.optString("University"));
            userCard.put("CollegeCode", community.optString("CollegeCode"));
            userCard.put("College", community.optString("College"));
        }

        dataModel.put("type", type);
        dataModel.put("userCards", userCards);

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 获取社区用户（学生/老师）列表页面数据（AJAX 拉取分页）.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/user-list-ajax", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void getUserList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }
        final int pageNum = Integer.valueOf(pageStr);

        String type = request.getParameter("type"); // teacher/student
        if (Strings.isEmptyOrNull(type)) {
            type = "teacher";
        }

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String userId = user.optString("id");

        final JSONObject community = userService.getUserInfo(userId);
        community.put("type", type);

        final List<JSONObject> userCards = userService.getUserCards(community, pageNum);

        final JSONObject currUser = userService.getCurrentUser(request);
        if (null != currUser) {
            ret.put("user", currUser);
        }

        for (final JSONObject userCard : userCards) {
            final String uId = userCard.optString("T_User_ID");
            if (null != currUser) {
                userCard.put("isFollow", userService.isFollow(currUser.optString("id"), uId));
            } else {
                userCard.put("isFollow", false);
            }

            userCard.put("Area", community.optString("Area"));
            userCard.put("University", community.optString("University"));
            userCard.put("CollegeCode", community.optString("CollegeCode"));
            userCard.put("College", community.optString("College"));
        }

        ret.put("type", type);
        ret.put("pageNum", pageNum);
        ret.put("userCards", userCards);
    }

    /**
     * 展示用户名片页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/user-card", method = HTTPRequestMethod.GET)
    public void showUserCard(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/user-card.ftl");
        final String userName = request.getParameter("userName");
        String type = request.getParameter("type"); // t:老师；s:学生；e：企业
        if (Strings.isEmptyOrNull(type)) {
            type = "teacher";
        }

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);

        final JSONObject user = userService.getUserByName(userName);
        if (null == user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        final String userId = user.getString("id");
        final List<JSONObject> cards = userService.getUserCard(userId, type);
        if (cards.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        final Map<String, Object> dataModel = renderer.getDataModel();

        if ("teacher".equals(type)) { // 我的服务列表
            final List<JSONObject> sales = itemService.getUserSales(userId, pageNum);
            dataModel.put("items", (Object) sales);
        } else { // 我的需求列表
            final List<JSONObject> demands = itemService.getUserDemands(userId, pageNum);
            dataModel.put("items", (Object) demands);
        }

        user.put("cardTitle", cards.get(0).getString("PropertyTitle"));
        user.put("cardRemark", cards.get(0).getString("PropertyRemark"));

        final JSONObject community = userService.getUserInfo(userId);
        user.put("community", community);

        dataModel.put("user", user);

        dataModel.put("isFollow", false);

        final JSONObject currUser = userService.getCurrentUser(request);
        if (null != currUser) {
            dataModel.put("isFollow", userService.isFollow(currUser.optString("id"), userId));
        }

        dataModel.put("type", type);

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 展示用户名片设置页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/user-card", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showUserCardSettings(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/admin/user-card.ftl");

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String memberId = user.optString("id");

        String type = request.getParameter("type");
        if (Strings.isEmptyOrNull(type)) {
            type = "teacher";
        }

        JSONObject userCard = null;
        List<JSONObject> userCards = userService.getUserCard(memberId, type);

        final Map<String, Object> dataModel = renderer.getDataModel();
        if (userCards.isEmpty()) {
            userCard = new JSONObject();

            userCard.put("nickName", "请设置昵称");
            userCard.put("PropertyTitle", "请设置您的个性签名");
            userCard.put("PropertyRemark", "请设置您个人的详细介绍");
        }

        dataModel.put("userCard", userCard);

        dataModel.put("type", type);

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 设置用户名片.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/user-card", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void setUserCard(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        ret.put(Keys.STATUS_CODE, true);
        renderer.setJSONObject(ret);

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);
        final String nickName = requestJSONObject.optString("nickName");
        final String title = requestJSONObject.optString("title");
        final String details = requestJSONObject.optString("details");
        final String type = requestJSONObject.optString("type");

        final JSONObject userCard = new JSONObject();
        userCard.put("nickName", nickName);
        userCard.put("PropertyTitle", title);
        userCard.put("PropertyRemark", details);
        userCard.put("Property", "teacher".equals(type) ? 1 : 0);

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String memberId = user.optString("id");

        userCard.put("T_User_ID", Integer.valueOf(memberId));

        final boolean succ = userService.setUserCard(userCard);

        ret.put(Keys.STATUS_CODE, succ);
        if (!succ) {
            ret.put(Keys.MSG, "更新失败");
        }
    }

    /**
     * 展示我关注的用户列表页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/follow-list", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showMyUserList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/admin/follow-list.ftl");

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);

        final Map<String, Object> dataModel = renderer.getDataModel();

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String memberId = user.optString("id");

        final List<JSONObject> followingUsers = userService.getFollowingUsers(memberId, pageNum);

        dataModel.put("followingUsers", (Object) followingUsers);

        dataModel.put("type", "follow");
        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * AJAX 拉取我关注的人.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/follow-list-ajax", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void getMyUserList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String memberId = user.optString("id");

        final List<JSONObject> followingUsers = userService.getFollowingUsers(memberId, pageNum);

        ret.put("followingUsers", (Object) followingUsers);
        ret.put("pageNum", pageNum);
        ret.put("type", "follow");
    }

    /**
     * 关注用户.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/follow/user", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void followUser(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        ret.put(Keys.STATUS_CODE, true);
        renderer.setJSONObject(ret);

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String memberId = user.optString("id");

        final String attentionMemberId = request.getParameter("id");

        final JSONObject userAttention = new JSONObject();
        userAttention.put("MemberID", Integer.valueOf(memberId));
        userAttention.put("AttentionMemberID", Integer.valueOf(attentionMemberId));

        final boolean succ = userService.addUserAttention(userAttention);

        ret.put(Keys.STATUS_CODE, succ);
        if (!succ) {
            ret.put(Keys.MSG, "发布失败");
        }
    }

    /**
     * 取消关注用户.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/unfollow/user", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void unfollowUser(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        ret.put(Keys.STATUS_CODE, true);
        renderer.setJSONObject(ret);

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String memberId = user.optString("id");

        final String attentionMemberId = request.getParameter("id");

        final boolean succ = userService.removeUserAttention(Integer.valueOf(memberId), Integer.valueOf(attentionMemberId));

        ret.put(Keys.STATUS_CODE, succ);
        if (!succ) {
            ret.put(Keys.MSG, "发布失败");
        }
    }
}
