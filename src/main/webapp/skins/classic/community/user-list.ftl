<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="用户列表 - 新助邦"/>
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
        <ul class="list" data-page="1">
            <#list userCards as userCard>
            <li class="fn-clear">
                <a href="/user-card?type=${type}&userName=${userCard.userName}">
                    <img class="list-view"
                         onerror="this.src='/images/default-user-thumbnail.png'" 
                         src="/images/default-user-thumbnail.png"/>
                </a>
                <div class="list-content">
                    <a href="/user-card?type=${type}&userName=${userCard.userName}">
                        <div class="fn-clear">
                            <span class="fn-left ft-dark">${userCard.nickName}</span>
                            <#if userCard.IdentificationStatus != 0>
                            <span class="ico ico-cater"></span>
                            </#if>
                        </div>
                        <div class="ft-gray">
                            ${userCard.PropertyTitle}
                        </div>
                    </a>
                    <span class="ft-green follow"<#if userCard.isFollow> style="color: #F48A00"</#if> onclick=<#if isLoggedIn>"community.follow(${userCard.ID?c}, this)"<#else>"window.location.href='/login'"</#if>><#if userCard.isFollow>取消关注<#else>关注</#if></span>
                </div>
            </li>
            </#list>
        </ul>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
        <script>
            loadMore.init("/user-list-ajax?type=${type}&p=");
            loadMore.genHTML = function(obj) {
                var identification = "";
                if (obj.IdentificationStatus !== 0) {
                    identification = '<span class="ico ico-cater"></span>';
                }

                var liHTML = '<li class="fn-clear">'
                        + '<a href="/user-card?type=${type}&userName=' + obj.userName + '">'
                        + '<img class="list-view" onerror="this.src=\'/images/default-user-thumbnail.png\'" src="/images/default-user-thumbnail.png"/>'
                        + '</a>'
                        + '<div class="list-content">'
                        + '<a href="/user-card?type=${type}&userName=' + obj.userName + '">'
                        + '<div class="fn-clear">'
                        + '<span class="fn-left ft-dark">' + obj.nickName + '</span>'
                        + identification
                        + '</div>'
                        + '<div class="ft-gray">' + obj.PropertyTitle + '</div>'
                        + '</a>'
                        + '<span' + (obj.isFollow ? ' style="color: #F48A00"' : '') + ' class="ft-green follow" onclick="community.follow(' + obj.ID + ', this)">'
                        + (obj.isFollow ? "取消关注" : "关注") + '</span>'
                        + '</div>'
                        + '</li>';
                return liHTML;
            };
        </script>
    </body>
</html>
