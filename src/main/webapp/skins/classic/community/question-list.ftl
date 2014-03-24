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
                       <#if subType == "3">class="current"</#if>>未解决</a>
                </li>
                <li style="width: 25%">
                    <a class="last" href="/question-publish">提问</a>
                </li>
            </ul>
        </div>
        <ul class="list">
            <#if  questionList??>  
            <#list questionList as q>
            <li class="fn-clear">
                <a href="/question-details?id=${q.id}">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div class="fn-clear">
                            <span class="fn-left ft-gray">${q.user.user_name}</span>
                            <span class="ico ico-cater"></span>
                            <span class="ico ico-level1"></span>
                        </div>
                        <div>
                            <span class="ft-green">【待解决】</span>${q.Title}
                        </div>
                        <div class="ft-gray">
                            ${q.Area}-${q.University}<#if q.CollegeCode != "-1">-${q.College}</#if>
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                 ${q.AddTime?string('yyyy-MM-dd')} &nbsp; 浏览 ${q.PV} &nbsp; 回应${q.count}
                            </span>
                            <span class="ft-green fn-right">${q.Points}</span>
                        </div>
                    </div>
                </a>
            </li>
            </#list>
            </#if>
        </ul>
    </body>
</html>
