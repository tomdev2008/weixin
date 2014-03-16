<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的关注列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list">
            <li class="fn-clear">
                <a href="/user-card">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                </a>
                <div class="list-content">
                    <a href="/user-card">
                        <div class="fn-clear">
                            <span class="fn-left ft-dark">{userCard.userName}</span>
                            <span class="ico ico-cater"></span>
                            <span class="ico ico-level1"></span>
                        </div>
                        <div class="ft-gray">
                            一句话描述
                        </div>
                        <div class="ft-gray">
                            圈子
                        </div>
                    </a>
                    <span class="ft-green follow" style="color: #F48A00" onclick="community.follow(this)">取消关注</span>
                </div>
            </li>
        </ul>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
