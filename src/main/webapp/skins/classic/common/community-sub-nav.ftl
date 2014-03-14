<div class="sub-nav">
    <ul class="fn-clear">
        <li>
            <a href="<#if type='sale'>sale<#else>requirement</#if>-list?type=1" 
               <#if subType == "1">class="current"</#if>>资料</a>
        </li>
        <li>
            <a href="<#if type='sale'>sale<#else>requirement</#if>-list?type=2"
               <#if subType == "2">class="current"</#if>>答疑</a>
        </li>
        <li>
            <a href="<#if type='sale'>sale<#else>requirement</#if>-list?type=3"
               <#if subType == "3">class="current"</#if>>授课</a>
        </li>
        <li style="text-align: right">
            <a class="last" href="/requirement-publish">发需求</a>&nbsp;
        </li>
        <li>
            <a class="last" href="/sale-publish">发出售</a>
        </li>
    </ul>
</div>