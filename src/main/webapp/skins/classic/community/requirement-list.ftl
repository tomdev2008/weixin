<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="需求列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <#include "../common/community-sub-nav.ftl">
        <ul class="list">
            <#list requirements as requirement>
            <li class="fn-clear">
                <a href="/requirement-details?id=${requirement.ID}">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div class="fn-clear">
                            <span class="ft-gray fn-left">${requirement.userName}</span>
                            <span class="ico ico-cater"></span>
                            <span class="ico ico-level1"></span>
                        </div>
                        <div>${sale.Name}</div>
                        <div class="ft-gray">
                            ${requirement.Area}-${requirement.University}<#if requirement.CollegeCode != "-1">-${requirement.College}</#if>
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                 ${requirement.CreateTime?string('yyyy-MM-dd')} &nbsp; 浏览${requirement.ClickCount}
                            </span>
                            <span class="ft-green fn-right">￥${requirement.Price}</span>
                        </div>
                    </div>
                </a>
            </li>
            </#list>
        </ul>
    </body>
</html>
