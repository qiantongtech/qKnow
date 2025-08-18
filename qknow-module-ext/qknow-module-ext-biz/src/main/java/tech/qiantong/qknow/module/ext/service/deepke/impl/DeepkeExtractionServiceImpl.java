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
