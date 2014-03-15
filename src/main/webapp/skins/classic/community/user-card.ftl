<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="${cardTitle} - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <ul class="list">
            <li class="fn-clear">
                <img class="list-view" src="/images/default-user-thumbnail.png"/>
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="fn-left ft-dark">{sale.userName}</span>
                        <span class="ico ico-cater"></span>
                        <span class="ico ico-level1"></span>
                    </div>
                    <div class="ft-gray">
                        一句话描述
                    </div>
                    <span class="ft-green follow" onclick="community.follow(this)">关注</span>
                </div>
            </li>
            <li class="ft-gray">
                个人信息介绍
            </li>
        </ul>
        <div class="user-card-sub">该学生的<#if type == "student">需求<#else>出售</#if>列表</div>
        <ul class="list" style="margin-bottom: 3em;">
            <li class="fn-clear">
                <a href="/<#if type == "student">requirement<#else>sale</#if>sale-details?id={sale.ID}">
                    <img class="list-view" src="/images/default-user-thumbnail.png"/>
                    <div class="list-content">
                        <div>
                            <span class="ico-resource">资料</span>
                            <span class="ico-qa">答疑</span>
                            <span class="ico-school">授课</span>
                            {sale.Name}
                        </div>
                        <div class="ft-gray">
                            圈子
                        </div>
                        <div class="fn-clear">
                            <span class="ft-gray ft-small fn-left">
                                2012-12-12 &nbsp; 浏览{sale.ClickCount}
                            </span>
                            <span class="ft-green fn-right">￥{sale.Price}</span>
                        </div>
                    </div>
                </a>
            </li>
        </ul>
        <a class="ft-green user-card-msg" href="/whisper">
          给他/她留言
        </a>
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
