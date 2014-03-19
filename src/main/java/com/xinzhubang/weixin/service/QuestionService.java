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

import com.xinzhubang.weixin.repository.QuestionRepository;
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

    /**
     * 添加提问
     * @param question
     * @return
     */
    @Transactional
    public String add(final JSONObject question) {
        String id = null;
        try {
            System.out.println(questionRepository);
            id = questionRepository.add(question);
        } catch (RepositoryException ex) {
            LOGGER.log(Level.ERROR, "保存用户出错！", ex);
            return null
                    ;
        }
        return id;
    }
    /**
     * 查询最新提问
     * @param param
     * @param pageNum
     * @return 
     */
    public List<JSONObject> questionList(final JSONObject param,final int pageNum){
         try {
            final List<Filter> filters = new ArrayList<Filter>();
            /**filters.add(new PropertyFilter("MemberID", FilterOperator.EQUAL, userId));
            filters.add(new PropertyFilter("DemandOrService", FilterOperator.EQUAL, 1));

            final Query query = new Query().setFilter(new CompositeFilter(CompositeFilterOperator.AND, filters));**/
            final Query query = new Query();
            final JSONObject result = questionRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取提问列表出错", e);

            return Collections.emptyList();
        }
    }
}
