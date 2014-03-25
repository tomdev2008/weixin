<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="提问列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <div class="sub-nav">
            <ul class="fn-clear">
                <li style="width: 25%">
                    <a href="/question-list?type=1"
                       <#if subType == "1">class="current"</#if>>最新问题</a>
                </li>
                <li style="width: 25%">
                    <a href="/question-list?type=2"
                       <#if subType == "2">class="current"</#if>>已解决</a>
                </li>
                <li style="width: 25%">
                    <a href="/question-list?type=3"
                       <#if subType == "3">class="current"</#if>>待解决</a>
                </li>
                <li style="width: 25%">
                    <a class="last" href="/question-publish">提问</a>
                </li>
            </ul>
        </div>
        <ul class="list" data-page="1">
            <#list questions as question>
            <li class="fn-clear">
                <a href="/question-details?id=${question.ID}">
                    <img class="list-view" 
                          onerror="this.src='/images/default-user-thumbnail.png'" 
                          src="TODO"/>
                    <div class="list-content">
                        <div class="fn-clear">
                            <span class="fn-left ft-gray">${question.user.user_name}</span>
                            <span class="ico ico-cater"></span>
                        </div>
                        <div>
                            <span class="ft-green TODO"></span>${question.Title}
                        </div>
                        <div class="ft-gray">
                            ${question.Area}-${question.University}<#if question.CollegeCode != "-1">-${question.College}</#if>
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                 ${question.AddTime?string('yyyy-MM-dd')} &nbsp; 浏览 ${question.PV} &nbsp; 回应 ${question.count}
                            </span>
                            <span class="ft-green fn-right">${question.Points}</span>
                        </div>
                    </div>
                </a>
            </li>
            </#list>
        </ul>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script>
            loadMore.init("/question-list-ajax?type=${subType}&p=");
            loadMore.genHTML = function(obj) {
                var community = obj.Area + '-' + obj.University;
                if (obj.CollegeCode !== "-1") {
                    community += '-' + obj.College;
                }
                var liHTML = '<li class="fn-clear">'
                        + '<a href="/sale-details?id=' + obj.id + '">'
                        + '<img class="list-view" onerror="this.src=\'/images/default-user-thumbnail.png\'" src="' + obj.user.avatar + '"/>'
                        + '<div class="list-content">'
                        + '<div class="fn-clear">'
                        + '<span class="ft-gray fn-left">' + obj.user.nick_name + '</span>'
                        + '<span class="ico ico-cater"></span>'
                        + '</div>'
                        + '<div>' + obj.Title + '</div>'
                        + '<div class="ft-gray">' + community
                        + '</div>'
                        + '<div class="fn-clear">'
                        + '<span class="ft-gray ft-small fn-left">'
                        + obj.AddTime.substr(0, 10) + '&nbsp; 浏览 ' + obj.PV + '&nbsp; 回应 ' + obj.count
                        + '</span>'
                        + '<span class="ft-green fn-right">￥' + obj.Points + '</span>'
                        + '</div>'
                        + '</div>'
                        + '</a>'
                        + '</li>';
                return liHTML;
            };
        </script>
    </body>
</html>
