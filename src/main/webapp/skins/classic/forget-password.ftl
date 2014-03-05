<!DOCTYPE html>
<html>
    <head>
        <#include "../macro-meta.ftl">
        <@meta title="忘记密码 - 新助邦"/>
    </head>
    <body>
        <div class="wrap">
            <div class="form">
                <label class="first">
                    邮箱：
                    <input placeholder="请输邮箱"/>
                </label>
            </div>
            <button class="button">提 交</button>
            <div class="fn-clear">
                <a class="fn-left" href="/register">立即注册>></a>
                <a class="fn-right" href="/login">已有帐号，去登录>></a>
            </div>
            <#include "footer.ftl">
        </div>
    </body>
</html>
