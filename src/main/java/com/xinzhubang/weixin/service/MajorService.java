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

import com.xinzhubang.weixin.repository.MajorRepository;
import com.xinzhubang.weixin.repository.SchoolRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.FilterOperator;
import org.b3log.latke.repository.PropertyFilter;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 专业搜索相关.
 *
 * @author 赵晶
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.1.0, Mar 29, 2014
 * @since 1.0.0
 */
@Service
public class MajorService {

    private static final Logger LOGGER = Logger.getLogger(MajorService.class);

    @Inject
    private MajorRepository majorRepository;

    @Inject
    private SchoolRepository schoolRepository;

    public List<JSONObject> seach(final String id, final int type) throws RepositoryException, JSONException {
        List<JSONObject> ret = null;

        if (StringUtils.isEmpty(id)) {
            final JSONObject result = majorRepository.get(new Query().setFilter(new PropertyFilter("MajorLevel", FilterOperator.EQUAL, 0)));

            final JSONArray results = result.getJSONArray(Keys.RESULTS);

            return CollectionUtils.jsonArrayToList(results);
        }

        if (type == 1) {
            final JSONObject result = majorRepository.get(new Query().setFilter(new PropertyFilter("MajorParentID", FilterOperator.EQUAL, id)));
            final JSONArray results = result.getJSONArray(Keys.RESULTS);

            ret = CollectionUtils.jsonArrayToList(results);
        } else if (type == 2) {
            final JSONObject result = schoolRepository.get(new Query().setFilter(new PropertyFilter("MajorCode", FilterOperator.LIKE, id + "%")));
            final JSONArray results = result.getJSONArray(Keys.RESULTS);
            
            ret = CollectionUtils.jsonArrayToList(results);
            
            Set<String> sets = new HashSet<String>();
            
            for (final JSONObject j : ret) {
                sets.add(j.getString("Name"));
            }
            
            ret = new ArrayList<JSONObject>();
            
            for (final String s : sets) {
                JSONObject j = new JSONObject();
                j.put("Name", s);
                ret.add(j);
            }
        }

        return ret;
    }
}
