/*
* 版权声明 Copyright (c) ${.now?string["yyyy"]}。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： ${.now?string["yyyy年M月d日"]}
*/

package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
<#if generateService>
import ${package.Service}.${table.serviceName};
</#if>
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
* @author ${author}
* @description <p>${table.comment!} 服务接口类</p>
* @datetime ${.now?string["yyyy-M-d H:m"]}
*/
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>()<#if generateService>, ${table.serviceName}</#if> {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}><#if generateService> implements ${table.serviceName}</#if> {

}
</#if>
