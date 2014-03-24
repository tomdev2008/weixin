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
 * @fileOverview 通用组件
 *
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.1, Mar 8, 2014
 */
var tip = {
    show: function(title, content) {
        var windowH = $(window).height(),
                windowW = $(window).width();

        $(".tip-bg").height(windowH < document.documentElement.scrollHeight
                ? document.documentElement.scrollHeight : windowH);
        $(".tip-content").css({
            "top": parseInt((windowH - $(".tip-content").height()) / 2) + "px",
            "left": parseInt((windowW - $(".tip-content").width()) / 2) + "px"
        });

        $("#tipTitle").html(title);
        $("#tipContent").html(content);

        $(".tip-content, .tip-bg").show();
    },
    close: function() {
        $(".tip-content, .tip-bg").hide();
    }
};

var loadMore = {
    init: function(url) {
        $(window).scroll(function() {
            var $window = $(window),
                    $list = $(".list:last");
            if ($window.scrollTop() + $window.height() + 100 > $("body").height()) {
                if ($list.find(".loading").length === 0) {
                    $list.append('<li onclick="loadMore.load(\''
                            + url + '\')" class="loading ft-gray last">查看更多...</li>');
                    loadMore.load(url);
                }
            }
        });
    },
    load: function(url) {
        $.ajax({
            url: url + ($(".list:last").data("page") + 1),
            type: "GET",
            cache: false,
            success: function(result, textStatus) {
                $(".list:last .loading").remove();
                $(".list:last").data("page", result.pageNum);
                var listHTML = "",
                        sales = result.sales || result.requirements;
                if (sales.length === 0) {
                     $(window).unbind("scroll");
                }
                
                for (var i = 0, iLength = sales.length; i < iLength; i++) {
                    listHTML += loadMore.genHTML(sales[i]);
                }
                $(".list:last").append(listHTML);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                tip.show("加载失败", textStatus);
            }
        });
    }
};