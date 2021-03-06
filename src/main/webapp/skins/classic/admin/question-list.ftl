<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的提问列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list" data-page="1">
            <#list questions as question>
            <li class="fn-clear">
                <a href="/question-details?id=${question.ID?c}">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div>
                            <span class="ft-green"><#if 0 == question.BestAnswer>【待解决】<#else>【已解决】</#if></span>
                            ${question.Title}
                        </div>
                        <div class="ft-gray">
                            <#if question.Area != "">
                            ${question.Area}-${question.University}<#if question.CollegeCode != "-1">-${question.College}</#if>
                            </#if>
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                ${question.AddTime?string('yyyy-MM-dd')} &nbsp; 浏览 ${question.PV}
                            </span>
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
            loadMore.init("/admin/question-list-ajax?p=");
            loadMore.genHTML = function(obj) {
                var resolution = "【已解决】";
                if (obj.BestAnswer === 0) {
                    resolution = "【待解决】";
                }
                
                var community = "";
                 if (obj.Area !== "") {
                    community += obj.Area + '-' + obj.University;
                    if (obj.CollegeCode !== "-1") {
                        community += '-' + obj.College;
                    }
                }
                
                var liHTML = '<li class="fn-clear">'
                        + '<a href="/question-details?id=' + obj.ID + '">'
                        + '<img class="list-view" onerror="this.src=\'/images/default-user-thumbnail.png\'" src="' + obj.user.avatar + '"/>'
                        + '<div class="list-content">'
                        + '<div><span class="ft-green">' + resolution + '</span>' + obj.Title + '</div>'
                        + '<div class="ft-gray">' + community
                        + '</div>'
                        + '<div class="fn-clear">'
                        + '<span class="ft-gray ft-small fn-left">'
                        + obj.AddTime.substr(0, 10) + '&nbsp; 浏览 ' + obj.PV
                        + '</span>'
                        + '</div>'
                        + '</div>'
                        + '</a>'
                        + '</li>';
                return liHTML;
            };
        </script>
    </body>
</html>
