package com.example;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Picture {

    private static int MODE; // 模式
    public static final List<WatermarkParams> P = List.of(
            WatermarkParams.builder()
                    .x(90).y(1165).dx(-10)
                    .fontName("Source Han Sans CN").fontSize(130).fontColor(new Color(254, 249, 233))
                    .build(),
            WatermarkParams.builder()
                    .x(90).y(1165).dx(-10)
                    .fontName("Source Han Sans CN").fontSize(130)
                    .fontGradient(WatermarkParams.GradientParams.builder()
                            .startColor(new Color(254, 249, 233))
                            .end(new Point(80, 80))
                            .endColor(new Color(220, 176, 119))
                            .build())
                    .build(),
            WatermarkParams.builder()
                    .x(333).y(510).dy(-3)
                    .fontStyles(Font.BOLD | Font.ITALIC).fontSize(55).fontColor(Color.WHITE)
                    .strokeSize(5).strokeColor(new Color(250, 65, 10))
                    .build(),
            WatermarkParams.builder()
                    .x(70).y(705).dx(-8)
                    .fontName("DIN").fontStyles(Font.BOLD).fontColor(new Color(227, 34, 17))
                    .build(),
            WatermarkParams.builder()
                    .x(840).y(1175).dx(-3)
                    .fontName("HarmonyOS Sans SC").fontStyles(Font.BOLD | Font.ITALIC).fontSize(140).fontColor(new Color(0xfdf04a))
                    .strokeSize(6).strokeColor(new Color(0x065223))
                    .build(),
            WatermarkParams.builder()
                    .x(878).y(948).dx(-3)
                    .fontName("方正兰亭特黑简体").fontStyles(Font.ITALIC).fontSize(94).fontColor(Color.WHITE)
                    .strokeSize(8).strokeColor(new Color(0xce004e))
                    .shadowDx(15).shadowDy(10).shadowColor(new Color(160, 0, 68)).shadowOpacity(0.5f)
                    .build()
    );

    public static void setMODE(int MODE) {
        Picture.MODE = MODE;
    }

    public static void getImageResult(File file, String watermark) {
        try (FileOutputStream outputStream = new FileOutputStream("Output" + "\\" + file.getName())) {
            Image image = ImageIO.read(new File(file.getAbsolutePath()));
            BufferedImage watermarkedImage = handleWatermark(image, watermark);
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) throw new RuntimeException("未找到 JPG 编写器");
            ImageWriter writer = writers.next();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream)) {
                writer.setOutput(ios);
                JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
                jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                jpegParams.setCompressionQuality(0.98f);
                jpegParams.setOptimizeHuffmanTables(true);
                writer.write(null, new IIOImage(watermarkedImage, null, null), jpegParams);
            }
            writer.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage handleWatermark(Image image, String watermark) { // 处理图片水印
        WatermarkParams p;
        try {
            p = P.get(MODE - 1);
        } catch (IndexOutOfBoundsException e) {
            p = WatermarkParams.builder().build();
        }
        int width = image.getWidth(null), height = image.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawImage(image, 0, 0, width, height, null);
        g.setFont(new Font(p.fontName(), p.fontStyles(), p.fontSize()));
        FontMetrics fm = g.getFontMetrics();
        if (!Float.isNaN(p.rotation()) || !Float.isNaN(p.shearX())) { // 仿射变换
            g.transform(getAffineTransform(p));
        }
        if (!Float.isNaN(p.shadowOpacity())) { // 投影
            Composite originalComposite = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, p.shadowOpacity()));
            g.setColor(p.shadowColor());
            int x = p.x() + p.shadowDx(), y = p.y() + p.shadowDy();
            for (int i = 0; i < watermark.length(); i++) {
                char c = watermark.charAt(i);
                g.drawString(String.valueOf(c), x, y);
                x += (p.includeCharWidthInDx() ? fm.charWidth(c) : 0) + p.dx();
                y += (p.includeCharHeightInDy() ? fm.getAscent() : 0) + p.dy();
            }
            g.setComposite(originalComposite);
        }
        if (p.strokeSize() != null) { // 描边
            if (p.strokeGradient() != null) {
                WatermarkParams.GradientParams gp = p.strokeGradient();
                g.setPaint(new GradientPaint(gp.start(), gp.startColor(), gp.end(), gp.endColor(), gp.cyclic()));
            } else {
                g.setColor(p.strokeColor());
            }
            for (int i = 0, x = p.x(), y = p.y(), gap = p.strokeSize(); i < watermark.length(); i++) {
                char c = watermark.charAt(i);
                for (int j = -gap; j <= gap; j++) {
                    for (int k = -gap; k <= gap; k++) {
                        g.drawString(String.valueOf(c), x + j, y + k);
                    }
                }
                x += (p.includeCharWidthInDx() ? fm.charWidth(c) : 0) + p.dx();
                y += (p.includeCharHeightInDy() ? fm.getAscent() : 0) + p.dy();
            }
        }
        if (p.fontGradient() != null) { // 渐变
            WatermarkParams.GradientParams gp = p.fontGradient();
            g.setPaint(new GradientPaint(gp.start(), gp.startColor(), gp.end(), gp.endColor(), gp.cyclic()));
        } else {
            g.setColor(p.fontColor());
        }
        for (int i = 0, x = p.x(), y = p.y(); i < watermark.length(); i++) {
            char c = watermark.charAt(i);
            g.drawString(String.valueOf(c), x, y);
            x += (p.includeCharWidthInDx() ? fm.charWidth(c) : 0) + p.dx();
            y += (p.includeCharHeightInDy() ? fm.getAscent() : 0) + p.dy();
        }
        g.dispose();
        return bufferedImage;
    }

    private static AffineTransform getAffineTransform(WatermarkParams p) { // 获取仿射变换
        AffineTransform transform = new AffineTransform();
        if (!Float.isNaN(p.rotation()) && !Float.isNaN(p.shearX())) {
            if (p.priorityRotation()) {
                transform.rotate(p.rotation());
                transform.shear(p.shearX(), 0);
            } else {
                transform.shear(p.shearX(), 0);
                transform.rotate(p.rotation());
            }
        } else if (!Float.isNaN(p.rotation())) {
            transform.rotate(p.rotation());
        } else {
            transform.shear(p.shearX(), 0);
        }
        return transform;
    }

    public static boolean check(String targetFont) { // 检查系统是否安装有目标字体
        String[] fontNames =  GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Set<String> fontList = Arrays.stream(fontNames).collect(Collectors.toSet());
        return fontList.contains(targetFont);
    }
}