<!DOCTYPE html>
<html>
    <head>
        <#include "common/macro-meta.ftl">
        <@meta title="用户登录 - 新助邦"/>
    </head>
    <body>
        <div class="wrap">
            <div class="form">
                <label class="first">
                    帐号：
                    <input id="account" placeholder="请输入帐号"/>
                </label>
                <label>
                    密码：
                    <input id="password" type="password" placeholder="请输入密码"/>
                </label>
            </div>
            <div>
                <label class="ft-green"><input value="student" type="radio" checked="checked" name="cardType"/>学生身份</label>
                <label class="ft-green"><input type="radio" value="teacher" name="cardType"/>老师身份（或学长）</label>
            </div>
            <div class="ft-small">
                建议完善信息，让他人更信任你 <label class="ft-gray"><input type="checkbox" checked="checked"/> 记住登录状态</label>
            </div>
            <button class="button" onclick="login.login('/admin/user-card')">现在就去</button>
            <button class="button" onclick="login.login('/user-list')">以后完善</button>
            <div class="fn-clear">
                <a class="fn-left" href="/register">立即注册>></a>
                <a class="fn-right" href="/forget-password">忘记密码？</a>
            </div>
            <#include "common/footer.ftl">
        </div>
        <#include "common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/login.js"></script>
    </body>
</html>
