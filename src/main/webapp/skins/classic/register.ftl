<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
        <title>用户注册 - 新助邦</title>
        <link type="text/css" rel="stylesheet" href="${staticServePath}/css/base${miniPostfix}.css?${staticResourceVersion}" />
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
            <button class="button">登录</button>
            <div class="fn-clear">
                <a class="fn-left" href="/register">立即注册>></a>
                <a class="fn-right" href="/get-password">忘记密码？</a>
            </div>
            <#include "footer.ftl">
        </div>
    </body>
</html>
