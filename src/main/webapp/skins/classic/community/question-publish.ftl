<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="提问 - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <div class="wrap">
            <div class="publish">
                <textarea id="content" placeholder="请填写您的问题" class="textarea"></textarea>
                <input id="keys" placeholder="请定义关键字" class="input" />
                <div class="ft-gray">关键字用空格隔开</div>
            </div>
        </div>
        <!--
        <div class="user-card-sub">类似问题及答案</div>
        <ul class="list" style="margin-bottom: 3em;">
            <li class="fn-clear">
                <a href="/question-details">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div>
                            <span class="ft-green">【待解决】</span>一句话描述
                        </div>
                        <div class="ft-gray">
                            圈子
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                2012-23-23 22:34 &nbsp; 浏览{sale.ClickCount} &nbsp;
                            </span>
                            <span class="ft-green fn-right">￥{sale.Price}</span>
                        </div>
                    </div>
                </a>
            </li>
        </ul>
        -->
        <div class="ft-green user-card-msg" onclick="community.questionPublish()">发 送</div>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
