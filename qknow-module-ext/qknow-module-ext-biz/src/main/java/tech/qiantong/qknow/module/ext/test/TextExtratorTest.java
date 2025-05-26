package tech.qiantong.qknow.module.ext.test;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TextExtratorTest {

    public static void main(String[] args) throws IOException {
        try {
            // HTTP 地址
            String fileUrl = "http://localhost:8080/profile/2025/02/19/67b597b38bf4ee5a537db9e2.docx";

            // 创建 URL 对象
            URL url = new URL(fileUrl);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法
            connection.setRequestMethod("GET");

            // 获取输入流
            InputStream inputStream = connection.getInputStream();

            // 使用 XWPFDocument 解析 docx 文件
            XWPFDocument document = new XWPFDocument(inputStream);

            // 获取文档中的段落
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            String text = "";
            // 输出每个段落的文本内容
            for (XWPFParagraph paragraph : paragraphs) {
                System.out.println("-------------" + paragraph.getText());
                text += paragraph.getText();
            }
            System.out.println(text);

            // 关闭输入流
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        try {
//            // 文件 URL 地址（确保是 .doc 文件）
//            String fileUrl = "http://localhost:8080/profile/2025/02/19/67b58ac18bf499192ef1d94b.doc";
//
//            // 创建 URL 对象
//            URL url = new URL(fileUrl);
//
//            // 打开连接
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            // 设置请求方法
//            connection.setRequestMethod("GET");
//            // 获取输入流
//            InputStream inputStream = connection.getInputStream();
//
//            // 使用 HWPFDocument 解析 .doc 文件
//            HWPFDocument document = new HWPFDocument(inputStream);
//
//            // 获取文档中的文本内容
//            Range range = document.getRange();
//
//            // 输出文档的所有文本
//            System.out.println(range.text());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
