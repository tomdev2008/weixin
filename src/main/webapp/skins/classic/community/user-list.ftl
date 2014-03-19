<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="用户列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <ul class="list">
            <#list userCards as userCard>
            <li class="fn-clear">
                <a href="/user-card?type${type}&userName=${userCard.userName}">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                </a>
                <div class="list-content">
                    <a href="/user-card?type${type}&userName=${userCard.userName}">
                        <div class="fn-clear">
                            <span class="fn-left ft-dark">${userCard.userName}</span>
                            <span class="ico ico-cater"></span>
                            <span class="ico ico-level1"></span>
                        </div>
                        <div class="ft-gray">
                            ${userCard.PropertyTitle}
                        </div>
                        <div class="ft-gray">
                            圈子
                        </div>
                    </a>
                    <span class="ft-green follow" onclick=<#if isLoggedIn>"community.follow(${user.id?c}, this)"<#else>"window.location.href='/login'"</#if>><#if isFollow>取消关注<#else>关注</#if></span>
                </div>

            </li>
            </#list>
        </ul>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
