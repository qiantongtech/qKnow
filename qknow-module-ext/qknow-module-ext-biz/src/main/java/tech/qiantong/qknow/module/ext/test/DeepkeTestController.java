//package tech.qiantong.qknow.module.ext.test;
//
//import com.opencsv.CSVWriter;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Value;
//import tech.qiantong.qknow.module.ext.dal.dataobject.extSchema.ExtSchemaDO;
//import tech.qiantong.qknow.module.ext.dal.dataobject.extSchemaRelation.ExtSchemaRelationDO;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//public class DeepkeTestController {
//    @Value("${deepke.relatinCsv}")
//    private String relatinCsv;
//
//    @Test
//    public void test1(List<ExtSchemaRelationDO> relationDOS){
//        try {
//            List<ExtSchemaDO> schemaAllList = new ArrayList<>();
//            List<String[]> rows = new ArrayList<>();
//            // 修改数据，忽略前两行
//            for (int i = 0; i < schemaAllList.size() + 2; i++) {
//                // 修改数据
//                String[] row = new String[4];
//                if (i == 0) {
//                    row[0] = "head_type";     // 修改第一列
//                    row[1] = "tail_type";     // 修改第二列
//                    row[2] = "ralation";      // 修改第三列
//                    row[3] = "index";         // 修改第四列
//                } else if (i == 1) {
//                    row[0] = "None"; // 修改第一列
//                    row[1] = "None";      // 修改第二列
//                    row[2] = "None";      // 修改第三列
//                    row[3] = "0"; // 修改第四列
//                } else {
//                    ExtSchemaRelationDO relationDO = relationDOS.get(i - 2);
//                    String head = "";
//                    String tail = "";
//                    for (ExtSchemaDO extSchemaDO : schemaAllList) {
//                        if (relationDO.getStartSchemaId().equals(extSchemaDO.getId())) {
//                            head = extSchemaDO.getName();
//                        }
//                        if (relationDO.getEndSchemaId().equals(extSchemaDO.getId())) {
//                            tail = extSchemaDO.getName();
//                        }
//                    }
//                    row[0] = head; // 修改第一列
//                    row[1] = tail;      // 修改第二列
//                    row[2] = relationDO.getRelation();      // 修改第三列
//                    row[3] = "" + (i - 1); // 修改第四列
//                }
//                rows.add(row);
//            }
//            // 写回原始CSV文件，覆盖原文件
//            CSVWriter csvWriter = new CSVWriter(new FileWriter(relatinCsv));
//            csvWriter.writeAll(rows);
//            csvWriter.close();
//            log.info("-------修改关系配置CSV成功!------");
//        } catch (IOException e) {
//            log.info("修改关系Schema异常:{}", e);
//            throw new RuntimeException("修改关系Schema异常" + e.getMessage());
//        }
//    }
//}
