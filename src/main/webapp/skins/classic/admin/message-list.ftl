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
                        <#if msg.type == "gb">
                        <span class="fn-left ico-qa">留言</span>
                        <span class="fn-left">&nbsp;${msg.fromUser.user_name}</span>
                        <#if msg.fromUser.IDCardStatus != 0>
                        <span class="ico ico-cater"></span>
                        </#if>
                        </#if>

                        <#if msg.type == "w">
                        <span class="fn-left ico-resource">悄悄话</span>
                        <#if msg.fromUser.id != currUser.id>
                        <span class="fn-left">&nbsp;${msg.fromUser.user_name}</span>
                        <#if msg.fromUser.IDCardStatus != 0>
                        <span class="ico ico-cater"></span>
                        </#if>
                        </#if>
                        </#if>

                        <#if msg.type == "a">
                        <span class="fn-left ico-school">问答</span>
                        <#if msg.fromUser.id != currUser.id>
                        <span class="fn-left">&nbsp;${msg.fromUser.user_name}</span>
                        <#if msg.fromUser.IDCardStatus != 0>
                        <span class="ico ico-cater"></span>
                        </#if>
                        </#if>
                        </#if>
                    </div>
                    <div class="ft-gray">
                        <#if msg.type == "w">(${msg.item.Area}-${msg.item.University}<#if msg.item.CollegeCode != "-1">-${msg.item.College}</#if>) ${msg.item.Name}</#if>
                        <#if msg.type == "gb">${msg.GBookContent}</#if>
                        <#if msg.type == "a">${msg.NoticeContent}</#if>
                    </div>
                    <div class="fn-clear">
                        <#if msg.type == "gb">
                        <span class="ft-gray ft-small fn-left">
                            ${msg.PostTime?string('yyyy-MM-dd HH:mm:ss')}
                        </span>
                        <a href="/guest-book?toMemberID=${msg.SendID?c}" class="fn-right">回复</a>
                        <a href="/admin/message-gb-details?id=${msg.ID?c}" class="fn-right">${msg.count}条信息 &nbsp; &nbsp;</a>
                        </#if>
                        <#if msg.type == "w">
                        <span class="ft-gray ft-small fn-left">
                            ${msg.CreateTime?string('yyyy-MM-dd HH:mm:ss')}
                        </span>
                        <a href="/whisper?itemID=${msg.KeyID?c}&toMemberID=${msg.FromID?c}&id=${msg.ID?c}" class="fn-right">回复</a>
                        <a href="/admin/message-details?id=${msg.ID?c}" class="fn-right">${msg.count}条信息 &nbsp; &nbsp;</a>
                        </#if>
                        <#if msg.type == "a">
                        <span class="ft-gray ft-small fn-left">
                            ${msg.PostTime?string('yyyy-MM-dd HH:mm:ss')}
                        </span>
                        <a href="/question-details?id=${msg.CorrID?c}" class="fn-right">查看</a>
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
                        infoHTML = '';
                var currUserId = "${currUser.id?c}";

                if (obj.type === "w") {
                    type = '<span class="fn-left ico-resource">悄悄话</span>';
                    content = obj.CONTENT;
                    infoHTML = '<span class="ft-gray ft-small fn-left">'
                            + obj.CreateTime.substr(0, 19)
                            + '</span>'
                            + '<a href="/whisper?itemID=' + obj.KeyID + '&toMemberID='
                            + obj.FromID + '&id=' + obj.ID + '" class="fn-right">回复</a>'
                            + '<a href="/admin/message-details?id=' + obj.ID + '" class="fn-right">'
                            + obj.count + '条信息 &nbsp; &nbsp;</a>';
                } else if (obj.type === "a") {
                    type = '<span class="fn-left ico-school">问答</span>';
                    content = obj.NoticeContent;
                    infoHTML = '<span class="ft-gray ft-small fn-left">'
                            + obj.PostTime.substr(0, 19)
                            + '</span>'
                            + '<a href="/question-details?id=' + obj.CorrID + '" class="fn-right">查看</a>';
                } else {
                    infoHTML = '<span class="ft-gray ft-small fn-left">'
                        + obj.PostTime.substr(0, 19)
                        + '</span>'
                        + '<a href="/guest-book?toMemberID=' + obj.SendID + '" class="fn-right">回复</a>';
                }

                var liHTML = '<li class="fn-clear">'
                        + '<img class="list-view" onerror="this.src=\'/images/default-user-thumbnail.png\'" src="/images/default-user-thumbnail.png"/>'
                        + '<div class="list-content">'
                        + '<div class="fn-clear">'
                        + type;

                if (obj.fromUser.id.toString() !== currUserId) {
                    liHTML += '<span class="fn-left">&nbsp;' + obj.fromUser.user_name + '</span>' + identification;
                }
                
                liHTML += '</div>'
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
