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
    //文字段落相关
    @Resource
    private ExtUnstructTaskTextMapper extUnstructTaskTextMapper;
    @Resource
    private ExtNeo4jService kmcNeo4jService;

    @Value("${deepke.batPath}")
    private String batPath;

    @Value("${deepke.predictYaml}")
    private String predictYaml;

    @Value("${deepke.startShPath}")
    private String startShPath;

    @Value("${spring.profiles.active}")
    private String active;

    //测试运行.sh脚本
    public static void main(String[] args) {
        try {
            ProcessBuilder processBuilder;
            processBuilder = new ProcessBuilder("wsl", "/mnt/c/Users/13121/startDeepKE.sh","歌曲《我怀念的》收录在王菲的专辑《菲靡靡之音》中，由林夕作词，王菲");
            processBuilder.redirectErrorStream(true); // 合并标准错误流到标准输出流
            Process process = processBuilder.start();

            // 读取输出流
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String result = "";
            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);  // 输出脚本的执行结果
                if(line.contains("抽取到的三元组") || line.contains("抽取到的实体")){
                    result += line;
                }
            }
            System.out.println(result);

            // 等待进程结束并获取退出码
            int exitCode = process.waitFor();
            System.out.println("退出码: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 执行知识抽取
     *
     * @param
     * @return
     */
    @Override
    public AjaxResult deepkeExtraction(String text) {
        if (StringUtils.isBlank(text)) {
            return AjaxResult.error();
        }

        //通过脚本执行脚本启动docker
        try {
            ProcessBuilder processBuilder;
            if("dev".equals(active)){
                // Windows环境使用WSL路径
                processBuilder = new ProcessBuilder("wsl", startShPath, text);
            }else {
                // Linux环境使用原生路径
                processBuilder = new ProcessBuilder("bash", startShPath, text); // 或直接执行脚本
            }
//            processBuilder = new ProcessBuilder("wsl", "/mnt/c/Users/13121/startDeepKE.sh","歌曲《我怀念的》收录在王菲的专辑《菲靡靡之音》中，由林夕作词，王菲");
            processBuilder.redirectErrorStream(true); // 合并标准错误流到标准输出流
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
            }else {
                return AjaxResult.error("抽取错误" + exitCode);
            }

        } catch (Exception e) {
            log.info("抽取异常:{}", e);
            return AjaxResult.error("抽取异常" + e.getMessage());
        }

// 原始的, 直接本地调用deepke代码的,也可以用
//        boolean status = updatePythonYmalContent(predictYaml, text);
//        if (!status) {
//            return AjaxResult.error();
//        }
//        try {
//            StringBuilder stringBuilder = new StringBuilder();
//            //执行脚本 运行deepke
//            Process process = Runtime.getRuntime().exec(batPath);
//
//            final InputStream is1 = process.getInputStream();
//            new Thread(() -> {
//                BufferedReader bufferedReader = null;
//                String line = null;
//                try {
//                    bufferedReader = new BufferedReader(new InputStreamReader(is1, "GBK"));
//                    while ((line = bufferedReader.readLine()) != null) {
//                        stringBuilder.append(line).append("\n");
//                    }
//                    bufferedReader.close(); // 确保关闭BufferedReader
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start(); // 启动单独的线程来清空p.getInputStream()的缓冲区;
//
//            InputStream is2 = process.getErrorStream();
//            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
//            new Thread(() -> {
//                String line = null;
//                try {
//                    while ((line = br2.readLine()) != null) {
//                        stringBuilder.append(line).append("\n");
//                        // 您可以根据需求打印标准错误流的内容
//                    }
//                    br2.close(); // 关闭标准错误流
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start(); // 启动单独的线程来读取错误输出流
//
//            // 等待进程完成并获取退出码
//            int exitCode = process.waitFor();
//            if (exitCode == 0) {
//                String result = stringBuilder.toString();
////                log.info("====原始文本数据{}, deepke打印的抽取信息====> {}", extractionDTO.getText(), result);
//                return AjaxResult.success("抽取成功",result);
//            } else {
////                log.info("========抽取失败========> {}", exitCode);
//                return AjaxResult.error("抽取错误" + exitCode);
//            }
//
//        } catch (IOException | InterruptedException e) {
//            log.info("抽取异常:{}", e);
//            return AjaxResult.error("抽取异常" + e.getMessage());
//        }
    }

//    /**
//     * 修改python项目中yaml文件内容
//     *
//     * @param yamlUrl
//     * @param text
//     * @return
//     */
//    private boolean updatePythonYmalContent(String yamlUrl, String text) {
//        try {
//            // 读取 YAML 文件
//            File file = new File(yamlUrl);
//            Yaml yaml = new Yaml();
//            Map<String, Object> data = yaml.loadAs(new FileReader(file), Map.class);
//            // 修改 text 字段的内容
//            data.put("text", text);
//            // 将修改后的内容写回文件
//            DumperOptions options = new DumperOptions();
//            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 设置输出格式为块样式
//            Yaml newYaml = new Yaml(options);
//            newYaml.dump(data, new FileWriter(file));
//        } catch (IOException e) {
//            log.info("修改 text 字段的内容异常:{}", e);
//            return false;
//        }
//        return true;
//    }
}
