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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xinzhubang.weixin.service;

import com.xinzhubang.weixin.XZBServletListener;
import com.xinzhubang.weixin.repository.AnswerRepository;
import com.xinzhubang.weixin.repository.QuestionRepository;
import com.xinzhubang.weixin.repository.UserRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.CompositeFilter;
import org.b3log.latke.repository.CompositeFilterOperator;
import org.b3log.latke.repository.Filter;
import org.b3log.latke.repository.FilterOperator;
import org.b3log.latke.repository.PropertyFilter;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.SortDirection;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户提问。
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.5.0, Apr 2, 2014
 * @since 1.0.0
 */
@Service
public class QuestionService {

    private static final Logger LOGGER = Logger.getLogger(QuestionService.class);

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AnswerRepository answerRepository;

    @Inject
    private NoticeService noticeService;

    /**
     * 添加提问
     *
     * @param question
     * @return
     */
    @Transactional
    public String addQuestion(final JSONObject question) {
        String ret;

        try {
            ret = questionRepository.add(question);
        } catch (RepositoryException ex) {
            LOGGER.log(Level.ERROR, "保存提问出错！", ex);
            return null;
        }

        return ret;
    }

    /**
     * 添加回答.
     *
     * @param answer
     * @return
     */
    @Transactional
    public boolean addAnswer(final JSONObject answer) {
        try {
            // 1. 添加回答
            answer.put("Agree", 0);

            answerRepository.add(answer);

            // 2. 更新问题的最近回答时间
            final int questionId = answer.optInt("QID");
            final JSONObject question = questionRepository.get(questionId + "");
            question.put("LastAnswerTime", new Timestamp(System.currentTimeMillis()));

            questionRepository.update(questionId + "", question);

            // 3. 添加一条通知
            final JSONObject notice = new JSONObject();
            final int memberId = answer.optInt("AddUserID");
            final int reviceId = question.optInt("AddUserID");
            final String content = "您的提问 \"" + question.optString("Title") + "\" 收到了一条回答";

            notice.put("MemberID", memberId);
            notice.put("ReviceID", reviceId);
            notice.put("NoticeContent", content);
            notice.put("IsRead", 0); // 默认已读
            notice.put("MsgType", 20);
            notice.put("CorrID", questionId);

            noticeService.addNotice(notice);
        } catch (RepositoryException ex) {
            LOGGER.log(Level.ERROR, "保存回答出错！", ex);

            return false;
        }

        return true;
    }

    /**
     * 采纳回答.
     *
     * @param answerId 指定的回答 id
     */
    @Transactional
    public void acceptAnswer(final String answerId) {
        try {
            // 1. 更新回答：为答案
            JSONObject answer = answerRepository.get(answerId);

            final int questionId = answer.getInt("QID");

            answer.put("Best", 1); // 最佳答案

            answerRepository.update(answerId, answer);

            // 2. 更新问题：关联答案
            final JSONObject question = questionRepository.get(questionId + "");
            question.put("BestAnswer", Long.valueOf(answerId));
            question.put("BestAnswerTime", new Timestamp(System.currentTimeMillis()));

            questionRepository.update(questionId + "", question);

            // 3. 添加一条通知
            final JSONObject notice = new JSONObject();
            final int answerUserId = answer.optInt("AddUserID");
            final int questonUserId = question.optInt("AddUserID");
            final String content = "您对提问 \"" + question.optString("Title") + "\" 的回答已被采纳";

            notice.put("MemberID", questonUserId);
            notice.put("ReviceID", answerUserId);
            notice.put("NoticeContent", content);
            notice.put("IsRead", 0); // 默认已读
            notice.put("MsgType", 20);
            notice.put("CorrID", questionId);

            noticeService.addNotice(notice);
        } catch (final Exception ex) {
            LOGGER.log(Level.ERROR, "保存回答出错！", ex);
        }
    }

