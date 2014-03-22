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
import org.b3log.latke.util.CollectionUtils;
import org.b3log.latke.util.Requests;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 细节页面相关处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.0, Mar 18, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class MessageProcessor {

    @Inject
    private Filler filler;

    @Inject
    private ItemService itemService;

    /**
     * 展示发送悄悄话页面.  ok
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/whisper", method = HTTPRequestMethod.GET)
    public void showSendWhisper(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/whisper.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        dataModel.put("itemID", request.getParameter("itemID"));
        dataModel.put("toMemberID", request.getParameter("toMemberID"));
        System.out.println(dataModel);
        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 发悄悄话. ok
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/whisper", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void sendWhisper(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
       final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);
        final JSONObject whisper = Requests.parseRequestJSONObject(request, response);
        System.err.println(whisper);
        final JSONObject user = (JSONObject) request.getAttribute("user");
        whisper.put("FromID", user.optInt("id"));
        whisper.put("ToID", whisper.getString("ToID").replace(",", ""));
        final boolean succ = itemService.sendWhisper(whisper);

        ret.put(Keys.STATUS_CODE, succ);
        if (!succ) {
            ret.put(Keys.MSG, "发布失败");
        }

        renderer.setJSONObject(ret);
    }

    /**
     * 展示合作方列表页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/partner-list", method = HTTPRequestMethod.GET)
    public void showPartnerList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/partner-list.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 展示系统推荐页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/pocoer", method = HTTPRequestMethod.GET)
    public void showPocoer(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/pocoer.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 展示我的消息列表页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/message-list", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showMyMessage(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/admin/message-list.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();
        final JSONObject user = (JSONObject) request.getAttribute("user");
        dataModel.put("list", itemService.queryWhisperByUserId(user.optInt("id")));
        dataModel.put("type", "message");
        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 展示我的消息详情
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/message-details", method = HTTPRequestMethod.GET)
    public void showMyMessageDetails(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/admin/message-details.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();
        String id = request.getParameter("id");
        if(StringUtils.isNotEmpty(id)){
            JSONObject message = itemService.queryWhisperById(Integer.parseInt(id));
            JSONArray sublist = message.getJSONArray("list");
            dataModel.put("message",message);
            dataModel.put("list",CollectionUtils.jsonArrayToList(sublist));
        }
        dataModel.put("type", "message");
        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }
}
