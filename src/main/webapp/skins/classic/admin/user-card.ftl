<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="我的设置 - 新助邦"/>
    </head>
    <body>
        <div class="sub-nav">
            <ul class="fn-clear">
                <li style="width: 50%">
                    <a data-type="student" class="current" href="#">学生</a>
                </li>
                <li style="width: 50%">
                    <a data-type="teacher" href="#">老师</a>
                </li>
            </ul>
        </div>
        <div class="wrap">
            <input class="input" id="title" placeholder="请设置昵称"/>
            <div class="ft-gray">最多不能超过5个字</div>
            <input class="input" id="sign" placeholder="请设置您的个性签名"/>
            <div class="ft-gray">最多不能超过50个字</div>
            <textarea class="textarea" id="details" placeholder="请设置您个人详细介绍"></textarea>
            <div class="ft-gray">最多不能超过100个字</div>
            <button class="button" onclick="admin.setUserCard()">确 定</button>
        </div>
        <#include "../common/tip.ftl">
        <script src="/js/lib/jquery-2.1.0.min.js"></script>
        <script src="/js/common.js"></script>
        <script src="/js/community.js"></script>
        <script>
                admin.userCardInit();
        </script>
    </body>
</html>
