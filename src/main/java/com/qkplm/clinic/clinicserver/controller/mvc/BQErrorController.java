/*
 * 版权声明 Copyright (c) 2024-2026。
 * 版权所有者： [全科医生系统V2.1]
 * 首创日期： 2026年4月8日
 */

package com.qkplm.clinic.clinicserver.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author: Wcke
 * @description:
 * @datetime: 2024-06-14 17:14
 */
@Controller
public class BQErrorController {
    @GetMapping("/error")
    public ModelAndView error(Model model) {
        return new ModelAndView("error", model.asMap());
    }
}
