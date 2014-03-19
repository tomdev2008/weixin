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
                    <a data-type="student" class="current" href="?type=student">学生</a>
                </li>
                <li style="width: 50%">
                    <a data-type="teacher" href="?type=teacher">老师</a>
                </li>
            </ul>
        </div>
        <div class="wrap">
            <input class="input" id="title" placeholder="${userCard.PropertyTitle}"/>
            <div class="ft-gray">最多不能超过50个字</div>
            <textarea class="textarea" id="details" placeholder="${userCard.PropertyRemark}"></textarea>
            <div class="ft-gray">最多不能超过100个字</div>
            <button class="button" onclick="admin.setUserCard('${type}')">确 定</button>
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
