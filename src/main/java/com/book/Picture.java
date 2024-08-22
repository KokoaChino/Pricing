package com.book;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Picture {

    private static int MODE; // 模式

    public static void setMODE(int MODE) {
        Picture.MODE = MODE;
    }

    public static void getImageResult(File file, String watermark) { // 获取最终图片对象
        try (FileOutputStream outputStream = new FileOutputStream("Output" + "\\" + file.getName())) {
            Image image = ImageIO.read(new File(file.getAbsolutePath()));
            ImageIO.write(getBufferedImage(image, watermark), "jpg", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage getBufferedImage(Image image, String watermark) { // 获取加了水印的图片缓存
        return switch (MODE) {
            case 1 -> fun_1(image, watermark); // 单一颜色
            case 2 -> fun_2(image, watermark); // 渐变颜色
            case 3 -> fun_3(image, watermark); // 斜体
            default -> throw new IllegalStateException("模式设置错误！");
        };
    }

    public static BufferedImage fun_1(Image image, String watermark) {
        int width = image.getWidth(null), height = image.getHeight(null); // 图片宽高
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // 图片缓存
        Graphics2D graphics = bufferedImage.createGraphics(); // 画笔
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // 抗锯齿
        graphics.drawImage(image, 0, 0, width, height, null); // 初始画布
        graphics.setFont(new Font("Source Han Sans CN", Font.PLAIN, 130)); // 字体类型 字体风格 字体大小
        graphics.setColor(new Color(254, 249, 233)); // 颜色
        FontMetrics fm = graphics.getFontMetrics(); // 获取字体的尺寸
        for (int i = 0, x = 90; i < watermark.length(); i++) { // 画水印
            char c = watermark.charAt(i);
            graphics.drawString(String.valueOf(c), x, 1165);
            x += fm.charWidth(c) - 10;
        }
        graphics.dispose();
        return bufferedImage;
    }

    public static BufferedImage fun_2(Image image, String watermark) {
        int width = image.getWidth(null), height = image.getHeight(null); // 图片宽高
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // 图片缓存
        Graphics2D graphics = bufferedImage.createGraphics(); // 画笔
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // 抗锯齿
        graphics.drawImage(image, 0, 0, width, height, null); // 初始画布
        graphics.setFont(new Font("Source Han Sans CN", Font.PLAIN, 130)); // 字体类型 字体风格 字体大小
        graphics.setPaint(new GradientPaint(0, 0, new Color(254, 249, 233), 80, 80, new Color(220, 176, 119), true)); // 渐变颜色
        FontMetrics fm = graphics.getFontMetrics(); // 获取字体的尺寸
        for (int i = 0, x = 90; i < watermark.length(); i++) { // 画水印
            char c = watermark.charAt(i);
            graphics.drawString(String.valueOf(c), x, 1165);
            x += fm.charWidth(c) - 10;
        }
        graphics.dispose();
        return bufferedImage;
    }

    public static BufferedImage fun_3(Image image, String watermark) {
        int width = image.getWidth(null), height = image.getHeight(null); // 图片宽高
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // 图片缓存
        Graphics2D graphics = bufferedImage.createGraphics(); // 画笔
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // 抗锯齿
        graphics.drawImage(image, 0, 0, width, height, null); // 初始画布
        graphics.setFont(new Font("微软雅黑", Font.BOLD + Font.ITALIC, 55)); // 字体类型 字体风格 字体大小
        FontMetrics fm = graphics.getFontMetrics(); // 获取字体的尺寸
        graphics.setColor(new Color(250, 65, 10)); // 描边颜色
        for (int i = 0, x = 333, y = 510, gap = 5; i < watermark.length(); i++) { // 画描边
            char c = watermark.charAt(i);
            for (int j = -gap; j <= gap; j++) {
                for (int k = -gap; k <= gap; k++) {
                    graphics.drawString(String.valueOf(c), x + j, y + k);
                }
            }
            x += fm.charWidth(c);
            y -= 3;
        }
        graphics.setColor(Color.WHITE); // 颜色
        for (int i = 0, x = 333, y = 510; i < watermark.length(); i++) { // 画水印
            char c = watermark.charAt(i);
            graphics.drawString(String.valueOf(c), x, y);
            x += fm.charWidth(c);
            y -= 3;
        }
        graphics.dispose();
        return bufferedImage;
    }
}