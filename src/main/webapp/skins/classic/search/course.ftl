<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="专业搜索 - 新助邦"/>
    </head>
    <body class="search">
        <header><a href="/search">重新搜索</a></header>
        <nav>
            <ul class="fn-clear">
                <li <#if type==1>class="current"</#if>>
                    <span onclick="window.location = '/search'">
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
        <ul class="list">
            <#list list as l>            
            <li>
                <#if type == 1><a href="/search?id=${l.ID?c}&type=${type}">${l.MajorName}</a></#if>
                <#if type == 2><a href="/search?id=${l.MajorCode}&type=${type}">${l.MajorName}</a></#if>
                <#if type == 3><a href="/search?id=${l.MajorCode}&type=${type}">${l.Name}</a></#if>
                <#if type == 4>${l.Name}</#if>
            </li>
            </#list>
        </ul>
    </body>
</html>