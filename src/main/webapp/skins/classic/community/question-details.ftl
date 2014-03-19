<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="提问详情 - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <ul class="list">
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left ft-gray">${question.user.user_name}</span>
                        <span class="ico ico-cater"></span>
                        <span class="ico ico-level1"></span>
                    </div>
                    <div>
                        <span class="ft-green">【待解决】</span>${question.Title}
                    </div>
                    <div class="ft-gray">
                        圈子
                    </div>
                    <div class="fn-clear">
                        <span class="ft-gray ft-small fn-left">
                            2012-23-23 &nbsp; 浏览${question.PV} &nbsp; 回应{sale.ClickCount}
                        </span>
                        <span class="ft-green fn-right">￥${question.Points}</span>
                    </div>
                </div>
            </li>
        </ul>
        <ul class="list question" style="margin-bottom: 3em;">
            <#if answers??>
            <#list answers as a>
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left ft-green">${a.user.user_name}</span>
                        <span class="ico ico-cater"></span>
                        <span class="ico ico-level1"></span>
                    </div>
                    <div>
                        ${a.Content}
                    </div>
                    <div class="fn-clear">
                        <span class="ft-gray ft-small fn-left">
                             ${a.AddTime?string('yyyy-MM-dd')}
                        </span>
                        <span onclick="community.questionAccept()" class="ft-green fn-right question-accept">采纳</span>
                    </div>
                </div>
            </li>
            </#list>
            </#if>
        </ul>
        <a class="ft-green user-card-msg" href="/question-answer?id=${question.id}">回 答</a>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
