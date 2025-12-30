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
    /**
     * 泛型方法，支持任何类型的 CSV 数据
     * @author shaun
     * @date 2025/06/13 16:01
     * @param dataList
     * @param createPath
     * @return void
     */
    public static <T> void createCsv(List<T> dataList, String createPath) {
        try {
            // 生成 CSV 文件内容
            List<String[]> rows = new ArrayList<>();

            // 获取字段名作为标题行
            if (!dataList.isEmpty()) {
                Field[] fields = dataList.get(0).getClass().getDeclaredFields();
                String[] title = new String[fields.length];

                for (int i = 0; i < fields.length; i++) {
                    // 使用字段名作为列标题
                    title[i] = fields[i].getName();
                }
                // 添加标题行
                rows.add(title);
            }

            // 遍历数据并生成每一行
            for (T data : dataList) {
                Field[] fields = data.getClass().getDeclaredFields();
                String[] row = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    // 允许访问私有字段
                    fields[i].setAccessible(true);
                    // 获取字段值
                    Object value = fields[i].get(data);
                    // 处理 null 值
                    row[i] = value != null ? value.toString() : "";
                }
                // 添加数据行
                rows.add(row);
            }

            // 获取文件的父文件夹路径，确保文件夹存在
            Path path = Paths.get(createPath).getParent();
            if (!Files.exists(path)) {
                // 如果文件夹不存在，则创建
                Files.createDirectories(path);
            }

            // 创建 CSVWriter 对象并写入数据，不使用双引号
            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(createPath),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END)) {
                // 这里的 rows 是你的数据数组，将数据写入 CSV 文件
                csvWriter.writeAll(rows);
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("生成 CSV 文件异常: " + e.getMessage());
        }
    }

}
