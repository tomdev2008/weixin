<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="${user.cardTitle} - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <ul class="list">
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left ft-dark">${user.user_name}</span>
                        <span class="ico ico-cater"></span>
                        <span class="ico ico-level1"></span>
                    </div>
                    <div class="ft-gray">
                        ${user.cardTitle}
                    </div>
                    <span class="ft-green follow" onclick=<#if isLoggedIn>"community.follow(${user.id?c}, this)"<#else>"window.location.href='/login'"</#if>><#if isFollow>取消关注<#else>关注</#if></span>
                </div>
            </li>
            <li class="ft-gray">
                个人信息介绍
            </li>
        </ul>
        <div class="user-card-sub">该学生的<#if type == "student">需求<#else>出售</#if>列表</div>
        <ul class="list" style="margin-bottom: 3em;">
            <#list items as item>
            <li class="fn-clear">
                <a href="/<#if type == "student">requirement<#else>sale</#if>-details?id=${item.ID?c}">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div>
                            <span class="ico-resource">资料</span>
                            <span class="ico-qa">答疑</span>
                            <span class="ico-school">授课</span>
                            ${item.Name}
                        </div>
                        <div class="ft-gray">
                            圈子
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                ${item.CreateTime?string('yyyy-MM-dd')} &nbsp; 浏览${item.ClickCount}
                            </span>
                            <span class="ft-green fn-right">￥${item.Price}</span>
                        </div>
                    </div>
                </a>
            </li>
            </#list>
        </ul>
        <a class="ft-green user-card-msg" href="/whisper">
          给他/她留言
        </a>
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
