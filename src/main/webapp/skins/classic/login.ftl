<!DOCTYPE html>
<html>
    <head>
        <#include "../macro-meta.ftl">
        <@meta title="用户登录 - 新助邦"/>
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
            </div>
            <label class="ft-gray"><input type="checkbox" checked="checked"/> 记住登录状态</label>
            <button class="button">登 录</button>
            <div class="fn-clear">
                <a class="fn-left" href="/register">立即注册>></a>
                <a class="fn-right" href="/forget-password">忘记密码？</a>
            </div>
            <#include "footer.ftl">
        </div>
    </body>
</html>
