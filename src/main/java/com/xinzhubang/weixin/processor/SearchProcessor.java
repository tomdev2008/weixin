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

import com.xinzhubang.weixin.service.MajorService;
import com.xinzhubang.weixin.util.Filler;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.latke.servlet.renderer.freemarker.FreeMarkerRenderer;

/**
 * 搜索处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.1.0, Mar 29, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class SearchProcessor {

    @Inject
    private Filler filler;

    @Inject
    private MajorService majorService;

    /**
     * 展示首页.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/search", method = HTTPRequestMethod.GET)
    public void showIndex(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/search/course.ftl");

        String id = request.getParameter("id");
        String typeS = request.getParameter("type");

        int type = 0;
        if (StringUtils.isNotEmpty(typeS)) {
            type = Integer.parseInt(typeS);
        }

        final Map<String, Object> dataModel = renderer.getDataModel();

        dataModel.put("list", majorService.seach(id, type));
        dataModel.put("type", ++type);

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }
}
