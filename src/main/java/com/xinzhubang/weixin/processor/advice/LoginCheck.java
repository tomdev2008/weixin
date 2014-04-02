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
package com.xinzhubang.weixin.processor.advice;

import com.xinzhubang.weixin.service.UserService;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.logging.Level;
import org.b3log.latke.model.User;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.advice.BeforeRequestProcessAdvice;
import org.b3log.latke.servlet.advice.RequestProcessAdviceException;
import org.b3log.latke.util.Strings;
import org.json.JSONObject;

/**
 * 登录检查，如果登录了，则把当前登录用户放到请求对象的属性里，属性名 "user".
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.0, Apr 2, 2014
 * @since 1.0.0
 */
@Named
@Singleton
public class LoginCheck extends BeforeRequestProcessAdvice {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LoginCheck.class.getName());

    @Inject
    private UserService userService;

    @Override
    public void doAdvice(final HTTPRequestContext context, final Map<String, Object> args) throws RequestProcessAdviceException {
        final HttpServletRequest request = context.getRequest();

        final JSONObject exception403 = new JSONObject();
        exception403.put(Keys.MSG, HttpServletResponse.SC_FORBIDDEN);
        exception403.put(Keys.STATUS_CODE, HttpServletResponse.SC_FORBIDDEN);
        
        final JSONObject exception412 = new JSONObject();
        exception412.put(Keys.MSG, HttpServletResponse.SC_PRECONDITION_FAILED);
        exception412.put(Keys.STATUS_CODE, HttpServletResponse.SC_PRECONDITION_FAILED);

        try {
            // 未登录：403
            JSONObject currentUser = userService.getCurrentUser(request);
            if (null == currentUser && !userService.tryLogInWithCookie(request, context.getResponse())) {
                throw new RequestProcessAdviceException(exception403);
            }
            
            currentUser = userService.getCurrentUser(request);

            request.setAttribute(User.USER, currentUser);
            
            // 未设置圈子：412
            final String requestURI = request.getRequestURI();
            if ("/admin/set-community".equals(requestURI) ||
                "/admin/user-card".equals(requestURI)) {
                return;
            }
            
            final JSONObject community = userService.getUserInfo(currentUser.optString("id"));
            if (Strings.isEmptyOrNull(community.optString("Area"))) {
                throw new RequestProcessAdviceException(exception412);
            }
            
        } catch (final ServiceException e) {
            LOGGER.log(Level.ERROR, "登录检查异常");

            throw new RequestProcessAdviceException(exception403);
        }
    }
}
