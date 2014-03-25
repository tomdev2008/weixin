<header>XXXXXXXXXXXXXXXXXX</header>
<nav class="nav">
    <ul class="fn-clear">
        <li>
            <a <#if type == "requirement">class="current" </#if>href="/admin/requirement-list">需求</a>
        </li>
        <li>
            <a <#if type == "message">class="current" </#if>href="/admin/message-list">消息</a>
        </li>
        <li>
            <a <#if type == "question">class="current" </#if>href="/admin/question-list">提问</a>
        </li>
        <li>
            <a <#if type == "follow">class="current" </#if>href="/admin/follow-list">关注</a>
        </li>
        <li>
            <a <#if type == "sale">class="current" </#if>href="/admin/sale-list">服务</a>
        </li>
    </ul>
</nav>