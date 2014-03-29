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
                    <a data-type="student" <#if type=="student">class="current" </#if>href="?type=student">学生</a>
                </li>
                <li style="width: 50%">
                    <a data-type="teacher" <#if type=="teacher">class="current" </#if>href="?type=teacher">老师</a>
                </li>
            </ul>
        </div>
        <div class="wrap">
            <input class="input" id="nickName" value="${userCard.nickName!}" placeholder="请设置昵称"/>
            <div class="ft-gray ft-small">最多不能超过5个字</div>
            <input class="input" id="title" value="${userCard.PropertyTitle!}" placeholder="请设置您的个性签名"/>
            <div class="ft-gray ft-small">最多不能超过5个字</div>
            <textarea class="textarea" id="details" placeholder="请设置您个人的详细介绍">${userCard.PropertyRemark!}</textarea>
            <div class="ft-gray ft-small">最多不能超过100个字</div><br/><br/>
            <div class="ft-gray ft-small">上传头像</div>
            <input type="file"/><br/><br/>
            <div>
                <a href="http://www.xiajirong.com">温馨提示：申请实名认证，请前往http://www.xiajirong.com（建议在电脑上进行操作）</a>
            </div>
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
