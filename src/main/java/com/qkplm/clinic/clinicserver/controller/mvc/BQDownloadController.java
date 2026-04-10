/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.controller.mvc;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.qkplm.clinic.clinicserver.entity.TbAttachmentEntity;
import com.qkplm.clinic.clinicserver.entity.TbMenuEntity;
import com.qkplm.clinic.clinicserver.entity.TbCustomerEntity;
import com.qkplm.clinic.clinicserver.service.ITbAttachmentService;
import com.qkplm.clinic.clinicserver.service.ITbMenuService;
import com.qkplm.clinic.clinicserver.service.ITbCustomerService;
import com.qkplm.clinic.libcommon.annotation.BQAuthMark;
import com.qkplm.clinic.libcommon.utils.BQJacksonUtils;
import com.qkplm.clinic.libcommon.utils.BQRSAUtils;
import com.qkplm.clinic.libcommon.utils.BQUploadHandlerUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

/**
 * @author: Wcke
 * @description: 下载控制器
 * @datetime: 2024-07-07 21:17
 */

@Controller
@RequestMapping("ams/mvc/v1/download")
public class BQDownloadController {
    private final ITbCustomerService customerService;
    private final ITbMenuService menuService;
    private final ITbAttachmentService attachmentService;

    public BQDownloadController(ITbCustomerService customerService, ITbMenuService menuService, ITbAttachmentService attachmentService) {
        this.customerService = customerService;
        this.menuService = menuService;
        this.attachmentService = attachmentService;
    }

    private void outFile(HttpServletResponse response, File file) throws IOException {
        if (file.exists()) {
            // 获取文件名后缀
            String suffix = file.getName().substring(file.getName().lastIndexOf(".")).toLowerCase();
            // 根据后缀名推测类型
            String type = switch (suffix) {
                case ".jpg", ".jpeg" -> "image/jpeg";
                case ".png" -> "image/png";
                case ".gif" -> "image/gif";
                default -> "application/octet-stream";
            };
            response.setContentType(type);
            byte[] bytes = Files.readAllBytes(file.toPath());
            response.setStatus(200);
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } else {
            response.setStatus(404);
            response.getWriter().write("模板图片不存在");
        }
    }

    /**
     * 下载PDF
     */
    @BQAuthMark(needGrant = false)
    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public void pdf(HttpServletRequest request, HttpServletResponse response) {
    }

    /**
     * 下载图片
     */
    @BQAuthMark(needGrant = false)
    @RequestMapping(value = "/img", method = RequestMethod.GET)
    public void img(HttpServletRequest request, HttpServletResponse response) {
    }

    /**
     * 下载租户授权文件
     */
    @BQAuthMark(needGrant = false)
    @RequestMapping(value = "/auth/{customerId}", method = RequestMethod.GET)
    public void auth(@PathVariable String customerId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbCustomerEntity entity = customerService.getById(customerId);
        if (Objects.isNull(entity)) {
            response.setStatus(404);
            response.getWriter().write("租户不存在");
            return;
        }

        // 当前租户信息
        ObjectNode json = BQJacksonUtils.newObjectNode();
        json.putPOJO("customer", entity);

        // 获取租户可用菜单
        List<String> menuIds = customerService.getMenuIdsBy(customerId);
        if (Objects.isNull(menuIds) || menuIds.isEmpty()) {
            response.setStatus(404);
            response.getWriter().write("没有授权任何可以访问的菜单");
            return;
        }
        List<TbMenuEntity> menus = menuService.listByIds(menuIds);
        json.putPOJO("menus", menus);

        //返回授权加密二进制文件
        response.setHeader("Content-Disposition", "attachment;filename=" + customerId + ".bqa");
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");

        // 设置内容
        String content = BQJacksonUtils.toJson(json);
        byte[] bytes = BQRSAUtils.encryptWithPrivateKeyToBytes(content, BQRSAUtils.getPrivateKeyFile().toString());
        response.setStatus(200);
        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }

    /**
     * 模板图片
     */
    @BQAuthMark(needGrant = false)
    @RequestMapping(value = "/template/{year}/{month}/{day}/{filename}", method = RequestMethod.GET)
    public void template(HttpServletResponse response, @PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String filename) throws IOException {
        String subPath = String.format("/%s/%s/%s/%s", year, month, day, filename);
        File file = BQUploadHandlerUtils.getTemplateFileBySubPath(subPath);
        outFile(response, file);
    }

    /**
     * 资料图片
     */
    @BQAuthMark(needGrant = false)
    @RequestMapping(value = "/medical/{attachmentId}", method = RequestMethod.GET)
    public void medical(HttpServletResponse response, @PathVariable String attachmentId) throws IOException {
        TbAttachmentEntity entity = attachmentService.getById(attachmentId);
        File file = BQUploadHandlerUtils.getMedicalFileBySubPath(entity.getFilePath());
        outFile(response, file);
    }

    /**
     * 常用图片
     */
    @BQAuthMark(needGrant = false)
    @RequestMapping(value = "/images/{year}/{month}/{day}/{filename}", method = RequestMethod.GET)
    public void images(HttpServletResponse response, @PathVariable String year, @PathVariable String month, @PathVariable String day, @PathVariable String filename) throws IOException {
        String subPath = String.format("/%s/%s/%s/%s", year, month, day, filename);
        File file = BQUploadHandlerUtils.getUploadImageBySubPath(subPath);
        outFile(response, file);
    }
}
