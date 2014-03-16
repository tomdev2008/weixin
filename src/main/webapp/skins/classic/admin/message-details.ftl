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
                        <span class="fn-left">{sale.userName}</span>
                        <span class="ico ico-cater"></span>
                        <span class="ico ico-level1"></span>
                    </div>
                    <div class="ft-gray">
                        消息
                    </div>
                    <div class="ft-gray ft-small">
                        2012-23-23 12:12
                    </div>
                </div>
            </li>
        </ul>
        <a class="ft-green user-card-msg" href="/whisper">回 复</a>
    </body>
</html>
