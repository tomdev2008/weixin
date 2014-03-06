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
 * 用户发给公众帐号的消息.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 6, 2014
 * @since 1.0.0
 */
public class RequestMsg {

    /**
     * 开发者微信号
     */
    public static final String ToUserName = "ToUserName";

    /**
     * 发送方帐号（一个OpenID）
     */
    public static final String FromUserName = "FromUserName";

    /**
     * 消息类型：text, image, location, link, event
     */
    public static final String MsgType = "MsgType";

    /**
     * 文本消息 - 文本内容
     */
    public static final String Content = "Content";

    /**
     * 图片消息 - 图片连接
     */
    public static final String PicUrl = "PicUrl";

    /**
     * 链接消息 - 标题
     */
    public static final String Title = "Title";

    /**
     * 链接消息 - 描述
     */
    public static final String Description = "Description";

    /**
     * 链接消息 - 链接
     */
    public static final String Url = "Url";

    public static final String CreateTime = "CreateTime";

    public static final String MsgId = "MsgId";
}
