<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我要投标"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <div class="tender-tip" onclick="$(this).next().show();">亲，请选择投标服务</div>
        <ul class="list fn-none">
            <li class="fn-clear" id="111">
                <img class="list-view" src="/images/default-doc.jpg" onerror="this.src='/images/default-doc.jpg'">
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="ft-gray fn-left">summer99</span>
                        <span class="ico ico-cater"></span>
                    </div>
                    <div>资料</div>
                    <div class="fn-clear">
                        <span class="ft-gray ft-small fn-left"> 2014-04-01   浏览 0 </span>
                        <span class="ft-green fn-right">￥50</span>
                    </div>
                </div>
            </li>
            <li class="fn-clear" id="222">
                <img class="list-view" src="/images/default-doc.jpg" onerror="this.src='/images/default-doc.jpg'">
                <div class="list-content">
                    <div class="fn-clear">
                        <span class="ft-gray fn-left">summer99</span>
                        <span class="ico ico-cater"></span>
                    </div>
                    <div>资料</div>
                    <div class="fn-clear">
                        <span class="ft-gray ft-small fn-left"> 2014-04-01   浏览 0 </span>
                        <span class="ft-green fn-right">￥50</span>
                    </div>
                </div>
            </li>
        </ul>
        <a href="/sale-publish" class="tender-tip">还没有服务信息，现在就去发布</a>
        <div class="wrap">
            <textarea id="content" class="input" placeholder="请填写投标说明"></textarea>
            <input class="input" id="money" placeholder="请填写希望的价格" />
            <button class="button" onclick="community.requirementTender()">提 交</button>
        </div>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
        <script>
            community.requirementTenderInit();
        </script>
    </body>
</html>
