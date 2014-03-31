<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="${user.card.PropertyTitle} - 新助邦"/>
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
        <ul class="list">
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear" style="word-wrap: break-word; white-space: normal; word-break: break-all; width: 72%;">
                        <#if user.card.Property == 0>
                        <span class="fn-left ico-qa">学生</span>
                        <#else>
                        <span class="fn-left ico-resource">老师</span>
                        </#if>
                        <span class="fn-left ft-dark">&nbsp;${user.nick_name}</span>
                        <#if user.IDCardStatus != 0>
                        <span class="ico ico-cater"></span>
                        </#if>
                        <span class="ico ico-rate ft-gray ft-small">${user.card.FavorableRate?c}</span>
                        <#if user.Memberlevels != "">
                        <span class="fn-left ico-school">${user.Memberlevels}</span>
                        </#if>
                    </div>
                    <div class="ft-gray">
                        ${user.card.PropertyTitle}
                    </div>
                    <span<#if isFollow> style="color: #F48A00"</#if> class="ft-green follow" onclick=<#if isLoggedIn>"community.follow(${user.card.ID?c}, this)"<#else>"window.location.href='/login'"</#if>><#if isFollow>取消关注<#else>关注</#if></span>
                </div>
            </li>
            <li class="ft-gray ft-small">
                ${user.card.PropertyRemark}
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
                            <#if item.Area != "">
                            ${item.Area}-${item.University}<#if item.CollegeCode != "-1">-${item.College}</#if>
                            </#if>
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
        <a class="user-card-msg" href="/guest-book?toMemberID=${user.id?c}&form=user">
            给 Ta 留言
        </a>
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
