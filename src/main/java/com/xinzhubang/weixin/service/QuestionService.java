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
import javax.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.annotation.Service;
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
     *
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
}
