/*
* 版权声明 Copyright (c) ${.now?string["yyyy"]}。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： ${.now?string["yyyy年M月d日"]}
*/
<#assign start = 3>
<#assign end = controllerMappingHyphen?length-8>
<#assign capName = "">
<#assign unCapName = "">
<#assign items = controllerMappingHyphen[start..end]?split("-")>
<#list items as item>
    <#assign capName = capName + item?cap_first>
    <#if unCapName?length == 0>
        <#assign unCapName = item?uncap_first>
    <#else>
        <#assign unCapName = unCapName + item?cap_first>
    </#if>
</#list>
<#assign perfix = table.controllerName?substring(0,2)>
package ${package.Controller};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import com.qkplm.clinic.clinicserver.service.I${perfix + capName}Service;
import com.qkplm.clinic.clinicserver.entity.${perfix + capName}Entity;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.annotation.BQLogMark;
import com.qkplm.clinic.libcommon.api.BQApiException;
import com.qkplm.clinic.libcommon.api.BQSearchParams;
import com.qkplm.clinic.libcommon.api.BQSearchParamsGet;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author ${author}
 * @description <p>${table.comment!} 前端控制器</p>
 * @datetime ${.now?string["yyyy-M-d H:m"]}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/ams/api/v1/<#if controllerMappingHyphenStyle>${items?join("/")}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    private final static String MODULE_NAME = "${table.comment!}";
    private final static String TAG_NAME = "${unCapName}";
    private final I${perfix + capName}Service ${unCapName}Service;

    public ${table.controllerName}(I${perfix + capName}Service ${unCapName}Service) {
        this.${unCapName}Service = ${unCapName}Service;
    }

    /**
    * 根据ID获取单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "查询")
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ${perfix + capName}Entity get(@PathVariable String id) {
        ${perfix + capName}Entity ${unCapName} = ${unCapName}Service.getById(id);
        if (${unCapName} == null) {
            throw new BQApiException("获取失败，对象可能不存在");
        }
        return ${unCapName};
    }

    /**
    * 新增单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ${perfix + capName}Entity save(@RequestBody ${perfix + capName}Entity ${unCapName}) {
        ${unCapName}.setEid(null);
        if (!${unCapName}Service.save(${unCapName})) {
            throw new BQApiException("新增失败");
        }
        return ${unCapName};
    }

    /**
    * 批量新增记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量新增")
    @RequestMapping(value = "/saveBatch", method = RequestMethod.POST)
    public Boolean saveBatch(@RequestBody Collection<${perfix + capName}Entity> ${unCapName}s) {
        if (!${unCapName}Service.saveBatch(${unCapName}s)) {
            throw new BQApiException("批量新增失败");
        }
        return true;
    }

    /**
    * 根据ID更新单条记录
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ${perfix + capName}Entity update(@RequestBody ${perfix + capName}Entity ${unCapName}) {
        if (!${unCapName}Service.updateById(${unCapName})) {
            throw new BQApiException("更新失败，对象可能已失效");
        }
        return ${unCapName};
    }

    /**
    * 批量更新记录，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量更新")
    @RequestMapping(value = "/updateBatch", method = RequestMethod.POST)
    public Boolean updateBatch(@RequestBody Collection<${perfix + capName}Entity> ${unCapName}s) {
        if (!${unCapName}Service.updateBatchById(${unCapName}s)) {
            throw new BQApiException("批量更新失败");
        }
        return true;
    }


    /**
    * 根据ID删除单条记录，物理删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public Boolean delete(@PathVariable String id) {
        if (!${unCapName}Service.removePhysicalById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录,物理删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    public Boolean deleteBatch(@RequestBody Collection<Serializable> ids) {
        if (!${unCapName}Service.removePhysicalBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 根据ID删除单条记录，逻辑删除
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "删除")
    @RequestMapping(value = "/deleteLogic/{id}", method = RequestMethod.GET)
    public Boolean deleteLogic(@PathVariable String id) {
        if (!${unCapName}Service.removeById(id)) {
            throw new BQApiException("删除失败，对象可能不存在");
        }
        return true;
    }

    /**
    * 批量删除记录，逻辑删除，默认单次执行1000条
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "批量删除")
    @RequestMapping(value = "/deleteLogicBatch", method = RequestMethod.POST)
    public Boolean deleteLogicBatch(@RequestBody Collection<Serializable> ids) {
        if (!${unCapName}Service.removeBatchByIds(ids)) {
            throw new BQApiException("批量删除失败");
        }
        return true;
    }

    /**
    * 列表查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Iterable<${perfix + capName}Entity> list(BQSearchParamsGet<${perfix + capName}Entity> paramsGet) {
        BQSearchParams<${perfix + capName}Entity> params = paramsGet.toSearchParams();
        return ${unCapName}Service.list(params.toPage(), params.toWrapper());
    }

    /**
    * 分页查询，orders可以排序，filters可以过滤
    */
    @BQAuthMark(tag = TAG_NAME, buttons = {})
    @BQLogMark(module = MODULE_NAME, operation = "分页查询")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<${perfix + capName}Entity> page(BQSearchParamsGet<${perfix + capName}Entity> paramsGet) {
        BQSearchParams<${perfix + capName}Entity> params = paramsGet.toSearchParams();
        return ${unCapName}Service.page(params.toPage(), params.toWrapper());
    }
}
</#if>
