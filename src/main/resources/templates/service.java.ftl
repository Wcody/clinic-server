/*
* 版权声明 Copyright (c) ${.now?string["yyyy"]}。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： ${.now?string["yyyy年M月d日"]}
*/

package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
* @author ${author}
* @description <p>${table.comment!} 服务类</p>
* @datetime ${.now?string["yyyy-M-d H:m"]}
*/
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

}
</#if>
