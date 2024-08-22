package com.book;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Main {
    public static void main(String[] args) {
        Picture.setMODE(3); // 设置模式
        Map<String, String> map = new HashMap<>(); // 商品id -> 价格
        DataFormatter dataFormatter = new DataFormatter();
        try {
            File table = Objects.requireNonNull(new File("Table").listFiles())[0]; // 表格文件
            XSSFWorkbook workbook = new XSSFWorkbook(table); // 工作簿
            XSSFSheet sheet = workbook.getSheetAt(0); // 表格
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                String mid = dataFormatter.formatCellValue(sheet.getRow(i).getCell(0));
                String price = dataFormatter.formatCellValue(sheet.getRow(i).getCell(1));
                map.put(mid, price); // 建立映射
            }
            File[] files = new File("Input").listFiles(); // 图片文件
            if (files != null) {
                for (File f: files) {
                    String mid = f.getName();
                    mid = mid.substring(0, mid.lastIndexOf('.'));
                    String price = map.get(mid);
                    if (price == null) System.out.println(mid); // 匹配失败
                    else Picture.getImageResult(f, price);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}