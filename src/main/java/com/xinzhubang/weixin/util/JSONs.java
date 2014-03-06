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

import net.sf.json.xml.XMLSerializer;

/**
 * JSON 工具.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 6, 2014
 * @since 1.0.0
 */
public class JSONs {

    /**
     * xml转JSON
     *
     * @param xml
     * @return
     */
    public static String xmlToJSON(String xml) {
        XMLSerializer serializer = new XMLSerializer();
        
        return serializer.read(xml).toString(4);
    }

    /**
     * JSON转XML
     *
     * @param json
     * @return
     */
    public static String jsonToXml(String json) {
        net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(json);
        XMLSerializer xml = new XMLSerializer();
        xml.setRootName("xml");
        xml.setTypeHintsEnabled(false);
        
        return xml.write(obj, "UTF-8");
    }

    private JSONs() {
    }
}
