<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="{title} - 新助邦"/>
    </head>
    <body class="sale-details">
        <#include "../common/community-nav.ftl">
        <div class="sub-nav">
            <ul class="fn-clear">
                <li>
                    <a href="/pocoer">系统推荐</a>
                </li>
                <li>
                    <a href="/partner-list">合作方列表</a>
                </li>
                <li style="text-align: right">
                    <a class="last" href="/requirement-publish">发需求</a>&nbsp;
                </li>
                <li>
                    <a class="last" href="/sale-publish">发出售</a>
                </li>
            </ul>
        </div>
        <div class="content">
            <div class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div>一句话描述</div>
                    <div class="ft-gray">
                        细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节细节
                    </div>
                    <div class="ft-gray">
                        北京-北京大学-计算机
                    </div>
                    <div class="fn-clear">
                        <span class="ft-gray fn-left">{userName}</span>
                        <span class="ico ico-cater"></span>
                        <span class="ico ico-level1"></span>
                    </div>
                    <div class="fn-clear">
                        <span class="ft-gray ft-small fn-left">
                            2013-21-12 &nbsp; 浏览 200
                        </span>
                        <span class="ft-green fn-right">￥200</span>
                    </div>
                </div>
            </div>
            <div class="fn-clear">
                <button class="button fn-left" onclick="window.location='/whisper'">说悄悄话</button>
                <button class="button fn-right" onclick='tip.show("温馨提示", "功能正在开发中，敬请期待");'>我要购买</button>
            </div>
        </div>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>