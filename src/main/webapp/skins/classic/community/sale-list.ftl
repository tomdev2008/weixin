<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="出售列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <#include "../common/community-sub-nav.ftl">
        <ul class="list">
            <#list sales as sale>
            <li class="fn-clear">
                <a href="/sale-details">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div class="fn-clear">
                            <span class="ft-gray fn-left">${sale.RealName}</span>
                            <span class="ico ico-cater"></span>
                            <span class="ico ico-level1"></span>
                        </div>
                        <div>${sale.Name}</div>
                        <div class="ft-gray">
                            ${sale.Area}-${sale.University}<#if sale.CollegeCode != "-1">-${sale.College}</#if>
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                ${sale.CreateTime?string('yyyy-MM-dd')} &nbsp; 浏览：${sale.ClickCount}
                            </span>
                            <span class="ft-green fn-right">${sale.Price}</span>
                        </div>
                    </div>
                </a>
            </li>
            </#list>
        </ul>
    </body>
</html>
