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

/**
 * 细节页面相关处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @version 1.0.0.0, Mar 5, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class MessageProcessor {
    
    @Inject
    private Filler filler;

    /**
     * 展示发送悄悄话页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/whisper", method = HTTPRequestMethod.GET)
    public void showIndex(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/whisper.ftl");
        
        final Map<String, Object> dataModel = renderer.getDataModel();

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
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
    public void showMyMessage(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/admin/message-list.ftl");
        
        final Map<String, Object> dataModel = renderer.getDataModel();
        
        dataModel.put("type", "message");
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
    @RequestProcessing(value = "/admin/message-details", method = HTTPRequestMethod.GET)
    public void showMyMessageDetails(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/admin/message-details.ftl");
        
        final Map<String, Object> dataModel = renderer.getDataModel();
        
        dataModel.put("type", "message");
        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }
}
