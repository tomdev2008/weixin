<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的消息列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list">
            <#list whispers as whisper>
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left">${whisper.fromUser.user_name}</span>
                        <span class="ico ico-cater"></span>
                    </div>
                    <div class="ft-gray">
                        ${whisper.CONTENT}
                    </div>
                    <div class="fn-clear">
                        <span class="ft-gray ft-small fn-left">
                            ${whisper.CreateTime?string('yyyy-MM-dd HH:mm:ss')}
                        </span>
                        <a href="/whisper?itemID=${whisper.KeyID?c}&toMemberID=${whisper.FromID?c}" class="fn-right">回复</a>
                        <a href="/admin/message-details?id=${whisper.ID}" class="fn-right">${whisper.count}条信息 &nbsp; &nbsp;</a>
                    </div>
                </div>
            </li>
            </#list>

            <#list guestBooks as guestBook>
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left">${guestBook.fromUser.user_name}</span>
                        <span class="ico ico-cater"></span>
                        <span class="ico-qa">留言</span>
                    </div>
                    <div class="ft-gray">
                        ${guestBook.GBookContent}
                    </div>
                    <div class="fn-clear">
                        <span class="ft-gray ft-small fn-left">
                            ${guestBook.PostTime?string('yyyy-MM-dd HH:mm:ss')}
                        </span>
                        <a href="/guest-book?toMemberID=${guestBook.SendID?c}" class="fn-right">回复</a>
                    </div>
                </div>
            </li>
            </#list>
        </ul>
    </body>
</html>
