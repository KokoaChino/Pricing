package com.example;

import lombok.Builder;
import lombok.Getter;
import lombok.With;
import lombok.experimental.Accessors;
import java.awt.*;


@With
@Builder
@Getter
@Accessors(fluent = true)
public class WatermarkParams {

    private static final int N = 1200;

    // 位置（水印左下角的起始坐标）
    @Builder.Default private final int x = 0;
    @Builder.Default private final int y = N;

    // 字符偏移
    @Builder.Default private final int dx = 0;
    @Builder.Default private final int dy = 0;
    @Builder.Default private final boolean includeCharWidthInDx = true;
    @Builder.Default private final boolean includeCharHeightInDy = false;

    // 字体
    @Builder.Default private final String fontName = "微软雅黑";
    @Builder.Default private final int fontStyles = Font.PLAIN;
    @Builder.Default private final Integer fontSize = 100;
    @Builder.Default private final Color fontColor = Color.BLACK;

    // 描边
    @Builder.Default private final Integer strokeSize = null;
    @Builder.Default private final Color strokeColor = Color.WHITE;

    // 投影
    @Builder.Default private final int shadowDx = 0;
    @Builder.Default private final int shadowDy = 0;
    @Builder.Default private final Color shadowColor = new Color(0, 0, 0, 0);
    @Builder.Default private final float shadowOpacity = Float.NaN;

    // 渐变
    @Builder.Default private final GradientParams fontGradient = null;
    @Builder.Default private final GradientParams strokeGradient = null;

    // 仿射变换
    @Builder.Default private final float rotation = Float.NaN;
    @Builder.Default private final float shearX = Float.NaN;
    @Builder.Default private final boolean priorityRotation = true;

    @With
    @Builder
    @Getter
    @Accessors(fluent = true)
    public static class GradientParams {
        @Builder.Default private final Point start = new Point(0, 0);
        @Builder.Default private final Color startColor = Color.BLUE;
        @Builder.Default private final Point end = new Point(N, N);
        @Builder.Default private final Color endColor = Color.RED;
        @Builder.Default private final boolean cyclic = true;
    }
}