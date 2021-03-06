<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="出售列表 - 新助邦"/>
    </head>
    <body>
       <header class="fn-clear">
            <div class="fn-left" style="width: 75%;">         
                <#if Area != "">
                ${Area}-${University}<#if CollegeCode != "-1">-${College}</#if>
                </#if>
            </div>
            <a class="fn-right" href="/admin/set-community">圈子设置</a>
        </header>
        <#include "../common/community-nav.ftl">
        <#include "../common/community-sub-nav.ftl">
        <ul class="list" data-page="1">
            <#list sales as sale>
            <li class="fn-clear">
                <a href="/sale-details?id=${sale.ID?c}">
                    <img class="list-view"
                         onerror="this.src='/images/default-doc.jpg'"
                         src="/images/default-doc.jpg"/>
                    <div class="list-content">
                        <div class="fn-clear">
                            <span class="ft-gray fn-left">${sale.userName}</span>
                            <#if sale.IDCardStatus != 0>
                            <span class="ico ico-cater"></span>
                            </#if>
                        </div>
                        <div>${sale.Name}</div>
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
            loadMore.init("/sale-list-ajax?type=${subType}&p=");
            loadMore.genHTML = function(obj) {
                var identification = "";
                if (obj.IDCardStatus !== 0) {
                    identification = '<span class="ico ico-cater"></span>';
                }

                var liHTML = '<li class="fn-clear">'
                        + '<a href="/sale-details?id=' + obj.ID + '">'
                        + '<img class="list-view" src="/images/default-doc.jpg" onerror="this.src=\'/images/default-doc.jpg\'"/>'
                        + '<div class="list-content">'
                        + '<div class="fn-clear">'
                        + '<span class="ft-gray fn-left">' + obj.userName + '</span>'
                        + identification
                        + '</div>'
                        + '<div>' + obj.Name + '</div>'
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
