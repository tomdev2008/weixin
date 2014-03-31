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
import com.xinzhubang.weixin.repository.NoticeRepository;
import com.xinzhubang.weixin.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.FilterOperator;
import org.b3log.latke.repository.PropertyFilter;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.SortDirection;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 通知服务.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 31, 2014
 * @since 1.0.0
 */
@Service
public class NoticeService {

    private static final Logger LOGGER = Logger.getLogger(NoticeService.class);

    @Inject
    private NoticeRepository noticeRepository;
    @Inject
    private UserRepository userRepository;
    /**
     * 获取通知列表.
     *
     * @param msgType
     * @param pageNum
     * @return
     */
    public List<JSONObject> getNotices(final int msgType, final int pageNum) {
        try {
            final Query query = new Query().setFilter(new PropertyFilter("MsgType", FilterOperator.EQUAL, msgType));
            query.setCurrentPageNum(pageNum).setPageSize(XZBServletListener.PAGE_SIZE);

            query.addSort("PostTime", SortDirection.DESCENDING);

            final JSONObject result = noticeRepository.get(query);

            final JSONArray results = result.getJSONArray(Keys.RESULTS);

            final List<JSONObject> ret = CollectionUtils.jsonArrayToList(results);
            for (final JSONObject notice : ret) {
                notice.put("CreateTime", notice.opt("PostTime"));
                notice.put("type", "a");

                notice.put("toUser", userRepository.get(notice.getString("ReviceID")));
                notice.put("fromUser", userRepository.get(notice.getString("MemberID")));
            }

            return ret;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "获取社区圈子提问列表异常", e);

            return Collections.emptyList();
        }
    }

    /**
     * 添加一条通知.
     *
     * @param notice 指定的通知，例如：
     * <pre>
     * {
     *     "MemberID": int,
     *     "ReviceID": int,
     *     "NoticeContent": "",
     *     "IsRead": int,
     *     "MsgType": int, // 通知类型1:竞标2工作协议3项目管理4悄悄话5支付6系统20提问的回答
     *     "CorrID": int
     * }
     * </pre>
     *
     * @return
     */
    @Transactional
    public boolean addNotice(final JSONObject notice) {
        try {
            noticeRepository.add(notice);

            return true;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "添加通知异常", e);

            return false;
        }
    }
}
