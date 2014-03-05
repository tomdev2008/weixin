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
package com.xinzhubang.weixin.util;

import java.util.Calendar;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.Latkes;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.annotation.Service;

/**
 * Filler utilities.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 5, 2014
 * @since 1.0.0
 */
@Service
public class Filler {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(Filler.class.getName());
    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Fills header.
     *
     * @param request the specified request
     * @param response the specified response
     * @param dataModel the specified data model
     * @throws Exception exception
     */
    public void fillHeader(final HttpServletRequest request, final HttpServletResponse response,
                           final Map<String, Object> dataModel) throws Exception {
        fillMinified(dataModel);
        dataModel.put("staticResourceVersion", Latkes.getStaticResourceVersion());

        fillLangs(dataModel);
    }

    /**
     * Fills footer.
     *
     * @param dataModel the specified data model
     * @throws Exception exception
     */
    public void fillFooter(final Map<String, Object> dataModel) throws Exception {
        fillSysInfo(dataModel);

        dataModel.put("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    }

    /**
     * Fills minified directory and file postfix for static JavaScript, CSS.
     *
     * @param dataModel the specified data model
     */
    private void fillMinified(final Map<String, Object> dataModel) {
        switch (Latkes.getRuntimeMode()) {
            case DEVELOPMENT:
                dataModel.put("miniPostfix", "");
                break;
            case PRODUCTION:
                dataModel.put("miniPostfix", ".min");
                break;
            default:
                throw new AssertionError();
        }
    }

    /**
     * Fills the all language labels.
     *
     * @param dataModel the specified data model
     */
    private void fillLangs(final Map<String, Object> dataModel) {
        dataModel.putAll(langPropsService.getAll(Latkes.getLocale()));
    }

    /**
     * Fills system info.
     *
     * @param dataModel the specified data model
     * @throws Exception exception
     */
    private void fillSysInfo(final Map<String, Object> dataModel) throws Exception {
        dataModel.put("version", "1.0.0");
    }
}
