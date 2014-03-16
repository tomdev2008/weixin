<nav class="nav">
    <ul class="fn-clear">
        <li>
            <a <#if type == "student">class="current" </#if>href="/user-list?type=student">学生</a>
        </li>
        <li>
            <a <#if type == "teacher">class="current" </#if>href="/user-list?type=teacher">老师</a>
        </li>
        <li>
            <a <#if type == "question">class="current" </#if>href="/question-list?type=1">提问</a>
        </li>
        <li>
            <a <#if type == "requirement">class="current" </#if>href="/requirement-list">需求</a>
        </li>
        <li>
            <a <#if type == "sale">class="current" </#if>href="/sale-list">出售</a>
        </li>
    </ul>
</nav>