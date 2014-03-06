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

import com.xinzhubang.weixin.util.Signs;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.TextHTMLRenderer;

/**
 * 微信公众平台处理器.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 6, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class WeiXinProcessor {

    /**
     * 确认请求来自微信服务器.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/weixin", method = HTTPRequestMethod.GET)
    public void checkSignature(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        // 微信加密签名  
        String signature = request.getParameter("signature");
        // 时间戳  
        String timestamp = request.getParameter("timestamp");
        // 随机数  
        String nonce = request.getParameter("nonce");
        // 随机字符串  
        String echostr = request.getParameter("echostr");

        final TextHTMLRenderer renderer = new TextHTMLRenderer();
        context.setRenderer(renderer);
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败  
        if (Signs.checkSignature(signature, timestamp, nonce)) {
            renderer.setContent(echostr);
        } else {
            renderer.setContent("");
        }
    }
    
    /**
     * 处理微信服务器发来的消息.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/weixin", method = HTTPRequestMethod.POST)
    public void process(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        // TODO: 处理微信服务器发来的消息
    }
}
