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
        } else if ($.trim($("#price").val()).length === 0) {
            tip.show("提交失败", "价格不能为空");
        } else {
            var requestJSONObject = {
                name: $("#title").val(),
                desc: $("#details").val(),
                price: $("#price").val(),
                type: $(".sub-nav i.ico-radio-checked").data("type")
            };

            $.ajax({
                url: "/sale-publish",
                type: "POST",
                cache: false,
                data: JSON.stringify(requestJSONObject),
                success: function(result, textStatus) {
                    if (result.sc) {
                        tip.show("成功提交！", "现在就去看看系统推荐吧！");
                        $("#tipContent").next().text("GO").attr("onclick", "window.location='/sale-details?id="
                                + result.id + "'");
                    } else {
                        tip.show("提交失败", result.msg);
                    }
                }
            });
        }
    },
    salePublishInit: function() {
        $(".sub-nav > span").click(function() {
            $(".sub-nav > span i").removeClass("ico-radio-checked").addClass("ico-radio");
            $(this).find("i").addClass("ico-radio-checked").removeClass("ico-radio");
        });
    },
    requirementPublish: function() {
        if ($.trim($("#title").val()).length === 0) {
            tip.show("提交失败", "标题不能为空");
        } else if ($.trim($("#details").val()).length === 0) {
            tip.show("提交失败", "描述不能为空");
        } else if ($.trim($("#money").val()).length === 0) {
            tip.show("提交失败", "价格不能为空");
        } else {
            var requestJSONObject = {
                name: $("#title").val(),
                desc: $("#details").val(),
                price: $("#money").val(),
                type: $(".sub-nav i.ico-radio-checked").data("type")
            };

            $.ajax({
                url: "/requirement-publish",
                type: "POST",
                cache: false,
                data: JSON.stringify(requestJSONObject),
                success: function(result, textStatus) {
                    if (result.sc) {
                        tip.show("成功提交！", "现在去看看系统的推荐吧！");
                        $("#tipContent").next().text("GO").attr("onclick", "window.location='/requirement-details?id="
                                + result.id + "'");
                    } else {
                        tip.show("提交失败", result.msg);
                    }
                }
            });
        }
    },
    cancel: function() {
        history.back();
    },
    sendWhisper: function(itemID, toMemberID) {
        if ($.trim($(".textarea").val()).length === 0) {
            tip.show("发送失败", "内容不能为空");
        } else {
            var requestJSONObject = {
                KeyID: itemID,
                ToID: toMemberID,
                CONTENT: $(".textarea").val()
            };

            $.ajax({
                url: "/whisper",
                type: "POST",
                cache: false,
                data: JSON.stringify(requestJSONObject),
                success: function(result, textStatus) {
                    if (result.sc) {
                        var searchs = window.location.search.split("=");
                        window.location = '/admin/message-details?id=' + searchs[searchs.length - 1];
                    } else {
                        tip.show("发送失败", result.msg);
                    }
                }
            });
        }
    },
    sendGuestBook: function(toMemberID) {
        if ($.trim($(".textarea").val()).length === 0) {
            tip.show("发送失败", "内容不能为空");
        } else {
            var requestJSONObject = {
                ToID: toMemberID,
                Content: $(".textarea").val()
            };

            $.ajax({
                url: "/guest-book",
                type: "POST",
                cache: false,
                data: JSON.stringify(requestJSONObject),
                success: function(result, textStatus) {
                    if (result.sc) {
                        window.location = '/admin/message-list';
                    } else {
                        tip.show("发送失败", result.msg);
                    }
                }
            });
        }
    },
    follow: function(userId, it) {
        var $it = $(it),
                url = "/follow/user?id=" + userId;
        if ($it.text() !== "关注") {
            url = "/unfollow/user?id=" + userId;
        }

        $.ajax({
            url: url,
            type: "POST",
            cache: false,
            success: function(result, textStatus) {
                if (result.sc) {
                    if ($it.text() === "关注") {
                        $it.text("取消关注").css("color", "#F48A00");
                    } else {
                        $it.text("关注").css("color", "#5F9200");
                    }
                } else {
                    tip.show($it.text() + "失败", result.msg);
                }
            }
        });
    },
    questionAccept: function(qid, id) {
        var requestJSONObject = {
            id: id,
            qid: qid
        };
        $.ajax({
            url: "/accept?=id" + id,
            type: "POST",
            cache: false,
            data: JSON.stringify(requestJSONObject),
            success: function(result, textStatus) {
                if (result.sc) {
                    tip.show("答案采纳成功！");
                    $("#tipContent").next().text("GO").attr("onclick", "window.location='question-details?id=" + sc.qid + "'");
                } else {
                    tip.show("提示", result.msg);
                }
            }
        });
    },
    questionPublish: function() {
        if ($.trim($("#content").val()).length === 0) {
            tip.show("发送失败", "内容不能为空");
        } else if ($.trim($("#keys").val()).length === 0) {
            tip.show("发送失败", "关键字不能为空");
        } else {
            var requestJSONObject = {
                Content: $("#content").val(),
                Keywords: $("#keys").val()
            };

            $.ajax({
                url: "/question-publish",
                type: "POST",
                cache: false,
                data: JSON.stringify(requestJSONObject),
                success: function(result, textStatus) {
                    if (result.sc) {
                        window.location = '/question-list?type=1';
                    } else {
                        tip.show("提示", result.msg);
                    }
                }
            });
        }
    },
    questionAnswer: function() {
        if ($.trim($(".textarea").val()).length === 0) {
            tip.show("回答失败", "内容不能为空");
        } else {
            var requestJSONObject = {
                Content: $(".textarea").val(),
                QID: $('#id').val()
            };

            $.ajax({
                url: "/question-answer",
                type: "POST",
                cache: false,
                data: JSON.stringify(requestJSONObject),
                success: function(result, textStatus) {
                    if (result.sc) {
                        tip.show("回答成功", result.msg);
                        $("#tipContent").next().text("GO").attr("onclick", "window.location='/question-details?id=" + result.id + "'");
                        //window.location = '/question-details?id='+result.id;
                    } else {
                        tip.show("回答失败", result.msg);
                    }
                }
            });
        }
    }
};

var admin = {
    userCardInit: function() {
        $(".sub-nav a").click(function() {
            $(".sub-nav a").removeClass("current");
            $(this).addClass("current");
        });
    },
    setUserCard: function(type) {
        if ($.trim($("#nickName").val()).length === 0) {
            tip.show("提交失败", "昵称不能为空");
        } else if ($.trim($("#title").val()).length === 0) {
            tip.show("提交失败", "个性签名不能为空");
        } else if ($.trim($("#details").val()).length === 0) {
            tip.show("提交失败", "详细介绍不能为空");
        } else {
            var requestJSONObject = {
                nickName: $("#nickName").val(),
                title: $("#title").val(),
                details: $("#details").val(),
                type: type
            };

            $.ajax({
                url: "/admin/user-card",
                type: "POST",
                cache: false,
                data: JSON.stringify(requestJSONObject),
                success: function(result, textStatus) {
                    if (result.sc) {
                        tip.show("提示", "设置成功");
                    } else {
                        tip.show("提示", result.msg);
                    }
                }
            });
        }
    }
};
community.requirementPublishInit = community.salePublishInit;
community.requirementDetailsInit = community.saleDetailsInit;
