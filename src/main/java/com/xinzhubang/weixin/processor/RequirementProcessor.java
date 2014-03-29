package com.xinzhubang.weixin.processor;

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
import com.xinzhubang.weixin.processor.advice.LoginCheck;
import com.xinzhubang.weixin.service.ItemService;
import com.xinzhubang.weixin.service.UserService;
import com.xinzhubang.weixin.util.Filler;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * 需求处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.2.0, Mar 29, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class RequirementProcessor {

    @Inject
    private Filler filler;

    @Inject
    private ItemService itemService;

    @Inject
    private UserService userService;

    /**
     * 展示个人中心-我的信息-我的需求页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/requirement-list", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showMyRequirementList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/admin/requirement-list.ftl");

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);
        final JSONObject user = (JSONObject) request.getAttribute("user");

        final Map<String, Object> dataModel = renderer.getDataModel();

        final List<JSONObject> list = itemService.getUserDemands(user.optString("id"), pageNum);

        dataModel.put("requirements", (Object) list);
        dataModel.put("pageNum", pageNum);

        dataModel.put("type", "requirement");
        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 获取个人中心-我的信息-我的需求数据（AJAX 拉取分页）.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/requirement-list-ajax", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void getMyRequirementList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
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

        final List<JSONObject> list = itemService.getUserDemands(user.optString("id"), pageNum);

        ret.put("requirements", (Object) list);
        ret.put("pageNum", pageNum);
        ret.put("type", "requirement");
    }

    /**
     * 展示社区圈子需求列表页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/requirement-list", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showCommunityRequirementList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/requirement-list.ftl");

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        String typeStr = request.getParameter("type");
        if (Strings.isEmptyOrNull(typeStr)) {
            typeStr = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);
        final int type = Integer.valueOf(typeStr);

        final Map<String, Object> dataModel = renderer.getDataModel();

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String userId = user.optString("id");

        final JSONObject community = userService.getUserInfo(userId);
        community.put("type", type);
        final List<JSONObject> list = itemService.getDemands(community, pageNum);

        dataModel.put("requirements", (Object) list);
        dataModel.put("pageNum", pageNum);
        dataModel.put("type", "requirement");
        dataModel.put("subType", typeStr);

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 获取需求页面数据（AJAX 拉取分页）.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/requirement-list-ajax", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void getRequirementList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        String typeStr = request.getParameter("type");
        if (Strings.isEmptyOrNull(typeStr)) {
            typeStr = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);
        final int type = Integer.valueOf(typeStr);

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String userId = user.optString("id");

        final JSONObject community = userService.getUserInfo(userId);

        community.put("type", type);
        final List<JSONObject> list = itemService.getDemands(community, pageNum);

        ret.put("requirements", (Object) list);
        ret.put("pageNum", pageNum);
    }

    /**
     * 展示需求细节页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/requirement-details", method = HTTPRequestMethod.GET)
    public void showDetails(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/requirement-details.ftl");

        final String id = request.getParameter("id");

        final Map<String, Object> dataModel = renderer.getDataModel();

        final JSONObject requirement = itemService.getDemand(id);
        dataModel.put("requirement", requirement);

        dataModel.put("type", "requirement");
        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 展示需求发布页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/requirement-publish", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showPublish(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/requirement-publish.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModel.put("type", "requirement");

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 发布需求.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/requirement-publish", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void publishRequirement(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);

        final String name = requestJSONObject.getString("name");
        final int type = requestJSONObject.getInt("type");
        final String desc = requestJSONObject.getString("desc");
        final int price = requestJSONObject.getInt("price");

        final JSONObject requirement = new JSONObject();

        final JSONObject user = (JSONObject) request.getAttribute("user");

        requirement.put("Name", name);
        requirement.put("ItemType", type);
        requirement.put("MemberID", user.optInt("id"));
        requirement.put("ItemContent", desc);
        requirement.put("Price", price);

        final String userId = user.optString("id");

        final JSONObject community = userService.getUserInfo(userId);

        requirement.put("Area", community.optString("Area"));
        requirement.put("AreaCode", community.optString("AreaCode"));
        requirement.put("University", community.optString("University"));
        requirement.put("UniversityCode", community.optString("UniversityCode"));
        requirement.put("College", community.optString("College"));
        requirement.put("CollegeCode", community.optString("CollegeCode"));

        final boolean succ = itemService.publishDemand(requirement);
        ret.put(Keys.STATUS_CODE, succ);
        if (!succ) {
            ret.put(Keys.MSG, "发布失败");

            return;
        }

        final JSONObject latestItem = itemService.getLatestItem(userId);
        ret.put("id", latestItem.optString("ID"));
    }
}
