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

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * DES 加解密工具单元测试.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Mar 10, 2014
 * @since 1.0.0
 */
public class DESsTestCase {

    @Test
    public void encrypt() {
        System.out.println("encrypt");
        final String result = DESs.encrypt("dl88250", "XHJY");
        
        System.out.println(result);

        Assert.assertEquals(result, "A15415F348D2FD01");
    }

    @Test
    public void decrypt() {
        System.out.println("decrypt");
        final String result = DESs.decrypt("A15415F348D2FD01", "XHJY");
        
        System.out.println(result);
        
        Assert.assertEquals(result, "dl88250");
    }
}
