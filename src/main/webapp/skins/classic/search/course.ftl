<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="专业搜索 - 新助邦"/>
    </head>
    <body class="search">
        <nav>
            <ul class="fn-clear">
                <li <#if type==1>class="current"</#if>>
                    <span>
                        学科门类<span class="ico ico-arrow-down"></span>
                    </span>
                </li>
                <li <#if type==2>class="current"</#if>>
                    <span>
                        一级学科<span class="ico ico-arrow-down"></span>
                    </span>
                </li> 
                <li <#if type==3>class="current"</#if>>
                    <span>
                        二级学科<span class="ico ico-arrow-down"></span>
                    </span>
                </li>
            </ul>
        </nav>

        <#if list??>
            <ul class="list">
                <#list list as l>            
                    <li>
                        <#if type!=3><a href="/search?<#if type==1>id=${l.ID}</#if><#if type==2>id=${l.MajorCode}</#if>&type=${type}"></#if>
                            <#if type==1>${l.MajorName}</#if>
                            <#if type==2>${l.MajorName}</#if>
                            <#if type==3>${l.Name}</#if>
                        <#if type!=3></a></#if>
                    </li>
                </#list>
            </ul>
        </#if>
    </body>
</html>