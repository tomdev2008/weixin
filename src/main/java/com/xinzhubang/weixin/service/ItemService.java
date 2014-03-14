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

import com.xinzhubang.weixin.repository.ItemRepository;
import com.xinzhubang.weixin.repository.UserCardRepository;
import com.xinzhubang.weixin.repository.UserRepository;
import javax.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.json.JSONObject;

/**
 * 项目服务.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 14, 2014
 * @since 1.0.0
 */
@Service
public class ItemService {

    private static final Logger LOGGER = Logger.getLogger(ItemService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserCardRepository userCardRepository;

    @Inject
    private ItemRepository itemRepository;

    /**
     * 发布指定的服务.
     *
     * @param sale 指定的服务，例如：
     * <pre>
     * {
     *     "Name": "",
     *     "ItemType": int,
     *     "MemberID": int,
     *     "ItemContent": "",
     *     "Price": int
     * }
     * </pre>
     *
     * @return
     * @throws ServiceException
     */
    @Transactional
    public boolean publishSale(final JSONObject sale) throws ServiceException {
        try {
            sale.put("DemandOrService", 1);
            
            final String userId = sale.getString("MemberID");
            
            final JSONObject user = userRepository.get(userId);
            
            final String realName = user.optString("RealName");
            final String mobile = user.optString("mobile");
            final String email = user.optString("email");
            
            sale.put("RealName", realName);
            sale.put("Email", email);
            sale.put("LinkMan", realName);
            sale.put("Mobile", mobile);
            
            itemRepository.add(sale);

            return true;
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "发布出售 [" + sale.toString() + "] 异常", e);

            return false;
        }
    }

    
}
