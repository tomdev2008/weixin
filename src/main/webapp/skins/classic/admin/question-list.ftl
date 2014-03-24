<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的提问列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list">
            <#list questions as question>
            <li class="fn-clear">
                <a href="/question-details?id=${question.id}">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div>
                            <span class="ft-green">【待解决】</span>
                            <span class="ft-green">【已解决】</span>
                            ${question.Title}
                        </div>
                        <div class="ft-gray">
                            ${question.Area}-${question.University}<#if question.CollegeCode != "-1">-${question.College}</#if>
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
    </body>
</html>
