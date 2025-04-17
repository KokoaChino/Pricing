package com.example;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.util.*;
import static com.example.Picture.check;


public class Main {

    private static final int MODE = 6;

    public static void main(String[] args) {
        Picture.setMODE(MODE);
        Map<String, String> map = new HashMap<>();
        DataFormatter dataFormatter = new DataFormatter();
        File table = Objects.requireNonNull(new File("Table").listFiles())[0]; // 表格
        try (XSSFWorkbook workbook = new XSSFWorkbook(table)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                String mid = dataFormatter.formatCellValue(sheet.getRow(i).getCell(0));
                String price = dataFormatter.formatCellValue(sheet.getRow(i).getCell(1));
                map.put(mid, price);
            }
            Set<String> successMatch = new HashSet<>() ,tableNoMatch = map.keySet(), imageNoMatch = new HashSet<>();
            File[] files = new File("Input").listFiles(); // 图片
            if (files != null) {
                try {
                    String fontName;
                    try {
                        fontName = Picture.P.get(MODE - 1).fontName();
                    } catch (IndexOutOfBoundsException e) {
                        fontName = WatermarkParams.builder().build().fontName();
                    }
                    if (!check(fontName)) throw new FontNotFoundException("字体 [" + fontName + "] 未安装在系统中");
                } catch (FontNotFoundException e) {
                    System.err.println("异常信息: " + e.getMessage());
                    System.out.println("1. 请从官方网站下载并安装该字体");
                    System.out.println("2. 已自动切换至默认字体\n");
                }
                for (File f: files) {
                    String mid = f.getName();
                    mid = mid.substring(0, mid.lastIndexOf('.'));
                    String price = map.get(mid);
                    if (price == null) imageNoMatch.add(mid);
                    else {
                        Picture.getImageResult(f, price);
                        tableNoMatch.remove(mid);
                        successMatch.add(mid);
                    }
                }
                System.out.format("匹配成功：%d\n", successMatch.size());
                successMatch.forEach(e -> System.out.println("- " + e));
                System.out.format("图匹配失败：%d\n", imageNoMatch.size());
                imageNoMatch.forEach(e -> System.out.println("- " + e));
                System.out.format("表匹配失败：%d\n", tableNoMatch.size());
                tableNoMatch.forEach(e -> System.out.println("- " + e));
            } else System.err.println("图片文件为空！！！");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}