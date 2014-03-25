<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的消息 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list" style="margin-bottom: 3em">
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left">${message.fromUser.user_name}</span>
                        <span class="ico ico-cater"></span>
                    </div>
                    <div class="ft-gray">
                        ${message.CONTENT}
                    </div>
                    <div class="ft-gray ft-small">
                        ${message.CreateTime?string('yyyy-MM-dd HH:mm:ss')}
                    </div>
                </div>
            </li>
回复：
            <#if list??>
            <#list list as l>
                <li class="fn-clear">
                            <img class="list-view" src="/images/default-user-thumbnail.png"/>
                            <div class="list-content">
                                <div class="fn-clear">
                                    <span class="fn-left">${l.fromUser.user_name}</span>
                                    <span class="ico ico-cater"></span>
                                </div>
                                <div class="ft-gray">
                                    ${l.CONTENT}
                                </div>
                                <div class="ft-gray ft-small">
                                    ${l.CreateTime?string('yyyy-MM-dd HH:mm:ss')}
                                </div>
                            </div>
                        </li>
            </#list>
            </#if>
        </ul>
        <a class="ft-green user-card-msg" href="/whisper?itemID=${message.ID}&toMemberID=${message.FromID}">回 复</a>
    </body>
</html>
