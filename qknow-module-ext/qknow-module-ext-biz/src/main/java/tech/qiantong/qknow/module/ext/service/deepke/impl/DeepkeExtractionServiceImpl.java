/*
 * Copyright © 2026 Qiantong Technology Co., Ltd.
 * qKnow Knowledge Platform
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qknow.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2026 江苏千桐科技有限公司
 * qKnow 知识平台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qknow.qiantong.tech/business.html
 */

package tech.qiantong.qknow.module.ext.service.deepke.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import tech.qiantong.qknow.common.core.domain.AjaxResult;
import tech.qiantong.qknow.common.utils.StringUtils;
import tech.qiantong.qknow.module.ext.dal.mapper.extUnstructTaskText.ExtUnstructTaskTextMapper;
import tech.qiantong.qknow.module.ext.service.deepke.DeepkeExtractionService;
import tech.qiantong.qknow.module.ext.service.neo4j.service.ExtNeo4jService;

import javax.annotation.Resource;
import java.io.*;
import java.util.Map;

/**
 * 知识抽取
 */
@Slf4j
@Service
public class DeepkeExtractionServiceImpl implements DeepkeExtractionService {

    /**
     * 执行知识抽取
     *
     * @param
     * @return
     */
    @Override
    public AjaxResult deepkeExtraction(String text) throws Exception {
        if (StringUtils.isBlank(text)) {
            return AjaxResult.error();
        }

        ProcessBuilder processBuilder;

        // 获取当前工作目录（即项目根目录）
        String projectRoot = new File("").getCanonicalPath();
        // 构建 bin/start.sh 路径
        String startShPath = new File(projectRoot, "bin/DeepKE/start.sh").getCanonicalPath();

        // 根据当前运行环境判断脚本执行命令
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            // Windows 环境下使用 WSL
            processBuilder = new ProcessBuilder("wsl", startShPath, text);
        } else {
            // 假设为 Linux 或其他类 Unix 系统
            processBuilder = new ProcessBuilder("bash", startShPath, text);
        }

        // 合并标准错误流到标准输出流
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // 读取输出流
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String result = "";
        String line;
        while ((line = reader.readLine()) != null) {
            if(line.contains("抽取到的三元组") || line.contains("抽取到的实体")){
                result += line;
            }
        }
        // 等待进程结束并获取退出码
        int exitCode = process.waitFor();
        if (exitCode == 0){
            return AjaxResult.success("抽取成功", result);
        } else {
            return AjaxResult.error("抽取错误" + exitCode);
        }
    }
}
