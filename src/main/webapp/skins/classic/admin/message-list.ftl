<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的消息列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list">
            <#if list??>
                <#list list as l>
                    <li class="fn-clear">
                        <img class="list-view" src="/images/default-user-thumbnail.png"/>
                        <div class="list-content">
                            <div class="fn-clear">
                                <span class="fn-left">${l.fromUser.user_name}</span>
                                <span class="ico ico-cater"></span>
                                <span class="ico ico-level1"></span>
                            </div>
                            <div class="ft-gray">
                                ${l.CONTENT}
                            </div>
                            <div class="fn-clear">
                                <span class="ft-gray ft-small fn-left">
                                    2012-23-23 12:12
                                </span>
                                <a href="/whisper?itemID=${l.ID?c}&toMemberID=${l.FromID?c}" class="fn-right">回复</a>
                                <a href="/admin/message-details?id=${l.ID}" class="fn-right">${l.count}条信息 &nbsp; &nbsp;</a>
                            </div>
                        </div>
                    </li>
                </#list>
            </#if>
        </ul>
    </body>
</html>
