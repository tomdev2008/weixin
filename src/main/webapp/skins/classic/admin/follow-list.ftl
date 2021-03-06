<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的关注列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list" data-page="1">
            <#list followingUsers as followingUser>
            <li class="fn-clear">
                <a href="/user-card?type=<#if followingUser.Property == 0>student<#else>teacher</#if>&userName=${followingUser.userName}">
                    <img class="list-view" 
                         onerror="this.src='/images/default-user-thumbnail.png'" 
                         src="/images/default-user-thumbnail.png"/>
                </a>
                <div class="list-content">
                    <a href="/user-card?type=<#if followingUser.Property == 0>student<#else>teacher</#if>&userName=${followingUser.userName}">
                        <div class="fn-clear" style="word-wrap: break-word; white-space: normal; word-break: break-all; width: 72%;">
                            <#if followingUser.Property == 0>
                            <span class="fn-left ico-qa">学生</span>
                            <#else>
                            <span class="fn-left ico-resource">老师</span>
                            </#if>
                            <span class="fn-left ft-dark">&nbsp;${followingUser.nickName}</span>
                            <#if followingUser.IdentificationStatus != 0>
                            <span class="ico ico-cater"></span>
                            </#if>
                        </div>
                        <div class="ft-gray">
                            ${followingUser.PropertyTitle}
                        </div>
                        <div class="ft-gray">
                            <#if followingUser.Area != "">
                            ${followingUser.Area}-${followingUser.University}<#if followingUser.CollegeCode != "-1">-${followingUser.College}</#if>
                            </#if>
                        </div>
                    </a>
                    <span class="ft-green follow" style="color: #F48A00" onclick=<#if isLoggedIn>"community.follow(${followingUser.ID?c}, this)"<#else>"window.location.href='/login'"</#if>><#if followingUser.isFollow>取消关注<#else>关注</#if></span>
                </div>
            </li>
            </#list>
        </ul>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
        <script>
            loadMore.init("/admin/follow-list-ajax?p=");
            loadMore.genHTML = function(obj) {
                var community = "";
                if (obj.Area !== "") {
                    community += obj.Area + '-' + obj.University;
                    if (obj.CollegeCode !== "-1") {
                        community += '-' + obj.College;
                    }
                }

                var type = '<span class="fn-left ico-resource">老师</span>';
                var typeStr = 'teacher';
                if (obj.Property === 0) {
                    type = '<span class="fn-left ico-qa">学生</span>';
                    type = 'student';
                }

                var identification = "";
                if (obj.IdentificationStatus !== 0) {
                    identification = '<span class="ico ico-cater"></span>';
                }
                var liHTML = '<li class="fn-clear">'
                        + '<a href="/user-card?type=' + typeStr + '&userName=' + obj.userName + '">'
                        + '<img class="list-view" onerror="this.src=\'/images/default-user-thumbnail.png\'" src="/images/default-user-thumbnail.png"/>'
                        + '</a>'
                        + '<div class="list-content">'
                        + '<a href="/user-card?type=' + typeStr + '&userName=' + obj.userName + '">'
                        + '<div class="fn-clear" style="word-wrap: break-word; white-space: normal; word-break: break-all; width: 72%;">' + type
                        + '<span class="fn-left ft-dark">&nbsp;' + obj.userName + '</span>'
                        + identification
                        + '</div>'
                        + '<div class="ft-gray">' + obj.PropertyTitle + '</div>'
                        + '<div class="ft-gray">' + community + '</div>'
                        + '</a>'
                        + '<span style="color: #F48A00" class="ft-green follow" onclick="community.follow(' + obj.ID + ', this)">'
                        + (obj.isFollow ? "取消关注" : "关注") + '</span>'
                        + '</div>'
                        + '</li>';
                return liHTML;
            };
        </script>
    </body>
</html>
