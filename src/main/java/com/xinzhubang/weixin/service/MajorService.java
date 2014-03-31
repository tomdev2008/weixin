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
package com.xinzhubang.weixin.service;

import com.xinzhubang.weixin.repository.MajorRepository;
import com.xinzhubang.weixin.repository.SchoolRepository;
import java.util.ArrayList;
import java.util.List;
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
 * @version 1.1.1.0, Mar 31, 2014
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
            final List<JSONObject> results = CollectionUtils.jsonArrayToList(result.getJSONArray(Keys.RESULTS));

            ret = new ArrayList<JSONObject>();

            for (final JSONObject j : results) {
                boolean duplicated = false;

                for (final JSONObject k : ret) {
                    if (j.optString("Name").equals(k.optString("Name"))) {
                        duplicated = true;

                        break;
                    }
                }

                if (!duplicated) {
                    ret.add(j);
                }
            }
        } else if (3 == type) {
            final JSONObject result = schoolRepository.get(new Query().setFilter(new PropertyFilter("MajorCode", FilterOperator.EQUAL, id)));
            final List<JSONObject> results = CollectionUtils.jsonArrayToList(result.getJSONArray(Keys.RESULTS));

            ret = new ArrayList<JSONObject>();

            // XXX: Performance Issue
            for (final JSONObject college : results) {
                final JSONObject subResult = schoolRepository.get(new Query().setFilter(new PropertyFilter("ID", FilterOperator.EQUAL,
                                                                                                           college.optString("ParentID"))));
                final List<JSONObject> subResults = CollectionUtils.jsonArrayToList(subResult.getJSONArray(Keys.RESULTS));

                for (final JSONObject i : subResults) {
                    final JSONObject subResult2 = schoolRepository.get(new Query().setFilter(new PropertyFilter("ID", FilterOperator.EQUAL,
                                                                                                                i.optString("ParentID"))));
                    final List<JSONObject> subResults2 = CollectionUtils.jsonArrayToList(subResult2.getJSONArray(Keys.RESULTS));

                    for (final JSONObject j : subResults2) {
                        boolean duplicated = false;

                        for (final JSONObject k : ret) {
                            if (j.optString("Name").equals(k.optString("Name"))) {
                                duplicated = true;

                                break;
                            }
                        }

                        if (!duplicated) {
                            ret.add(j);
                        }
                    }
                }
            }
        }

        return ret;
    }
}
