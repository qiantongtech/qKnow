package tech.qiantong.qknow.common.utils.csv;

import com.opencsv.CSVWriter;
import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    @Data
    public static class CsvData{
        private Long id;
        private String name;
        private String desc;
    }

    //测试
    public static void main(String[] args) {
        List<CsvData> csvDataList = new ArrayList<>();
        CsvData csvData = new CsvData();
        csvData.setId(Long.valueOf(1));
        csvData.setName("aaaa");
        csvData.setDesc("bbbb");
        csvDataList.add(csvData);
        createCsv(csvDataList,"D:/newfile.csv");
    }

    // 泛型方法，支持任何类型的 CSV 数据
    public static <T> void createCsv(List<T> dataList, String createPath) {
        try {
            // 生成 CSV 文件内容
            List<String[]> rows = new ArrayList<>();

            // 获取字段名作为标题行
            if (!dataList.isEmpty()) {
                Field[] fields = dataList.get(0).getClass().getDeclaredFields();
                String[] title = new String[fields.length];

                for (int i = 0; i < fields.length; i++) {
                    title[i] = fields[i].getName();  // 使用字段名作为列标题
                }
                rows.add(title);  // 添加标题行
            }

            // 遍历数据并生成每一行
            for (T data : dataList) {
                Field[] fields = data.getClass().getDeclaredFields();
                String[] row = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);  // 允许访问私有字段
                    Object value = fields[i].get(data);  // 获取字段值
                    row[i] = value != null ? value.toString() : "";  // 处理 null 值
                }
                rows.add(row);  // 添加数据行
            }

            // 确保文件夹存在
            Path path = Paths.get(createPath).getParent();  // 获取文件的父文件夹路径
            if (!Files.exists(path)) {
                Files.createDirectories(path);  // 如果文件夹不存在，则创建
            }

            // 创建 CSVWriter 对象并写入数据，不使用双引号
            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(createPath),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,  // 禁用双引号
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END)) {
                // 这里的 rows 是你的数据数组
                csvWriter.writeAll(rows);  // 将数据写入 CSV 文件
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("生成 CSV 文件异常: " + e.getMessage());
        }
    }

}
