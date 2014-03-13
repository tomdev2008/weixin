<nav class="nav">
    <ul>
        <li>
            <a href="/user-list?type=student">学生</a>
        </li>
        <li>
            <a href="/user-list?type=teacher">老师</a>
        </li>
        <li>
            <a href="/question-list">提问</a>
        </li>
        <li>
            <a href="/requirement-list">需求</a>
        </li>
        <li>
            <a <#if type == "sale">class="current"</#if>href="/sale-list">出售</a>
        </li>
    </ul>
</nav>