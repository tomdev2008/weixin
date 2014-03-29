<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="${user.cardTitle} - 新助邦"/>
    </head>
    <body>
        <header>
            ${Area}-${University}<#if CollegeCode != "-1">-${College}</#if>
            <a href="/admin/set-community">圈子设置</a>
        </header>
        <#include "../common/community-nav.ftl">
        <ul class="list">
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left ft-dark">${user.nick_name}</span>
                        <span class="ico ico-cater"></span>
                    </div>
                    <div class="ft-gray">
                        ${user.community.Area}-${user.community.University}<#if user.community.CollegeCode != "-1">-${user.community.College}</#if>
                    </div>
                    <div>
                        ${user.cardTitle}
                    </div>
                    <span<#if isFollow> style="color: #F48A00"</#if> class="ft-green follow" onclick=<#if isLoggedIn>"community.follow(${user.id?c}, this)"<#else>"window.location.href='/login'"</#if>><#if isFollow>取消关注<#else>关注</#if></span>
                </div>
            </li>
            <li class="ft-gray">
                个人信息介绍
            </li>
        </ul>
        <div class="user-card-sub">Ta 的<#if type == "student">需求<#else>服务</#if>列表</div>
        <ul class="list question" style="margin-bottom: 3em;">
            <#list items as item>
            <li class="fn-clear">
                <a href="/<#if type == "student">requirement<#else>sale</#if>-details?id=${item.ID?c}">
                   <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div>
                            <#if item.ItemType == 1>
                            <span class="ico-resource">资料</span>
                            <#elseif item.ItemType ==2>
                            <span class="ico-qa">答疑</span>
                            <#else>
                            <span class="ico-school">授课</span>
                            </#if>
                            ${item.Name}
                        </div>
                        <div class="ft-gray">
                            ${item.Area}-${item.University}<#if item.CollegeCode != "-1">-${item.College}</#if>
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                ${item.CreateTime?string('yyyy-MM-dd')} &nbsp; 浏览 ${item.ClickCount}
                            </span>
                            <span class="ft-green fn-right">￥${item.Price}</span>
                        </div>
                    </div>
                </a>
            </li>
            </#list>
        </ul>
        <a class="ft-green user-card-msg" href="/guest-book?toMemberID=${user.id?c}">
            给 Ta 留言
        </a>
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
