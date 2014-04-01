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
 * 出售处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.5.1, Mar 29, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class SaleProcessor {

    @Inject
    private Filler filler;

    @Inject
    private ItemService itemService;

    @Inject
    private UserService userService;

    /**
     * 展示个人中心-我的信息-我的服务页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/sale-list", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showMySaleList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/admin/sale-list.ftl");

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);
        final JSONObject user = (JSONObject) request.getAttribute("user");

        final Map<String, Object> dataModel = renderer.getDataModel();

        final List<JSONObject> list = itemService.getUserSales(user.optString("id"), pageNum);

        dataModel.put("sales", (Object) list);
        dataModel.put("pageNum", pageNum);
        dataModel.put("type", "sale");

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 获取个人中心-我的信息-我的服务数据（AJAX 拉取分页）.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/sale-list-ajax", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void getMySaleList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
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

        final List<JSONObject> list = itemService.getUserSales(user.optString("id"), pageNum);

        ret.put("sales", (Object) list);
        ret.put("pageNum", pageNum);
        ret.put("type", "sale");
    }

    /**
     * 展示社区圈子出售页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/sale-list", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showCommunitySaleList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/sale-list.ftl");

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
        final List<JSONObject> list = itemService.getSales(community, pageNum);

        dataModel.put("sales", (Object) list);
        dataModel.put("pageNum", pageNum);
        dataModel.put("type", "sale");
        dataModel.put("subType", typeStr);

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 获取出售页面数据（AJAX 拉取分页）.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/sale-list-ajax", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void getSaleList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
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
        final List<JSONObject> list = itemService.getSales(community, pageNum);

        ret.put("sales", (Object) list);
        ret.put("pageNum", pageNum);
    }

    /**
     * 展示出售细节页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/sale-details", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showDetails(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/sale-details.ftl");

        final String id = request.getParameter("id");

        final Map<String, Object> dataModel = renderer.getDataModel();

        final JSONObject sale = itemService.getSale(id);
        dataModel.put("sale", sale);
        dataModel.put("type", "sale");

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 展示发布出售页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/sale-publish", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showPublish(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/sale-publish.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModel.put("type", "sale");

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 发布出售.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/sale-publish", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void publishSale(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
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

        final JSONObject sale = new JSONObject();

        final JSONObject user = (JSONObject) request.getAttribute("user");

        sale.put("Name", name);
        sale.put("ItemType", type);
        sale.put("MemberID", user.optInt("id"));
        sale.put("ItemContent", desc);
        sale.put("Price", price);

        final String userId = user.optString("id");

        final JSONObject community = userService.getUserInfo(userId);

        sale.put("Area", community.optString("Area"));
        sale.put("AreaCode", community.optString("AreaCode"));
        sale.put("University", community.optString("University"));
        sale.put("UniversityCode", community.optString("UniversityCode"));
        sale.put("College", community.optString("College"));
        sale.put("CollegeCode", community.optString("CollegeCode"));

        final boolean succ = itemService.publishSale(sale);
        ret.put(Keys.STATUS_CODE, succ);
        if (!succ) {
            ret.put(Keys.MSG, "发布失败");

            return;
        }

        final JSONObject latestItem = itemService.getLatestItem(userId);
        ret.put("id", latestItem.optString("ID"));
    }
}
