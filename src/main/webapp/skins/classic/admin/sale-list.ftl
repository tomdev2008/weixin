<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的出售列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list">
            <#list sales as sale>
            <li class="fn-clear">
                <a href="/sale-details?id=${sale.id}">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div>
                            <#if sale.ItemType == 1>
                            <span class="ico-resource">资料</span>
                            <#elseif sale.ItemType == 2>
                            <span class="ico-qa">答疑</span>
                            <#elseif sale.ItemType == 3>
                            <span class="ico-school">授课</span>
                            </#if>
                            ${sale.Name}
                        </div>
                        <div class="ft-gray">
                            ${sale.Area}-${sale.University}<#if sale.CollegeCode != "-1">-${sale.College}</#if>
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                ${sale.CreateTime?string('yyyy-MM-dd')} &nbsp; 浏览 ${sale.ClickCount}
                            </span>
                            <span class="ft-green fn-right">￥${sale.Price}</span>
                        </div>
                    </div>
                </a>
            </li>
            </#list>
        </ul>
    </body>
</html>
