<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的提问列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/admin-nav.ftl">
        <ul class="list">
            <li class="fn-clear">
                <a href="/question-details">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div>
                            <span class="ft-green">【待解决】</span>
                            <span class="ft-green">【已解决】</span>
                            一句话描述
                        </div>
                        <div class="ft-gray">
                            圈子
                        </div>
                         <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                2012-23-23 12:12 &nbsp; 浏览{sale.ClickCount}
                            </span>
                            <span class="ft-green fn-right">￥{sale.Price}</span>
                        </div>
                    </div>
                </a>
            </li>
        </ul>
    </body>
</html>
