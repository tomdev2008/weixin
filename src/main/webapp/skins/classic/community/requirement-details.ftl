<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="需求详情 - 新助邦"/>
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
                    <div>
                        <#if requirement.ItemType == 1>
                        <span class="ico-resource">资料</span>
                        <#elseif requirement.ItemType == 2>
                        <span class="ico-qa">答疑</span>
                        <#elseif requirement.ItemType == 3>
                        <span class="ico-school">授课</span>
                        </#if>
                        ${requirement.Name}
                    </div>
                    <div class="ft-gray">
                        ${requirement.ItemContent}
                    </div>
                    <div class="ft-gray">
                        ${requirement.Area}<#if requirement.UniversityCode != "-1">-${requirement.University}</#if><#if requirement.CollegeCode != "-1">-${requirement.College}</#if>
                    </div>
                    <div class="fn-clear">
                        <span class="ft-gray fn-left">${requirement.userName}</span>
                        <span class="ico ico-cater"></span>
                    </div>
                    <div class="fn-clear">
                        <span class="ft-gray ft-small fn-left">
                            ${requirement.CreateTime?string('yyyy-MM-dd')} &nbsp; 浏览：${requirement.ClickCount}
                        </span>
                        <span class="ft-green fn-right">￥${requirement.Price}</span>
                    </div>
                </div>
            </div>
            <div class="fn-clear">
                <button class="button fn-left" onclick="window.location = '/whisper?itemID=${requirement.ID?c}&toMemberID=${requirement.MemberID?c}'">说悄悄话</button>
                <button class="button fn-right" onclick='tip.show("温馨提示", "功能正在开发中，敬请期待");'>我要投标</button>
            </div>
        </div>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
