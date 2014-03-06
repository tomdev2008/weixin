<!DOCTYPE html>
<html>
    <head>
        <#include "macro-meta.ftl">
        <@meta title="用户注册 - 新助邦"/>
    </head>
    <body>
        <div class="wrap">
            <div class="form">
                <label class="first">
                    帐号：
                    <input placeholder="请输入帐号"/>
                </label>
                <label>
                    密码：
                    <input placeholder="请输入密码"/>
                </label>
                <label>
                    确认密码：
                    <input placeholder="请输入密码"/>
                </label>
            </div>
            <button class="button">注 册</button>
            <a href="/login">已有帐号，去登录>></a>
            <#include "footer.ftl">
        </div>
    </body>
</html>
