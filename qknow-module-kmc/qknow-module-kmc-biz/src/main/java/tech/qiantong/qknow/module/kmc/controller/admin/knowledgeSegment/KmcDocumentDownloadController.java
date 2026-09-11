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

package tech.qiantong.qknow.module.kmc.controller.admin.knowledgeSegment;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qknow.module.kmc.service.knowledgeSegment.IKmcDocumentSegmentService;
import tech.qiantong.qknow.module.kmc.service.knowledgeSegment.bo.DownloadJsonConfigBO;
import tech.qiantong.qknow.redis.service.IRedisService;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;

@RestController
@RequestMapping("/docs/kmcDocument")
public class KmcDocumentDownloadController {

    @Resource
    private IRedisService redisService;
    @Resource
    private IKmcDocumentSegmentService kmcDocumentSegmentService;
    @Resource
    private ObjectMapper objectMapper;

    private static final String FILE_NAME_FORMAT_JSON = "document_{}.json";
    private static final String FILE_NAME_FORMAT_JSONL = "document_{}.jsonl";

    /**
     * 原生a标签访问的流式下载接口
     */
    @GetMapping("/streamDownload")
    @ResponseBody
    public void streamDownload(@RequestParam String fileId, HttpServletResponse response) throws Exception {
        String redisKey = StrUtil.format(kmcDocumentSegmentService.DOWNLOAD_FILE_ID_FORMAT, fileId);
        DownloadJsonConfigBO configBO = JSONObject.parseObject(redisService.get(redisKey), DownloadJsonConfigBO.class);
        if (StrUtil.isBlank(fileId) || Objects.isNull(configBO)) {
            response.setStatus(401);
            response.getWriter().write("链接失效或无权限");
            return;
        }

        long nowSec = Instant.now().getEpochSecond();
        String fileName;
        if (Objects.equals(configBO.getFileType(),"json")){
            fileName = StrUtil.format(FILE_NAME_FORMAT_JSON, nowSec);
        }else {
            fileName = StrUtil.format(FILE_NAME_FORMAT_JSONL, nowSec);
        }

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=\"" + fileName + "\";filename*=UTF-8''" + encodedFileName);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");

        try (OutputStream os = response.getOutputStream();
             JsonGenerator generator = objectMapper.createGenerator(os)) {

            if (Objects.equals(configBO.getFileType(),"json")){
//                generator.useDefaultPrettyPrinter();
                generator.writeStartArray();
                generator.writeRaw("\n");
            }

            long pageIndex = 1;
            int batchSize = 200; // 每批读取1000条，根据业务调整

            // 循环分页读取数据
            while (true) {
                IPage<JSONObject> page = new Page<>(pageIndex, batchSize);
                IPage<JSONObject> batchList = kmcDocumentSegmentService.queryDownloadPage(page,configBO);
                if (CollUtil.isEmpty(batchList.getRecords())) {
                    break;
                }
                pageIndex++;
                for (JSONObject item : batchList.getRecords()) {
                    generator.writeObject(item);
                    generator.writeRaw("\n");
                }
            }
            if (Objects.equals(configBO.getFileType(),"json")){
                generator.writeEndArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  一次性签名使用后立即删除，防止重复下载
        redisService.delete(redisKey);
    }
}
