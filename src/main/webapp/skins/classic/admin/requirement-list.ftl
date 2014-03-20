<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的需求列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list">
            <#list requirements as requirement>
            <li class="fn-clear">
                <a href="/requirement-details?id=${requirement.ID?c}">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div>
                            <#if requirement.ItemType == 1>
                            <span class="ico-resource">资料</span>
                            <#elseif requirement.ItemType == 2>
                            <span class="ico-qa">答疑</span>
                            <#elseif requirement.ItemType == 3>
                            <span class="ico-school">授课</span>
                            </#if>
                            ${requirement.Name}
                        </div>
                        <div class="ft-gray">
                            ${requirement.Area}-${requirement.University}<#if requirement.CollegeCode != "-1">-${requirement.College}</#if>
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                ${requirement.CreateTime?string('yyyy-MM-dd')} &nbsp; 浏览 ${requirement.ClickCount}
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
