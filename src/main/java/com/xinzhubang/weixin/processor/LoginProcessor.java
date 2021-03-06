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
import com.xinzhubang.weixin.util.DESs;
import com.xinzhubang.weixin.util.Filler;
import com.xinzhubang.weixin.util.Sessions;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.Keys;
import org.b3log.latke.mail.MailService;
import org.b3log.latke.mail.MailServiceFactory;
import org.b3log.latke.model.User;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JSONRenderer;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.latke.servlet.renderer.freemarker.FreeMarkerRenderer;
import org.b3log.latke.util.Requests;
import org.b3log.latke.util.Strings;
import org.json.JSONObject;

/**
 * 登录、注册处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.4.4.0, Apr 4, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class LoginProcessor {

    @Inject
    private Filler filler;

    @Inject
    private UserService userService;

    private final MailService mailService = MailServiceFactory.getMailService();

    /**
     * 展示登录页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/login", method = HTTPRequestMethod.GET)
    public void showLogin(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/login.ftl");

        String go = request.getParameter("go");
        if (Strings.isEmptyOrNull(go)) {
            go = "/user-list";
        }

        final Map<String, Object> dataModel = renderer.getDataModel();

        dataModel.put("go", go);

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 用户登录.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/login", method = HTTPRequestMethod.POST)
    public void login(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        ret.put(Keys.STATUS_CODE, true);
        renderer.setJSONObject(ret);

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);
        final String userName = requestJSONObject.optString("userName");
        final String password = requestJSONObject.optString("password");
        final String go = requestJSONObject.optString("go", "/user-list");
        final String cardType = requestJSONObject.getString("cardType");

        final JSONObject user = userService.getUserByName(userName);
        if (null == user || !user.optString("password").equals(DESs.encrypt(password, "XHJY"))) {
            ret.put(Keys.STATUS_CODE, false);
            ret.put(Keys.MSG, "用户不存在或密码错误");

            return;
        }

        final String userId = user.optString("id");

        user.put(User.USER_EMAIL, user.getString("email"));
        user.put(User.USER_PASSWORD, user.getString("password"));
        user.put("userId", userId);

        Sessions.login(request, response, user);

        // 登录日志
        userService.addLoginLog(userName, userId, Requests.getRemoteAddr(request));

        // 查询用户是否设置过选择的名片
        final List<JSONObject> userCards = userService.getUserCard(userId, cardType);

        if (userCards.isEmpty()) { // 没有设置过选择的名片
            // 默认创建
            final JSONObject userCard = new JSONObject();
            userCard.put("nickName", userName);
            userCard.put("PropertyTitle", "");
            userCard.put("PropertyRemark", "");
            userCard.put("Property", "teacher".equals(cardType) ? 1 : 0);

            userCard.put("T_User_ID", Integer.valueOf(userId));

            userService.setUserCard(userCard);
        }

        if ("/admin/user-card".equals(go)) {
            ret.put("go", go + "?type=" + cardType);

            return;
        }

        // 查询用户是否已经设置过圈子
        final JSONObject community = userService.getUserInfo(userId);

        if (Strings.isEmptyOrNull(community.optString("Area"))) { // 如果用户还没有设置过圈子
            ret.put("go", "/admin/set-community");
        } else {
            ret.put("go", go);
        }
    }

    /**
     * 展示忘记密码页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/forget-password", method = HTTPRequestMethod.GET)
    public void showForgetPwd(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/forget-password.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 忘记密码.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/forget-password", method = HTTPRequestMethod.POST)
    public void forgetPwd(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        ret.put(Keys.STATUS_CODE, true);
        renderer.setJSONObject(ret);

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);
        final String email = requestJSONObject.getString("email");

        final MailService.Message message = new MailService.Message();
        message.addRecipient(email);
        message.setSubject("新助邦 - 找回密码");
        message.setHtmlBody("邮件发送测试");

        // mailService.send(message);
    }

    /**
     * 展示注册页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/register", method = HTTPRequestMethod.GET)
    public void showRegister(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/register.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 用户注册.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/register", method = HTTPRequestMethod.POST)
    public void register(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        ret.put(Keys.STATUS_CODE, true);
        renderer.setJSONObject(ret);

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);
        final String email = requestJSONObject.getString("email");

        if (!Strings.isEmail(email)) {
            ret.put(Keys.STATUS_CODE, false);
            ret.put(Keys.MSG, "邮箱格式错误！");

            return;
        }

        final String userName = requestJSONObject.getString("user_name");
        final String password = requestJSONObject.getString("password");

        final JSONObject user = userService.getUserByEmailOrUsername(email, userName);
        if (user != null) {
            ret.put(Keys.STATUS_CODE, false);
            ret.put(Keys.MSG, "邮箱或用户名已存在，请重新输入！");

            return;
        }

        requestJSONObject.put("password", DESs.encrypt(password, "XHJY"));

        userService.addUser(requestJSONObject);
        ret.put(Keys.STATUS_CODE, true);
        ret.put(Keys.MSG, "注册成功，请登录！");
    }
}
