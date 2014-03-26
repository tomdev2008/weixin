<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="发需求 - 新助邦"/>
    </head>
    <body>
        <header>${Area}-${University}<#if CollegeCode != "">-${College}</#if></header>
        <#include "../common/community-nav.ftl">
        <div class="sub-nav">
            <span><i class="ico ico-radio-checked" data-type="1"></i>资料</span>
            <span><i class="ico ico-radio" data-type="2"></i>答疑</span>
            <span><i class="ico ico-radio" data-type="3"></i>授课</span>
        </div>
        <div class="wrap">
            <div class="publish">
                <input class="input" id="title" placeholder="请填写标题名称"/>
                <textarea class="textarea" id="details" placeholder="亲，请填写具体描述..."></textarea>
                <div>50</div>
                <input class="input" id="money" placeholder="请填写愿意支付的价格"/>
                <div>￥(元)</div>
            </div>
            <button class="button" onclick="community.requirementPublish()">提 交</button>
        </div>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
        <script>
            community.requirementPublishInit();
        </script>
    </body>
</html>
