<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="出售详情 - 新助邦"/>
    </head>
    <body>
        <#include "../common/community-nav.ftl">
        <div class="wrap">
            <div>亲，请填写你想预约的试听时间：</div>
            <input id="month" class="input" style="width:30%" /> 月 &nbsp; &nbsp;
            <input id="day" class="input" style="width:30%" /> 日<br/>
            <input id="hour" class="input" style="width:30%" /> 时 &nbsp; &nbsp;
            <input id="min" class="input" style="width:30%" /> 分
            <textarea id="content" class="input" placeholder="亲，有什么需要和老师说嘛？"></textarea>
            <button class="button" onclick="community.saleAudition()">提 交</button>
        </div>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
