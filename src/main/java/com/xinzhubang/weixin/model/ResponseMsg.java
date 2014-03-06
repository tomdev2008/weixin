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
package com.xinzhubang.weixin.model;

/**
 * 公众帐号给用户发的消息.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 6, 2014
 * @since 1.0.0
 */
public class ResponseMsg {

    /**
     * 接收方帐号（收到的OpenID）
     */
    public static final String ToUserName = "ToUserName";

    /**
     * 开发者微信号
     */
    public static final String FromUserName = "FromUserName";

    /**
     * 文本消息 - 文本内容
     */
    public static final String Content = "Content";

    public static final String MsgType = "MsgType";

    public static final String CreateTime = "CreateTime";

}
