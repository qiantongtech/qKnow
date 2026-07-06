/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qKnow Intelligent Agent Building Platform (Open Source Edition).
 *
 * qKnow is licensed under Apache License 2.0 with additional qKnow terms.
 * You may use qKnow for commercial purposes, but you may not remove, hide,
 * modify, or replace the qKnow logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qKnow as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qknow.ai/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qknow.module.ext.controller.admin.extraction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qknow.module.ext.service.deepke.DeepkeExtractionService;
import tech.qiantong.qknow.module.ext.service.neo4j.service.ExtNeo4jService;

import jakarta.annotation.Resource;


/**
 * 知识抽取
 */
@Slf4j
@RestController
@RequestMapping("/extExtraction")
public class ExtExtractionController {
    @Resource
    private DeepkeExtractionService kmcExtractionService;
    @Resource
    private ExtNeo4jService kmcNeo4jService;

//    /**
//     * 三元组抽取
//     *
//     * @return
//     */
//    @PostMapping("/extraction")
//    public AjaxResult extraction(@RequestBody ExtExtractionDTO extractionDTO) {
//        return kmcExtractionService.extraction(extractionDTO);
//    }


//    @GetMapping("/getByName")
//    public AjaxResult getByName(String name) {
//        return kmcNeo4jService.getByName(name);
//    }
}
