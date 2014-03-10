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
package com.xinzhubang.weixin.util;

import junit.framework.Assert;
import org.json.JSONObject;
import org.testng.annotations.Test;

/**
 * JSON 工具工具单元测试.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 7, 2014
 * @since 1.0.0
 */
public class JSONsTestCase {

    @Test
    public void xmlToJSON() throws Exception {
        String jsonStr = JSONs.xmlToJSON("<xml>\n"
                                         + " <ToUserName><![CDATA[toUser]]></ToUserName>\n"
                                         + " <FromUserName><![CDATA[fromUser]]></FromUserName> \n"
                                         + " <CreateTime>1348831860</CreateTime>\n"
                                         + " <MsgType><![CDATA[text]]></MsgType>\n"
                                         + " <Content><![CDATA[this is a test]]></Content>\n"
                                         + " <MsgId>1234567890123456</MsgId>\n"
                                         + " </xml>");
        System.out.println(jsonStr);

        JSONObject jsonObject = new JSONObject(jsonStr);

        Assert.assertEquals("text", jsonObject.optString("MsgType"));
    }

    @Test
    public void jsonToXml() throws Exception {
        String xml = JSONs.jsonToXml("{\n"
                                     + "    \"ToUserName\": \"toUser\",\n"
                                     + "    \"FromUserName\": \"fromUser\",\n"
                                     + "    \"CreateTime\": \"1348831860\",\n"
                                     + "    \"MsgType\": \"text\",\n"
                                     + "    \"Content\": \"this is a test\",\n"
                                     + "    \"MsgId\": \"1234567890123456\"\n"
                                     + "}");
        System.out.println(xml);

    }
}
