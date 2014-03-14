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
 * @fileOverview 社区
 *
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.1, Mar 13, 2014
 */
var community = {
    salePublish: function() {
        if ($.trim($("#title").val()).length === 0) {
            tip.show("提交失败", "标题不能为空");
        } else if ($.trim($("#details").val()).length === 0) {
            tip.show("提交失败", "描述不能为空");
        } else if ($.trim($("#money").val()).length === 0) {
            tip.show("提交失败", "价格不能为空");
        } else {
            var requestJSONObject = {
                title: $("#title").val(),
                details: $("#details").val(),
                money: $("#money").val()
            };

            $.ajax({
                url: "/login",
                type: "POST",
                cache: false,
                data: JSON.stringify(requestJSONObject),
                success: function(result, textStatus) {
                    if (result.sc) {
                        tip.show("成功提交！", "亲，现在去看看其他出售吧！");
                        $("#tipContent").next().text("GO").attr("onclick", "window.location='/sale-list'");
                    } else {
                        tip.show("提交失败", result.msg);
                    }
                }
            });
        }
    },
    salePublishInit: function () {
        $(".sub-nav > span").click(function () {
            $(".sub-nav > span i").removeClass("ico-radio-checked").addClass("ico-radio");
            $(this).find("i").addClass("ico-radio-checked").removeClass("ico-radio");
        });
    }
};