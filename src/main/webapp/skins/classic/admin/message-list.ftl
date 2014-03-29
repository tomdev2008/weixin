<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的消息列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list" data-page="1">
            <#list messages as msg>
            <li class="fn-clear">
                <img class="list-view" onerror="this.src='/images/default-user-thumbnail.png'"
                     src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <#if msg.type == "gb"><span class="fn-left ico-qa">留言</span></#if>
                        <#if msg.type == "w"><span class="fn-left ico-resource">悄悄话</span></#if>
                        <span class="fn-left">&nbsp;${msg.fromUser.user_name}</span>
                        <#if msg.fromUser.IDCardStatus != 0>
                        <span class="ico ico-cater"></span>
                        </#if>
                    </div>
                    <div class="ft-gray">
                        <#if msg.type == "w">${msg.CONTENT}</#if>
                        <#if msg.type == "gb">${msg.GBookContent}</#if>
                    </div>
                    <div class="fn-clear">
                        <#if msg.type == "gb">
                        <span class="ft-gray ft-small fn-left">
                            ${msg.PostTime?string('yyyy-MM-dd HH:mm:ss')}
                        </span>
                        <a href="/guest-book?toMemberID=${msg.SendID?c}" class="fn-right">回复</a>
                        </#if>
                        <#if msg.type == "w">
                        <span class="ft-gray ft-small fn-left">
                            ${msg.CreateTime?string('yyyy-MM-dd HH:mm:ss')}
                        </span>
                        <a href="/whisper?itemID=${msg.KeyID?c}&toMemberID=${msg.FromID?c}&id=${msg.ID}" class="fn-right">回复</a>
                        <a href="/admin/message-details?id=${msg.ID}" class="fn-right">${msg.count}条信息 &nbsp; &nbsp;</a>
                        </#if>
                    </div>
                </div>
            </li>
            </#list>
        </ul>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script>
            loadMore.init("/admin/message-list-ajax?p=");
            loadMore.genHTML = function(obj) {
                var identification = "";
                if (obj.fromUser.IDCardStatus !== 0) {
                    identification = '<span class="ico ico-cater"></span>';
                }

                var type = '<span class="fn-left ico-qa">留言</span>',
                        content = obj.GBookContent,
                        infoHTML = '<span class="ft-gray ft-small fn-left">'
                        + obj.PostTime
                        + '</span>'
                        + '<a href="/guest-book?toMemberID=' + obj.SendID + '" class="fn-right">回复</a>';
                if (obj.type === "w") {
                    type = '<span class="fn-left ico-resource">悄悄话</span>';
                    content = obj.CONTENT;
                    infoHTML = '<span class="ft-gray ft-small fn-left">'
                            + obj.CreateTime
                            + '</span>'
                            + '<a href="/whisper?itemID=' + obj.KeyID + '&toMemberID='
                            + obj.FromID + '&id=' + obj.ID + '" class="fn-right">回复</a>'
                            + '<a href="/admin/message-details?id=' + obj.ID + '" class="fn-right">'
                            + obj.count + '条信息 &nbsp; &nbsp;</a>';
                }

                var liHTML = '<li class="fn-clear">'
                        + '<img class="list-view" onerror="this.src=\'/images/default-user-thumbnail.png\'" src="/images/default-user-thumbnail.png"/>'
                        + '<div class="list-content">'
                        + '<div class="fn-clear">'
                        + type
                        + '<span class="fn-left">&nbsp;' + obj.fromUser.user_name + '</span>'
                        + identification
                        + '</div>'
                        + '<div class="ft-gray">' + content + '</div>'
                        + '<div class="fn-clear">'
                        + infoHTML
                        + '</div></div>'
                        + '</li>';
                return liHTML;
            };
        </script>
    </body>
</html>
