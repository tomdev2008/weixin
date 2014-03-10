<!DOCTYPE html>
<html>
    <head>
        <#include "common/macro-meta.ftl">
        <@meta title="用户注册 - 新助邦"/>
    </head>
    <body>
        <div class="wrap">
            <div class="form">
                <label class="first">
                    邮箱：
                    <input id="account" placeholder="请输入邮箱"/>
                </label>
                <label>
                    昵称：
                    <input id="account" placeholder="请输入昵称"/>
                </label>
                <label>
                    密码：
                    <input id="password" placeholder="请输入密码"/>
                </label>
                <label>
                    确认密码：
                    <input id="confirmPassword" placeholder="请再次输入密码"/>
                </label>
            </div>
            <button class="button" onclick="login.register()">注 册</button>
            <a href="/login">已有帐号，去登录>></a>
            <#include "common/footer.ftl">
        </div>
        <#include "common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/login.js"></script>
    </body>
</html>
