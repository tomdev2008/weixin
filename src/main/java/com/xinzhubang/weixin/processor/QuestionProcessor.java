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
import org.json.JSONObject;

/**
 * 问题处理器.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @version 1.0.0.0, Mar 5, 2014
 * @since 1.0.0
 */
@RequestProcessor
public class QuestionProcessor {

    @Inject
    private Filler filler;

    @Inject
    private QuestionService questionService;

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
    public void showMyIndex(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/question-list.ftl");
        Map<String, Object> dataModel = renderer.getDataModel();
        //获取用户session,放入用户id
        final JSONObject user = (JSONObject) request.getAttribute("user");
        dataModel.put("questionList", (Object) questionService.questionList(user.optInt("id"), 0));
        dataModel.put("type", "question");
        dataModel.put("subType", "1");

        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
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
    public void showIndex(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/question-list.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();
        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);
        String type = "1";
        if (request.getParameter("type") != null) {
            type = request.getParameter("type");
            if (type.equals("1")) {//最新

            } else if (type.equals("2")) {//已解决

            } else if (type.equals("3")) {//未解决

            }
        }
        dataModel.put("questionList", (Object) questionService.questionList(0, 0));
        dataModel.put("type", "question");
        dataModel.put("subType", type);
        filler.fillHeader(request, response, dataModel);
        filler.fillFooter(dataModel);
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
    public void showDetails(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final AbstractFreeMarkerRenderer renderer = new FreeMarkerRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("/community/question-details.ftl");

        final Map<String, Object> dataModel = renderer.getDataModel();
        JSONObject question = questionService.getById(request.getParameter("id"));
        List<JSONObject> answers = questionService.queryAnswerByQuestionId(question.getInt("id"));
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
     * 发布问题提交保存
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/question-publish", method = HTTPRequestMethod.POST)
    @Before(adviceClass = LoginCheck.class)
    public void savePublish(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);
        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, response);
        final JSONObject user = (JSONObject) request.getAttribute("user");
        requestJSONObject.put("AddUserID", user.optInt("id"));
        requestJSONObject.put("Title", requestJSONObject.get("Content"));
        requestJSONObject.put("AddTime", new Timestamp(System.currentTimeMillis()));
        String id = questionService.add(requestJSONObject);
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
     * 保存回答
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
     * 采纳答案
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
        questionService.acceptAnswer(requestJSONObject.getInt("id"));
        ret.put(Keys.STATUS_CODE, true);
        ret.put(Keys.MSG, "采纳成功！");
        ret.put("qid", requestJSONObject.getInt("qid"));
    }
}
