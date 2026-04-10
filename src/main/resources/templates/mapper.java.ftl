/*
* 版权声明 Copyright (c) ${.now?string["yyyy"]}。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： ${.now?string["yyyy年M月d日"]}
*/

package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
<#if mapperAnnotationClass??>
import ${mapperAnnotationClass.name};
</#if>

/**
* @author ${author}
* @description <p>${table.comment!} 映射器</p>
* @datetime ${.now?string["yyyy-M-d H:m"]}
*/
<#if mapperAnnotationClass??>
@${mapperAnnotationClass.simpleName}
</#if>
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
