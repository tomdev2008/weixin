<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="需求列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <#include "../common/community-sub-nav.ftl">
        <ul class="list" data-page="1">
            <#list requirements as requirement>
            <li class="fn-clear">
                <a href="/requirement-details?id=${requirement.ID?c}">
                    <img class="list-view" onerror="this.src='/images/default-doc.png'" src="${requirement.Thumbnail}"/>
                    <div class="list-content">
                        <div class="fn-clear">
                            <span class="ft-gray fn-left">${requirement.userName}</span>
                            <span class="ico ico-cater"></span>
                            <span class="ico ico-level1"></span>
                        </div>
                        <div>${requirement.Name}</div>
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
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script>
            loadMore.init("/requirement-list-ajax?p=");
            loadMore.genHTML = function(obj) {
                var community = obj.Area + '-' + obj.University;
                if (obj.CollegeCode !== "-1") {
                    community += '-' + obj.College;
                }
                var liHTML = '<li class="fn-clear">'
                        + '<a href="/sale-details?id=' + obj.ID + '">'
                        + '<img class="list-view" onerror="this.src=\'/images/default-doc.png\'" src="' + obj.Thumbnail + '"/>'
                        + '<div class="list-content">'
                        + '<div class="fn-clear">'
                        + '<span class="ft-gray fn-left">' + obj.userName + '</span>'
                        + '<span class="ico ico-cater"></span>'
                        + '<span class="ico ico-level1"></span>'
                        + '</div>'
                        + '<div>' + obj.Name + '</div>'
                        + '<div class="ft-gray">' + community
                        + '</div>'
                        + '<div class="fn-clear">'
                        + '<span class="ft-gray ft-small fn-left">'
                        + obj.CreateTime.substr(0, 10) + '&nbsp; 浏览' + obj.ClickCount
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
