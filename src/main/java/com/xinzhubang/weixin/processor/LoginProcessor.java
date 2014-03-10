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

import com.xinzhubang.weixin.repository.UserRepository;
import com.xinzhubang.weixin.util.DESs;
import com.xinzhubang.weixin.util.Filler;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JSONRenderer;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.latke.servlet.renderer.freemarker.FreeMarkerRenderer;
import org.json.JSONObject;

/**
 * 登录、注册处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 6, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class LoginProcessor {

    @Inject
    private Filler filler;
    
    @Inject
    private UserRepository userRepository;

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

        final Map<String, Object> dataModel = renderer.getDataModel();

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
        renderer.setJSONObject(ret);
        
        final String userName = request.getParameter("userName");
        final String password = request.getParameter("password");
        
        final String passwordEncrypted = DESs.encrypt(password, userName);
        
        

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
}
