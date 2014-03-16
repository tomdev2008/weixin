<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的消息列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list">
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left">{sale.userName}</span>
                        <span class="ico ico-cater"></span>
                        <span class="ico ico-level1"></span>
                    </div>
                    <div class="ft-gray">
                        消息
                    </div>
                    <div class="fn-clear">
                        <span class="ft-gray ft-small fn-left">
                            2012-23-23 12:12
                        </span>
                        <a href="/whisper" class="fn-right">回复</a>
                        <a href="/admin/message-details" class="fn-right">n条信息 &nbsp; &nbsp;</a>
                    </div>
                </div>
            </li>
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div>系统消息</div>
                    <div class="ft-gray">
                        消息
                    </div>
                    <div class="ft-gray ft-small">
                        2012-23-23 12:12
                    </div>
                </div>
            </li>
        </ul>
    </body>
</html>