    /**
     * 获取指定的社区圈子中的提问列表.
     *
     * @param community 指定的社区圈子，例如：
     * <pre>
     * {
     *     "AreaCode": "",
     *     "UniversityCode": "",
     *     "CollegeCode": "", // 可选的
     *     "type": "" // "1"：最新，"2"：已解决，"3"：待解决
     * }
     * </pre>
     *
     * @param pageNum
     * @return
     */
    public List<JSONObject> getQuestions(final JSONObject community, final int pageNum) {
        try {
            final String areaCode = community.getString("AreaCode");
            final String universityCode = community.getString("UniversityCode");
            final String collegeCode = community.optString("CollegeCode", "-1");

            final List<Filter> filters = new ArrayList<Filter>();
            filters.add(new PropertyFilter("AreaCode", FilterOperator.EQUAL, areaCode));
            filters.add(new PropertyFilter("UniversityCode", FilterOperator.EQUAL, universityCode));

            if (!"-1".equals(collegeCode)) {
                filters.add(new PropertyFilter("CollegeCode", FilterOperator.EQUAL, collegeCode));
            }

            final String type = community.optString("type");
            if ("1".equals(type)) { // 最新
                // 最新就是后面按时间排序
            } else if ("2".equals(type)) { // 已解决
                filters.add(new PropertyFilter("BestAnswer", FilterOperator.NOT_EQUAL, 0));
            } else { // 待解决
                filters.add(new PropertyFilter("BestAnswer", FilterOperator.EQUAL, 0));
            }

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);

            query.addSort("LastAnswerTime", SortDirection.DESCENDING).addSort("AddTime", SortDirection.DESCENDING);

            final JSONObject result = questionRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);

            for (final JSONObject question : ret) {
                question.put("user", userRepository.get(question.getString("AddUserID")));
                final JSONObject subResult = answerRepository.get(new Query().setFilter(
                        new PropertyFilter("QID", FilterOperator.EQUAL, question.getInt("ID"))));
                question.put("count", subResult.getJSONArray(Keys.RESULTS).length());
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取社区圈子提问列表异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 获取指定用户的问题列表.
     *
     * @param userId
     * @param pageNum
     * @return
     */
    public List<JSONObject> getUserQuestions(final String userId, final int pageNum) {
        try {
            final Query query = new Query();
            query.setFilter(new PropertyFilter("AddUserID", FilterOperator.EQUAL, userId));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);

            final JSONObject result = questionRepository.get(query);
            final JSONArray results = result.getJSONArray(Keys.RESULTS);

            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);
            for (final JSONObject question : ret) {
                question.put("user", userRepository.get(question.getString("AddUserID")));
                final JSONObject subResult = answerRepository.get(new Query().setFilter(
                        new PropertyFilter("QID", FilterOperator.EQUAL, question.getInt("ID"))));
                question.put("count", subResult.getJSONArray(Keys.RESULTS).length());
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取用户 [id=" + userId + "] 提问列表异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 根据ID获取
     *
     * @param id
     * @return
     * @throws RepositoryException
     * @throws org.json.JSONException
     */
    public JSONObject getById(String id) throws RepositoryException, JSONException {
        JSONObject ret = questionRepository.get(id);

        ret.put("user", userRepository.get(ret.getString("AddUserID")));

        return ret;
    }

    /**
     * 获取指定 id 问题的答案列表.
     *
     * @param id 指定 id 问题
     * @return
     * @throws Exception
     */
    public List<JSONObject> getAnswerByQuestionId(final String id) throws Exception {
        final Query query = new Query().setFilter(new PropertyFilter("QID", FilterOperator.EQUAL, id));
        query.addSort("Best", SortDirection.DESCENDING).addSort("AddTime", SortDirection.DESCENDING);

        JSONObject answers = answerRepository.get(query);

        final JSONArray answersArray = answers.getJSONArray(Keys.RESULTS);
        final List<JSONObject> ret = CollectionUtils.jsonArrayToList(answersArray);

        for (final JSONObject j : ret) {
            j.put("user", userRepository.get(j.getString("AddUserID")));
        }

        return ret;
    }
}
