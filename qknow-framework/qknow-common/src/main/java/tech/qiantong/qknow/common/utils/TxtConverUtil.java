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

package tech.qiantong.qknow.common.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;

/**
 * @author 鑸掓湀閼�
 * @date 2023/10/27 15:20
 **/
public class TxtConverUtil {
    /**
     * txt文本转为pdf并返回baes64编码
     */
    public static File txtToPdf(FileInputStream txtFilePath,String uploadFilePath) {
        File f = new File("");
        try {
            String FONT = "/home/qtt/src/Nami/fonts/";
            f = File.createTempFile("linshi", ".pdf", new File(uploadFilePath));
            String pdfFilePath = f.getAbsolutePath();
            // Convert txt to pdf
            Document document = new Document();
            OutputStream os = new FileOutputStream(pdfFilePath);
            PdfWriter.getInstance(document, os);
            //方法一：使用Windows系统字体(TrueType)
            BaseFont baseFont = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont);
            InputStreamReader isr = new InputStreamReader(txtFilePath, "UTF-8");

            BufferedReader bufferedReader = new BufferedReader(isr);
            String str = "初始化";
            Boolean isDocument = false;
            if (bufferedReader.readLine() != null){
                document.open();
            }
            System.out.println(bufferedReader.readLine());
            while ((str = bufferedReader.readLine()) != null) {
                isDocument = true;
                document.add(new Paragraph(str, font));
            }
            if (isDocument) {
                document.close();
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * 文件转为字节数组
     */
    private static byte[] readBytesFromFile (String filePath) throws IOException {
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        long fileSize = file.length();
        byte[] bytes = new byte[(int) fileSize];
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
    }
}
