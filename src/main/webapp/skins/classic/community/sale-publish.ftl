<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="发出售 - 新助邦"/>
    </head>
    <body>
        <header class="fn-clear">
            <div class="fn-left" style="width: 75%;">         
                <#if Area != "">
                ${Area}-${University}<#if CollegeCode != "-1">-${College}</#if>
                </#if>
            </div>
            <a class="fn-right" href="/admin/set-community">圈子设置</a>
        </header>
        <#include "../common/community-nav.ftl">
        <div class="sub-nav">
            <span><i class="ico ico-radio-checked" data-type="1"></i>资料</span>
            <span><i class="ico ico-radio" data-type="2"></i>答疑</span>
            <span><i class="ico ico-radio" data-type="3"></i>授课</span>
        </div>
        <div class="wrap">
            <input class="input" id="title" placeholder="请填写标题名称"/>
            <textarea class="textarea" id="details" placeholder="亲，请填写具体描述..."></textarea>
            <div class="ft-gray">50</div>
            <input class="input" id="price" placeholder="请填写需要的费用"/>
            <div class="ft-gray">￥(元)</div>
            <button class="button" onclick="community.salePublish()">提 交</button>
        </div>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
        <script>
                community.salePublishInit();
        </script>
    </body>
</html>
