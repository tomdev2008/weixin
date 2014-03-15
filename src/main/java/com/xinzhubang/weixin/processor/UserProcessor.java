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

import com.xinzhubang.weixin.service.UserService;
import com.xinzhubang.weixin.util.Filler;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.latke.servlet.renderer.freemarker.FreeMarkerRenderer;
import org.json.JSONObject;

/**
 * 用户处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.1, Mar 6, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class UserProcessor {

    @Inject
    private Filler filler;

    @Inject
    private UserService userService;

    /**
     * 展示用户列表页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/user-list", method = HTTPRequestMethod.GET)
    public void showUserList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/user-list.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModel.put("type", "student");

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
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
        final String type = request.getParameter("type"); // t:老师；s:学生；e：企业

        final JSONObject user = userService.getUserByName(userName);
        if (null == user) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        final int userId = user.getInt("id");
        final JSONObject card = userService.getUserCard(userId, type);
        if (null == card) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModel.put("userName", user.getString("user_name"));
        dataModel.put("cardTitle", card.getString("PropertyTitle"));

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
    @RequestProcessing(value = "/user-card-settings", method = HTTPRequestMethod.GET)
    public void showUserCardSettings(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/user-card.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }
}
