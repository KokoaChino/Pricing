package com.book;

import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class MainTest {
    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        List<String> fontList = Arrays.asList(fontNames);
        String targetFont = "HarmonyOS Sans SC";
        String ans = fontList.contains(targetFont) ? "存在" : "不存在";
        System.out.println(ans);
    }
}