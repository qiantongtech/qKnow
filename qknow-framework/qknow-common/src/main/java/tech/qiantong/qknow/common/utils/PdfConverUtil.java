package tech.qiantong.qknow.common.utils;

import cn.hutool.system.*;
import com.aspose.words.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.xslf.usermodel.*;

import java.awt.Font;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;

/**
 * 类名称：PdfConvertUtil
 * 类描述：转为为PDF工具类
 */
public final class PdfConverUtil {

    /**
     * @Title: pptToPdf
     * @Description: ppt格式转换pdf
     * @param inputStream
     * @return:
     * @throws
     * @author: zhouyangyang
     * @Date:  2021/4/2  9:49
     */
    public static File pptToPdf(InputStream inputStream,String uploadFilePath) {

        Document document = null;

        HSLFSlideShow hslfSlideShow = null;

        PdfWriter pdfWriter = null;
        File f = new File("");
        FileOutputStream outputStream=null;
        try {
            // 创建pdf临时文件
            f = File.createTempFile("linshi", ".pdf", new File(uploadFilePath));
            String pdfPath = f.getAbsolutePath();
            outputStream = new FileOutputStream(pdfPath);
            hslfSlideShow = new HSLFSlideShow(inputStream);

            Dimension dimension = hslfSlideShow.getPageSize();


            document = new Document();

            pdfWriter = PdfWriter.getInstance(document, outputStream);

            document.open();

            PdfPTable pdfPTable = new PdfPTable(1);

            List<HSLFSlide> hslfSlideList = hslfSlideShow.getSlides();

            // 加载自定义字体文件
            String fontPath = "/home/qtt/src/Nami/fonts/";
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            for (int i=0; i < hslfSlideList.size(); i++) {

                HSLFSlide hslfSlide = hslfSlideList.get(i);
                // 设置字体, 解决中文乱码
                for (HSLFShape shape : hslfSlide.getShapes()) {
                    HSLFTextShape textShape = (HSLFTextShape) shape;

                    for (HSLFTextParagraph textParagraph : textShape.getTextParagraphs()) {
                        for (HSLFTextRun textRun : textParagraph.getTextRuns()) {
                            textRun.setFontFamily(customFont.getFontName());
                        }
                    }
                }
                BufferedImage bufferedImage = new BufferedImage((int)dimension.getWidth(), (int)dimension.getHeight(), BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics2d = bufferedImage.createGraphics();

                graphics2d.setPaint(Color.white);
                graphics2d.setFont(new Font(customFont.getFontName(), Font.PLAIN, 12));

                hslfSlide.draw(graphics2d);

                graphics2d.dispose();

                Image image = Image.getInstance(bufferedImage, null);
                image.scalePercent(50f);

                // 写入单元格
                pdfPTable.addCell(new PdfPCell(image, true));
                document.add(image);
            }
            // Convert pdf to base64
//            byte[] pdfBytes = readBytesFromFile(pdfPath);
//            base64String = Base64.encodeBase64String(pdfBytes);
            // 终止后删除临时文件
//            f.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
            return f;
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (pdfWriter != null) {
                    pdfWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    /**
     * @param inputStream  源文件输入流
     * @return
     * @throws
     * @Title: pptxToPdf
     * @Description: PPTx转为PDF文件
     * @return: boolean
     * @author: hechengcheng
     * @Date: 2020年12月18日/下午3:17:28
     */
    public static File pptxToPdf(InputStream inputStream,String uploadFilePath) {

        Document document = null;

        XMLSlideShow slideShow = null;

        PdfWriter pdfWriter = null;
        File f = new File("");
        FileOutputStream outputStream=null;
        try {
            // 创建pdf临时文件
            f = File.createTempFile("linshi", ".pdf", new File(uploadFilePath));
            String pdfPath = f.getAbsolutePath();
            outputStream = new FileOutputStream(pdfPath);
            slideShow = new XMLSlideShow(inputStream);

            Dimension dimension = slideShow.getPageSize();

            document = new Document();

            pdfWriter = PdfWriter.getInstance(document, outputStream);

            document.open();


            PdfPTable pdfPTable = new PdfPTable(1);

            List<XSLFSlide> slideList = slideShow.getSlides();
            // 加载自定义字体文件
            String fontPath = "/home/qtt/src/Nami/fonts/";
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            for (int i = 0, row = slideList.size(); i < row; i++) {

                XSLFSlide slide = slideList.get(i);

                // 设置字体, 解决中文乱码
                for (XSLFShape shape : slide.getShapes()) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;

                    for (XSLFTextParagraph textParagraph : textShape.getTextParagraphs()) {
                        for (XSLFTextRun textRun : textParagraph.getTextRuns()) {
                            textRun.setFontFamily(customFont.getFontName());
                        }
                    }
                }

                BufferedImage bufferedImage = new BufferedImage((int) dimension.getWidth(), (int) dimension.getHeight(), BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics2d = bufferedImage.createGraphics();

                graphics2d.setPaint(Color.white);
                graphics2d.setFont(new Font(customFont.getFontName(), Font.PLAIN, 12));

                slide.draw(graphics2d);

                graphics2d.dispose();

                Image image = Image.getInstance(bufferedImage, null);
                image.scalePercent(50f);

                // 写入单元格
                pdfPTable.addCell(new PdfPCell(image, true));
                document.add(image);
            }
            // Convert pdf to base64
//            byte[] pdfBytes = readBytesFromFile(pdfPath);
//            base64String = Base64.encodeBase64String(pdfBytes);
            // 终止后删除临时文件
//            f.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
            return f;
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (pdfWriter != null) {
                    pdfWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return f;
    }

    /**
     * word(doc/docx)转换pdf
     * @param inputStream  源文件输入流
     **/
    public static File wordTopdfByAspose(InputStream inputStream,String uploadFilePath) {
        String base64String = "";
        File f = new File("");
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            return null;
        }
        FileOutputStream outputStream=null;
        try {
            // 创建pdf临时文件
            f = File.createTempFile("linshi", ".pdf", new File(uploadFilePath));
            String pdfPath = f.getAbsolutePath();
            outputStream = new FileOutputStream(pdfPath);
            OsInfo osInfo = SystemUtil.getOsInfo();
            if(osInfo.isLinux()){
                FontSettings.setFontsFolder("/home/qtt/src/Nami/fonts/", true);
            }
            // 将源文件保存在com.aspose.words.Document中，具体的转换格式依靠里面的save方法
            com.aspose.words.Document doc = new com.aspose.words.Document(inputStream);

            // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,EPUB, XPS, SWF 相互转换
            doc.save(outputStream, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
            return f;
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return f;
    }

    // 官方文档的要求 无需理会
    public static boolean getLicense() {
        boolean result = false;
        try {
            String s = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";
            ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * excel问价转pdf
     * @param inputStream  源文件输入流
     **/
    public static File excelToPdf(InputStream inputStream,String uploadFilePath) {
        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getExeclLicense()) {
            return null;
        }
        String base64String = "";
        File f = new File("");
        FileOutputStream outputStream=null;
        try {
            // 创建pdf临时文件
            f = File.createTempFile("linshi", ".pdf", new File(uploadFilePath));
            String pdfPath = f.getAbsolutePath();
            outputStream = new FileOutputStream(pdfPath);
            OsInfo osInfo = SystemUtil.getOsInfo();
            if(osInfo.isLinux()){
                FontSettings.setFontsFolder("/home/qtt/src/Nami/fonts/", true);
            }
            com.aspose.cells.Workbook wb = new com.aspose.cells.Workbook(inputStream);// 原始excel路径
            com.aspose.cells.PdfSaveOptions pdfSaveOptions = new com.aspose.cells.PdfSaveOptions();
            pdfSaveOptions.setOnePagePerSheet(false);


            int[] autoDrawSheets={3};
            //当excel中对应的sheet页宽度太大时，在PDF中会拆断并分页。此处等比缩放。
            autoDraw(wb,autoDrawSheets);

            int[] showSheets={0};
            //隐藏workbook中不需要的sheet页。
            printSheetPage(wb,showSheets);
            wb.save(outputStream, pdfSaveOptions);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }


    /**
     * 设置打印的sheet 自动拉伸比例
     * @param wb
     * @param page 自动拉伸的页的sheet数组
     */
    public static void autoDraw(com.aspose.cells.Workbook wb,int[] page){
        if(null!=page&&page.length>0){
            for (int i = 0; i < page.length; i++) {
                wb.getWorksheets().get(i).getHorizontalPageBreaks().clear();
                wb.getWorksheets().get(i).getVerticalPageBreaks().clear();
            }
        }
    }

    /**
     * 隐藏workbook中不需要的sheet页。
     *
     * @param wb
     * @param page 显示页的sheet数组
     */
    public static void printSheetPage(com.aspose.cells.Workbook wb, int[] page) {
        for (int i = 1; i < wb.getWorksheets().getCount(); i++) {
            wb.getWorksheets().get(i).setVisible(false);
        }
        if (null == page || page.length == 0) {
            wb.getWorksheets().get(0).setVisible(true);
        } else {
            for (int i = 0; i < page.length; i++) {
                wb.getWorksheets().get(i).setVisible(true);
            }
        }
    }

    public static boolean getExeclLicense() {
        boolean result = false;
        try {
            String s = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";
            ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
            com.aspose.cells.License aposeLic = new com.aspose.cells.License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }





    /**
     * 文件转为字节数组
     */
    public static byte[] readBytesFromFile (String filePath) throws IOException {
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        long fileSize = file.length();
        byte[] bytes = new byte[(int) fileSize];
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
    }

    /**
     * CAD文件（.dwg）转为 PDF
     */

//    public static File dwgToPdfByAspose(InputStream inputStream, String uploadFilePath) {
//        File f = new File("");
//        FileOutputStream outputStream = null;
//        try {
//            // 创建PDF临时文件
//            f = File.createTempFile("linshi", ".pdf", new File(uploadFilePath));
//            String pdfPath = f.getAbsolutePath();
//            outputStream = new FileOutputStream(pdfPath);
//
//            // 加载DWG文件
//            CadImage cadImage = (CadImage) CadImage.load(inputStream);
//
//            // 配置PDF选项
//            PdfSaveOptions pdfOptions = new PdfSaveOptions();
//            pdfOptions.setVectorRasterization(true); // 设置矢量转栅格化
//
//            // 将DWG保存为PDF
//            cadImage.save(outputStream, SaveFormat.PDF, pdfOptions);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return f;
//        } finally {
//            if (outputStream != null) {
//                try {
//                    outputStream.flush();
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return f;
//    }
}
