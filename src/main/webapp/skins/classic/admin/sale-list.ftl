<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的出售列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list" data-page="1">
            <#list sales as sale>
            <li class="fn-clear">
                <a href="/sale-details?id=${sale.ID?c}">
                    <img class="list-view" 
                         onerror="this.src='/images/default-doc.jpg'" 
                         src="/images/default-doc.jpg"/>
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
                            <#if sale.Area != "">
                            ${sale.Area}-${sale.University}<#if sale.CollegeCode != "-1">-${sale.College}</#if>
                            </#if>
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
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script>
            loadMore.init("/admin/sale-list-ajax?p=");
            loadMore.genHTML = function(obj) {
                var community = "",
                        type = '<span class="ico-resource">资料</span>';
                if (obj.Area !== "") {
                    community += obj.Area + '-' + obj.University;
                    if (obj.CollegeCode !== "-1") {
                        community += '-' + obj.College;
                    }
                }

                if (obj.ItemType === 2) {
                    type = '<span class="ico-qa">答疑</span>';
                } else if (obj.ItemType === 3) {
                    type = '<span class="ico-school">授课</span>';
                }
                var liHTML = '<li class="fn-clear">'
                        + '<a href="/sale-details?id=' + obj.ID + '">'
                        + '<img class="list-view" onerror="this.src=\'/images/default-doc.jpg\'" src="/images/default-doc.jpg"/>'
                        + '<div class="list-content">'
                        + '<div>' + type + " " + obj.Name + '</div>'
                        + '<div class="ft-gray">' + community
                        + '</div>'
                        + '<div class="fn-clear">'
                        + '<span class="ft-gray ft-small fn-left">'
                        + obj.CreateTime.substr(0, 10) + '&nbsp; 浏览 ' + obj.ClickCount
                        + '</span>'
                        + '<span class="ft-green fn-right">￥' + obj.Price + '</span>'
                        + '</div>'
                        + '</div>'
                        + '</a>'
                        + '</li>';
                return liHTML;
            };
        </script>
    </body>
</html>
