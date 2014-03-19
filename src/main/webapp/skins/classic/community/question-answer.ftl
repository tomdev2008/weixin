<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="回答 - 新助邦"/>
    </head>
    <body>
        <div class="wrap">
            <textarea class="textarea" placeholder="请填写回答的内容"></textarea>
             <input type="hidden" id="id" value="${id}"/>
            <button class="button orange" onclick="community.questionAnswer()">回 答</button>
            <button class="button" onclick="community.cancel()">取 消</button>
        </div>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
    </body>
</html>
