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
import com.xinzhubang.weixin.service.QuestionService;
import com.xinzhubang.weixin.service.UserService;
import com.xinzhubang.weixin.util.Filler;
import java.sql.Timestamp;
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
 * 问题处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.1.0, Mar 25, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class QuestionProcessor {

    @Inject
    private Filler filler;

    @Inject
    private QuestionService questionService;

    @Inject
    private UserService userService;

    /**
     * 展示我的问题列表页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/question-list", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showMyQuestionList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/admin/question-list.ftl");

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);

        final Map<String, Object> dataModel = renderer.getDataModel();

        final JSONObject user = (JSONObject) request.getAttribute("user");

        dataModel.put("questions", (Object) questionService.getUserQuestions(user.optString("id"), pageNum));
        dataModel.put("type", "question");
        dataModel.put("subType", "1");

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 获取个人中心-我的信息-我的问题数据（AJAX 拉取分页）.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/admin/question-list-ajax", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void getMyQuestionList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
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

        final List<JSONObject> list = questionService.getUserQuestions(user.optString("id"), pageNum);

        ret.put("questions", (Object) list);
        ret.put("pageNum", pageNum);
        ret.put("type", "question");
        ret.put("subType", "1");
    }

    /**
     * 展示问题列表页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/question-list", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showCommunityQuestionList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/question-list.ftl");

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);

        final Map<String, Object> dataModel = renderer.getDataModel();

        String type = request.getParameter("type");
        if (Strings.isEmptyOrNull(type)) {
            // 默认设置 type 为最新
            type = "1";
        }

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String userId = user.optString("id");

        final JSONObject community = userService.getUserInfo(userId);
        community.put("type", type);
        dataModel.put("questions", (Object) questionService.getQuestions(community, pageNum));
        dataModel.put("type", "question");
        dataModel.put("subType", type);

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 获取社区问题列表页面数据（AJAX 拉取分页）.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/question-list-ajax", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void getQuestionList(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);

        String pageStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageStr)) {
            pageStr = "1";
        }

        String type = request.getParameter("type");
        if (Strings.isEmptyOrNull(type)) {
            // 默认设置 type 为最新
            type = "1";
        }

        final int pageNum = Integer.valueOf(pageStr);

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String userId = user.optString("id");

        final JSONObject community = userService.getUserInfo(userId);
        community.put("type", type);
        final List<JSONObject> list = questionService.getQuestions(community, pageNum);

        ret.put("questions", (Object) list);
        ret.put("pageNum", pageNum);
        ret.put("subType", type);
    }

    /**
     * 展示问题细节页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/question-details", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showDetails(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/question-details.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        final JSONObject question = questionService.getById(request.getParameter("id"));
        final List<JSONObject> answers = questionService.getAnswerByQuestionId(question.getInt("ID"));

        question.put("count", answers.size());
        dataModel.put("question", question);
        dataModel.put("answers", answers);
        dataModel.put("type", "question");
        dataModel.put("subType", "1");

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 展示发布问题页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/question-publish", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showPublish(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/question-publish.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        dataModel.put("type", "question");
        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 发布问题.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/question-publish", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void publishQuestion(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);
        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);

        final JSONObject user = (JSONObject) request.getAttribute("user");
        final String userId = user.optString("id");

        final JSONObject community = userService.getUserInfo(userId);

        requestJSONObject.put("AddUserID", user.optInt("id"));
        requestJSONObject.put("Title", requestJSONObject.get("Content"));

        // 处理关键字
        String keywords = requestJSONObject.optString("Keywords");
        final StringBuilder keywordsBuilder = new StringBuilder("|");

        final String[] parts = keywords.split(","); // 先尝试用 ',' 分割
        for (final String part : parts) {
            final String[] parts1 = part.split(" "); // 再尝试用 ' ' 分割

            for (final String part1 : parts1) {
                final String keyword = part1.trim();

                if (!Strings.isEmptyOrNull(keyword)) {
                    keywordsBuilder.append(keyword);
                    keywordsBuilder.append('|');
                }
            }
        }
        
        requestJSONObject.put("Keywords", keywordsBuilder.toString());

        requestJSONObject.put("AddTime", new Timestamp(System.currentTimeMillis()));

        requestJSONObject.put("Area", community.optString("Area"));
        requestJSONObject.put("AreaCode", community.optString("AreaCode"));
        requestJSONObject.put("University", community.optString("University"));
        requestJSONObject.put("UniversityCode", community.optString("UniversityCode"));
        requestJSONObject.put("College", community.optString("College"));
        requestJSONObject.put("CollegeCode", community.optString("CollegeCode"));

        questionService.addQuestion(requestJSONObject);

        ret.put(Keys.STATUS_CODE, true);
        ret.put(Keys.MSG, "提问保存成功！");
    }

    /**
     * 展示回复问题页面.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/question-answer", method = HTTPRequestMethod.GET)
    @Before(adviceClass = LoginCheck.class)
    public void showAnswer(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/question-answer.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();

        dataModel.put("type", "question");
        dataModel.put("id", request.getParameter("id"));

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
    }

    /**
     * 保存回答.
     *
     * @param context
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestProcessing(value = "/question-answer", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void saveAnswer(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);

        final JSONObject user = (JSONObject) request.getAttribute("user");
        requestJSONObject.put("AddUserID", user.optInt("id"));
        requestJSONObject.put("AddTime", new Timestamp(System.currentTimeMillis()));

        questionService.addAnswer(requestJSONObject);

        ret.put(Keys.STATUS_CODE, true);
        ret.put(Keys.MSG, "回答成功！");
        ret.put("id", requestJSONObject.get("QID"));
    }

    /**
     * 采纳答案.
     *
     * @param context
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestProcessing(value = "/accept", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void acceptAnswer(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);

        questionService.acceptAnswer(requestJSONObject.getString("id"));

        ret.put(Keys.STATUS_CODE, true);
        ret.put(Keys.MSG, "采纳成功！");
        ret.put("qid", requestJSONObject.getInt("qid"));
    }
}
