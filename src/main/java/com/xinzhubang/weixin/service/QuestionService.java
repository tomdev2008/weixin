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

import com.xinzhubang.weixin.repository.AnswerRepository;
import com.xinzhubang.weixin.repository.QuestionRepository;
import com.xinzhubang.weixin.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
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
 * 用户提问
 *
 * @author <a href="http://88250.b3log.org">ZJ</a>
 * @version 1.0.0.0, Mar 6, 2014
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
    /**
     * 添加提问
     * @param question
     * @return
     */
    @Transactional
    public String add(final JSONObject question) {
        String id = null;
        try {
            id = questionRepository.add(question);
        } catch (RepositoryException ex) {
            LOGGER.log(Level.ERROR, "保存提问出错！", ex);
            return null
                    ;
        }
        return id;
    }
     /**
     * 添加回答
     * @param answer
     * @return
     */
    @Transactional
    public String addAnswer(final JSONObject answer) {
        String id = null;
        try {
            answer.put("Agree", 0);
            id = answerRepository.add(answer);
        } catch (RepositoryException ex) {
            LOGGER.log(Level.ERROR, "保存回答出错！", ex);
            return null
                    ;
        }
        return id;
    }
    /**
     * 采纳答案
     * @param answer
     * @return
     */
    @Transactional
    public void acceptAnswer(int id) throws RepositoryException, JSONException {
         try {
            JSONObject answer = answerRepository.get(id+"");
            JSONObject answer2 = new JSONObject();
            answer2.put("Content", answer.get("Content"));
            answer2.put("AddUserID", answer.getInt("AddUserID"));
            answer2.put("AddTime", answer.get("AddTime"));
            answer2.put("QID", answer.getInt("QID"));
            answer2.put("Agree", 1);
            answerRepository.update(id+"",answer2 );
        } catch (RepositoryException ex) {
            LOGGER.log(Level.ERROR, "保存回答出错！", ex);
                    ;
        }
    }
    /**
     * 查询最新提问
     * @param userId
     * @param pageNum
     * @return 
     */
    public List<JSONObject> questionList(final int userId,final int pageNum,String collegeCode){
         try {
            final List<Filter> filters = new ArrayList<Filter>();            
            if(userId!=0){
                 filters.add(new PropertyFilter("AddUserID", FilterOperator.EQUAL, userId));
             } 
            if(StringUtils.isNotEmpty(collegeCode)){
                 filters.add(new PropertyFilter("CollegeCode", FilterOperator.EQUAL, collegeCode));
             } 
            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));
            final JSONObject result = questionRepository.get(query);
            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);
            for(JSONObject j:ret){
                j.put("user", userRepository.get(j.getString("AddUserID")));
                final JSONObject subResult = answerRepository.get(new Query().setFilter(new PropertyFilter("QID", FilterOperator.EQUAL, j.getInt("id"))));
                j.put("count", subResult.getJSONArray(Keys.RESULTS).length());
            }
            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取提问列表出错", e);

            return Collections.emptyList();
        }
    }
    /**
     * 根据ID获取
     * @param id
     * @return
     * @throws RepositoryException 
     * @throws org.json.JSONException 
     */
    public JSONObject getById(String id) throws RepositoryException, JSONException{
       //根据id获取问题
       JSONObject j = questionRepository.get(id);
       //放入问题的用户
       j.put("user", userRepository.get(j.getString("AddUserID")));
       return j;
    }
    public List<JSONObject> queryAnswerByQuestionId(int id) throws RepositoryException, JSONException{
       final Query query = new Query().setFilter(new PropertyFilter("QID", FilterOperator.EQUAL, id));
       JSONObject answers = answerRepository.get(query);
       final JSONArray answersArray = answers.getJSONArray(Keys.RESULTS);
       final List<JSONObject> answersList = CollectionUtils.jsonArrayToList(answersArray);
       for(JSONObject j:answersList){
                j.put("user", userRepository.get(j.getString("AddUserID")));
       }
       return answersList;
    }
}
