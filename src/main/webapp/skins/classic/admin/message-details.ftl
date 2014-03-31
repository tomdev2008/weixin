<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的消息 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list question">
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left">${message.fromUser.user_name}</span>
                        <#if message.fromUser.IDCardStatus != 0>
                        <span class="ico ico-cater"></span>
                        </#if>
                    </div>
                    <div class="ft-gray">
                        ${message.CONTENT}
                    </div>
                    <div class="ft-gray ft-small">
                        ${message.CreateTime?string('yyyy-MM-dd HH:mm:ss')}
                    </div>
                </div>
            </li>
        </ul>
        <ul class="list question" style="margin-bottom: 3em">
            <#if list??>
            <#list list as l>
            <#if message.ID != l.ID>
            <li class="fn-clear<#if currUser.id == l.fromUser.id> my</#if>">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content" style="width: 80%">
                    <div class="fn-clear">
                        <span class="fn-left">${l.fromUser.user_name}</span>
                         <#if l.fromUser.IDCardStatus != 0>
                        <span class="ico ico-cater"></span>
                        </#if>
                    </div>
                    <div class="ft-gray">
                        ${l.CONTENT}
                    </div>
                    <div class="ft-gray ft-small">
                        ${l.CreateTime?string('yyyy-MM-dd HH:mm:ss')}
                    </div>
                </div>
            </li>
            </#if>
            </#list>
            </#if>
        </ul>
        <a class="user-card-msg" href="/whisper?itemID=${message.KeyID?c}&toMemberID=${message.FromID}&id=${message.ID}">回 复</a>
    </body>
</html>
