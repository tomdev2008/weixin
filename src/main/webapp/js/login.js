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
/**
 * @fileOverview 登录、注册、找回密码
 *
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.1, Mar 8, 2014
 */
var login = {
    login: function() {
        if ($.trim($("#account").val()).length === 0) {
            tip.show("登录失败", "帐号不能为空");
        } else if ($.trim($("#password").val()).length === 0) {
            tip.show("登录失败", "密码不能为空");
        } else {

        }
    },
    register: function() {
        if ($.trim($("#account").val()).length === 0) {
            tip.show("注册失败", "帐号不能为空");
        } else if ($.trim($("#password").val()).length === 0) {
            tip.show("注册失败", "密码不能为空");
        } else {

        }
    },
    getPossword: function() {
        if ($.trim($("#email").val()).length === 0) {
            tip.show("找回密码失败", "邮箱不能为空");
        } else {

        }
    }
};