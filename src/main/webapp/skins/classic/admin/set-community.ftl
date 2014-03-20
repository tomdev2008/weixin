<!DOCTYPE html>
<html>
    <head>
        <#include "../common/macro-meta.ftl">
        <@meta title="圈子设置 - 新助邦"/>
    </head>
    <body class="search">
        <nav>
            <ul class="fn-clear">
                <li <#if type==0>class="current"</#if> >
                    <span>
                        省份<span class="ico ico-arrow-down"></span>
                    </span>
                </li>
                <li <#if type==1>class="current"</#if>>
                    <span>
                        学校<span class="ico ico-arrow-down"></span>
                    </span>
                </li> 
                <li <#if type==2>class="current"</#if>>
                    <span>
                        学院<span class="ico ico-arrow-down"></span>
                    </span>
                </li>
            </ul>
        </nav>
        <ul class="list">
            <#if  provinces??>  
            <#list provinces as p>
            <li>
               <a href='/admin/set-community?<#if type==0>province_id=${p.Did}</#if><#if type==1>province_id=${provinceId}&school_id=${p.Did}</#if><#if type==2>province_id=${provinceId}&school_id=${schoolId}&college_id=${p.Did}</#if>'> ${p.Name}</a>
            </li>
            </#list>
            </#if>
        </ul>
    </body>
</html>
