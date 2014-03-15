<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="需求列表 - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <#include "../common/community-sub-nav.ftl">
        <ul class="list">
            <#list 1..3 as i>
            <li class="fn-clear">
                <a href="/requirement-details">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div class="fn-clear">
                            <span class="ft-gray fn-left">{userName}</span>
                            <span class="ico ico-cater"></span>
                            <span class="ico ico-level1"></span>
                        </div>
                        <div>一句话描述</div>
                        <div class="ft-gray">
                            北京-北京大学-计算机
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                2013-21-12 &nbsp; 浏览：200
                            </span>
                            <span class="ft-green fn-right">￥200</span>
                        </div>
                    </div>
                </a>
            </li>
            </#list>
        </ul>
    </body>
</html>
