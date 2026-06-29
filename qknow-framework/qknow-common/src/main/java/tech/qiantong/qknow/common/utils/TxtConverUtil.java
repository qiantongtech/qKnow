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
