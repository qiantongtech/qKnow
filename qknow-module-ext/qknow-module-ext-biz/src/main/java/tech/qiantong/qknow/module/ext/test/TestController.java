//package tech.qiantong.qknow.module.ext.test;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.yaml.snakeyaml.DumperOptions;
//import org.yaml.snakeyaml.Yaml;
//import tech.qiantong.qknow.common.core.domain.AjaxResult;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/test11/test")
//public class TestController {
//
//    @RequestMapping("testaaa")
//    @Test
//    public AjaxResult test() {
//        String yamlUrl = "D:\\qiantong\\DeepKE\\example\\ner\\standard\\conf\\predict.yaml";
//        String text = "桂林的山水被誉为“甲天下”，是世界著名的喀斯特地貌景区，山峰奇特，漓江水清。";
//        //修改python文件内容
//        boolean status = updatePythonYmalContent(yamlUrl, text);
//        if(!status) return AjaxResult.error();
//
//        String batPath = "C:\\Users\\13121\\Desktop\\run_demo.bat";
//        //执行抽取
//        return executeExtraction(batPath);
//    }
//
//    private static AjaxResult executeExtraction(String batPath) {
//        try {
//            StringBuilder stringBuilder = new StringBuilder();
//
//            Process process = Runtime.getRuntime().exec(batPath);
//            final InputStream is1 = process.getInputStream();
//            new Thread(() -> {
//                BufferedReader bufferedReader = null;
//                String line = null;
//                try {
//                    bufferedReader = new BufferedReader(new InputStreamReader(is1, "GBK"));
//                    while ((line = bufferedReader.readLine()) != null) {
//                        stringBuilder.append(line + "\n");
//                        if (line.contains("': '") || line.contains("在句中关系为"))
//                            System.out.println(line);
//                    }
//                    is1.close();
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }).start(); // 启动单独的线程来清空p.getInputStream()的缓冲区;
//
//            InputStream is2 = process.getErrorStream();
//            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
//            StringBuilder buf = new StringBuilder(); // 保存输出结果流
//
//            // 等待进程完成并获取退出码
//            int exitCode = process.waitFor();
//            if (exitCode == 0) {
//                String result = stringBuilder.toString();
//                log.info("执行成功========> {}", result);
////                JSONArray jsonArray = JSONArray.from(result);
//                return AjaxResult.success(result);
//            } else {
//                log.info("执行错误========> {}", exitCode);
//                if (buf.length() > 0) {
//                    log.info("执行错误Error: " + buf.toString());
//                }
//                return AjaxResult.error();
//            }
//
//        } catch (IOException | InterruptedException e) {
//            log.info("知识抽取异常: {}", e);
//            return AjaxResult.error(e.getMessage());
//        }
//    }
//
//    private static boolean updatePythonYmalContent(String yamlUrl,String text) {
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
//            log.info("修改 text 字段的内容异常:{}",e);
//            return false;
//        }
//        return true;
//    }
//
//    public static void main(String[] args) throws IOException {
////        try {
////            // 读取 YAML 文件
//////            File file = new File("D:\\qiantong\\DeepKE\\example\\ner\\standard\\conf\\predict.yaml");
//////            File file = new File("D:\\qiantong\\DeepKE\\example\\triple\\cnschema\\conf\\predict.yaml");
////            File file = new File("D:\\qiantong\\cnschema\\conf\\predict.yaml");
////            Yaml yaml = new Yaml();
////            Map<String, Object> data = yaml.loadAs(new java.io.FileReader(file), Map.class);
////            // 输出原始内容
////            System.out.println("===========输出原始内容=========>" + data);
////            // 修改 text 字段的内容
//////            data.put("text", "布达拉宫是藏传佛教的圣地之一，曾是达赖喇嘛的冬宫，也是西藏的象征。");
////            data.put("text", "歌曲《人生长路》出自刘德华国语专辑《男人的爱》，由李泉作词作曲，2001年出行发版。");
////
//////             将修改后的内容写回文件
////            DumperOptions options = new DumperOptions();
////            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 设置输出格式为块样式
////            Yaml newYaml = new Yaml(options);
////            newYaml.dump(data, new FileWriter(file));
////            System.out.println("修改 text 字段的内容: " + data);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
////        try {
////            StringBuilder stringBuilder = new StringBuilder();
//////            String batPath = "C:\\Users\\13121\\Desktop\\run_demo.bat";
////            String batPath = "C:\\Users\\13121\\Desktop\\run_c.bat";
////            Process process = Runtime.getRuntime().exec(batPath);
////            final InputStream is1 = process.getInputStream();
////            new Thread(() -> {
////                BufferedReader bufferedReader = null;
////                String line = null;
////                try {
////                    bufferedReader = new BufferedReader(new InputStreamReader(is1, "GBK"));
////                    while ((line = bufferedReader.readLine()) != null) {
////                        stringBuilder.append(line + "\n");
//////                        if (line.contains("': '") || line.contains("在句中关系为"))
//////                            System.out.println(line);
////                    }
////                    is1.close();
////                } catch (Exception e) {
////                    // TODO Auto-generated catch block
////                    e.printStackTrace();
////                }
////            }).start(); // 启动单独的线程来清空p.getInputStream()的缓冲区;
////
////            InputStream is2 = process.getErrorStream();
////            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
////            StringBuilder buf = new StringBuilder(); // 保存输出结果流
////
////            // 等待进程完成并获取退出码
////            int exitCode = process.waitFor();
////            if (exitCode == 0) {
////                String result = stringBuilder.toString();
////                System.out.println("执行成功========> " + result);
////                analysis(result);
////            } else {
////                System.out.println("执行错误========> " + exitCode);
////                if (buf.length() > 0) {
////                    System.out.println("Error: " + buf.toString());
////                }
////            }
////
////        } catch (IOException | InterruptedException e) {
////            System.out.println("执行异常========");
////            e.printStackTrace();
////        }
//
//        try {
//            StringBuilder stringBuilder = new StringBuilder();
//            // String batPath = "C:\\Users\\13121\\Desktop\\run_demo.bat";
//            String batPath = "C:\\Users\\13121\\Desktop\\run_c.bat";
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
//                        // 如果输出的内容包含某些特定信息可以在这里进行额外处理
//                        // if (line.contains("': '") || line.contains("在句中关系为")) {
//                        //     System.out.println(line);
//                        // }
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
//                        System.err.println(line);
//                    }
//                    br2.close(); // 关闭标准错误流
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start(); // 启动单独的线程来读取错误输出流
//
//            // 等待子进程结束
//            try {
//                int exitCode = process.waitFor();  // 等待进程结束
//                System.out.println("Process exited with code: " + exitCode);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            // 等待进程完成并获取退出码
//            int exitCode = process.waitFor();
//            if (exitCode == 0) {
//                String result = stringBuilder.toString();
//                System.out.println("执行成功========> " + result);
//                result = result.replace("'","\"");
//                int startIdx = result.indexOf("抽取到的三元组====>");
//                int endIdx = result.indexOf("<====抽取到的三元组");
//                if (startIdx < endIdx) {
//                    String subStr = result.substring(startIdx + 12, endIdx);  // 截取两个双引号之间的内容
//                    System.out.println("========1111===========");
//                    System.out.println(subStr);  // 输出 "sample"
//                }
//
//            } else {
//                System.out.println("执行错误========> " + exitCode);
//            }
//
//        } catch (IOException | InterruptedException e) {
//            System.out.println("执行异常========");
//            e.printStackTrace();
//        }
//    }
//
//
//    public static List<String> analysis(String content) {
//        // 移除字符串中的方括号
//        content = content.replace("[","").replace("]","").replace("'","");
//        // 将字符串按逗号分隔，获得每个元组
//        String[] pairs = content.split("\\), \\(");
//        // 创建一个List来存储所有的实体
//        List<String> entities = new ArrayList<>();
//        StringBuilder currentEntity = new StringBuilder();
//
//        // 遍历每一对词和标签
//        for (String pair : pairs) {
//            // 去掉每个元组的括号，并拆分词和标签
//            pair = pair.replace("(", "").replace(")", "");
//            String[] parts = pair.split(", ");
//            String word = parts[0];
//            String label = parts[1];
//
//            // 判断实体是否属于同一个组合
//            if (label.startsWith("B-")) {
//                // 如果有已经存在的实体，先添加
//                if (currentEntity.length() > 0) {
//                    entities.add(currentEntity.toString());
//                    currentEntity.setLength(0);  // 清空缓存
//                }
//                // 开始一个新的实体
//                currentEntity.append(word);
//            } else if (label.startsWith("I-")) {
//                // 合并当前实体
//                currentEntity.append(word);
//            }
//        }
//
//        // 最后如果有合并的实体，添加到结果
//        if (currentEntity.length() > 0) {
//            entities.add(currentEntity.toString());
//        }
//
//        // 输出最终的实体列表
//        System.out.println("识别出的实体:" + entities.toString());
//        return entities;
//    }
//}
//
//
//
