<!DOCTYPE html>
<html>
    <head>
        <#include "common/macro-meta.ftl">
        <@meta title="忘记密码 - 新助邦"/>
    </head>
    <body>
        <div class="wrap">
            <div class="form">
                <label class="first">
                    邮箱：
                    <input id="email" placeholder="请输邮箱"/>
                </label>
            </div>
            <button class="button" onclick="login.getPossword()">提 交</button>
            <div class="fn-clear">
                <a class="fn-left" href="/register">立即注册>></a>
                <a class="fn-right" href="/login">已有帐号，去登录>></a>
            </div>
            <#include "common/footer.ftl">
        </div>
        <#include "common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/login.js"></script>
    </body>
</html>
