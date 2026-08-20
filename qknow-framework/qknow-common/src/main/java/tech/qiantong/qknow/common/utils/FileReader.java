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

package tech.qiantong.qknow.common.utils;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件读取
 * @author wang
 * @date 2025/10/20 14:07
 **/
public class FileReader {

    private static final Tika TIKA = new Tika();
    // 解析器（单例模式）
    private static final AutoDetectParser PARSER = new AutoDetectParser();

    public static String safeReadFileMarkdown(String path) throws TikaException, IOException, SAXException {
        InputStream stream = new FileInputStream(path);

        Metadata metadata = new Metadata();
        // 生成 XHTML
        ToXMLContentHandler handler = new ToXMLContentHandler();
        PARSER.parse(stream, handler, metadata);

        MutableDataSet options = new MutableDataSet();
        options.setFrom( new MutableDataSet());

        return FlexmarkHtmlConverter.builder(options).build().convert(handler.toString());
    }

    /**
     * 读取文件内容，并返回字符串
     * @param path
     * @return
     */
    public static String safeReadFile(String path) throws TikaException, IOException {
        File file = new File(path);
        if (file.isFile() && file.length() == 0) {
            return "";
        }
        try {
            return TIKA.parseToString(file);
        } catch (Exception e) {
            // 记录日志：文件可能加密、损坏、或格式不支持
            System.err.println("无法读取文件: " + path + ", 原因: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 读取文件内容，并返回字符串
     * @param file
     * @return
     */
    public static String safeReadFile(File file) throws TikaException, IOException {
        try {
            return TIKA.parseToString(file);
        } catch (Exception e) {
            // 记录日志：文件可能加密、损坏、或格式不支持
            System.err.println("无法读取文件: " + file.getAbsolutePath() + ", 原因: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 将文本按\n\n分段，同时确保每个分段长度不超过1000
     * @param fileText 原始文本
     * @param maxLength 段落分隔符
     * @param regex 正则表达式
     * @return 分段后的文本列表
     */
    public static List<String> splitText(String fileText, int maxLength, String regex) {
        List<String> result = new ArrayList<>();
        if (fileText == null || fileText.isEmpty()) {
            return result;
        }

        // 先按regex分割成初步段落
        String[] paragraphs = fileText.split(regex);

        for (String para : paragraphs) {
            // 如果段落长度本身就小于等于1000，直接添加
            if (para.length() <= maxLength) {
                result.add(para);
            } else {
                // 超过1000则进一步拆分
                int start = 0;
                while (start < para.length()) {
                    int end = Math.min(start + maxLength, para.length());
                    result.add(para.substring(start, end));
                    start = end;
                }
            }
        }

        return result;
    }

    /**
     * 将文本按指定正则分割，并确保每段不超过 maxLength。
     * 同时合并相邻的短段落（在不超过 maxLength 的前提下），避免产生过多过短片段。
     *
     * @param fileText   输入的完整文本
     * @param maxLength  每段最大长度（必须 > 0）
     * @param regex      用于初步分割的正则表达式
     * @return 分割并合并优化后的字符串列表
     */
    public static List<String> splitText2(String fileText, int maxLength, String regex) {
        List<String> result = new ArrayList<>();
        if (fileText == null || fileText.isEmpty() || maxLength <= 0) {
            return result;
        }

        // 第一步：按 regex 分割成初步段落（保留非空段）
        String[] rawParagraphs = fileText.split(regex);
        List<String> paragraphs = new ArrayList<>();
        for (String para : rawParagraphs) {
            if (!para.isEmpty()) { // 可选：跳过空段
                paragraphs.add(para);
            }
        }

        StringBuilder buffer = new StringBuilder(); // 用于累积可合并的小段

        for (String para : paragraphs) {
            if (para.length() > maxLength) {
                // 当前段超长：先 flush 缓冲区
                if (buffer.length() > 0) {
                    result.add(buffer.toString());
                    buffer.setLength(0); // 清空
                }

                // 拆分超长段（按 maxLength 切片）
                int start = 0;
                while (start < para.length()) {
                    int end = Math.min(start + maxLength, para.length());
                    result.add(para.substring(start, end));
                    start = end;
                }
            } else {
                // 当前段不超长，尝试合并
                if (buffer.length() == 0) {
                    // 缓冲区为空，直接加入
                    buffer.append(para);
                } else if (buffer.length() + para.length() <= maxLength) {
                    // 可以安全合并（注意：这里没有加连接符）
                    buffer.append(para);
                } else {
                    // 无法合并，先保存当前 buffer，再用 para 开启新缓冲
                    result.add(buffer.toString());
                    buffer.setLength(0);
                    buffer.append(para);
                }
            }
        }

        // 循环结束后，处理剩余 buffer
        if (buffer.length() > 0) {
            result.add(buffer.toString());
        }

        return result;
    }

    /**
     * 将 XHTML 表格转换为 Markdown 表格（手动实现）
      * @param xhtml
     * @return
     */
    private static String convertXHTMLToMarkdownLike(String xhtml) {
        Document doc = Jsoup.parse(xhtml, "", org.jsoup.parser.Parser.xmlParser());

        // 移除 XML 声明和多余标签，只保留 body 内容
        Element body = doc.selectFirst("body");
        if (body == null) {
            return xhtml;
        }

        // 处理表格 → 转为 Markdown 表格
        Elements tables = body.select("table");
        for (Element table : tables) {
            String markdownTable = htmlTableToMarkdown(table);
            table.replaceWith(new org.jsoup.nodes.TextNode(markdownTable));
        }

        // 其他块级元素添加换行
        for (Element block : body.select("p, div, h1, h2, h3, h4, h5, h6, pre")) {
            block.appendText("\n");
        }

        // 获取纯文本（保留手动插入的换行和表格）
        String text = body.text();

        // Jsoup 的 text() 会合并空白，所以我们改用 html() 并清理标签
        // 更可靠方式：遍历节点，但为简化，这里用正则粗略恢复换行
        String roughHtml = body.html()
                .replaceAll("</(p|h[1-6]|div|tr|table)>", "\n")
                .replaceAll("<br\\s*/?>", "\n")
                .replaceAll("<[^>]+>", ""); // 移除所有标签

        // 清理多余空行
        roughHtml = roughHtml.replaceAll("\n{3,}", "\n\n").trim();

        return roughHtml;
    }

    /**
     * 将单个 HTML Table 转为 Markdown 表格
      * @param table
     * @return
     */
    private static String htmlTableToMarkdown(Element table) {
        StringBuilder md = new StringBuilder();
        boolean firstRow = true;

        for (Element row : table.select("tr")) {
            List<String> cells = new ArrayList<>();
            for (Element cell : row.select("td, th")) {
                // 提取文本并转义管道符
                String text = cell.text().replace("|", "\\|").trim();
                cells.add(text.isEmpty() ? " " : text);
            }

            if (cells.isEmpty()) {
                continue;
            }

            md.append("| ").append(String.join(" | ", cells)).append(" |\n");

            // 在第一行后添加分隔线
            if (firstRow) {
                List<String> separators = new ArrayList<>();
                for (int i = 0; i < cells.size(); i++) {
                    separators.add("---");
                }
                md.append("| ").append(String.join(" | ", separators)).append(" |\n");
                firstRow = false;
            }
        }

        return md.toString();
    }
}
