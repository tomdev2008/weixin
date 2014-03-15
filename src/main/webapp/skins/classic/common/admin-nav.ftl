<nav class="nav">
    <ul class="fn-clear">
        <li>
            <a <#if type == "requirement">class="current" </#if>href="/user-list?type=student">需求</a>
        </li>
        <li>
            <a <#if type == "requirement">class="current" </#if>href="/user-list?type=teacher">消息</a>
        </li>
        <li>
            <a <#if type == "requirement">class="current" </#if>href="/question-list">提问</a>
        </li>
        <li>
            <a <#if type == "requirement">class="current" </#if>href="/question-list">关注</a>
        </li>
        <li>
            <a <#if type == "sale">class="current" </#if>href="/question-list">服务</a>
        </li>
    </ul>
</nav>