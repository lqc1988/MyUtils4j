package com.lqc.util.qrcode;

import java.awt.*;

/**
 * <pre>
 * Created by Binary Wang on 2017-01-05.
 * @author <a href="https://github.com/binarywang">binarywang(Binary Wang)</a>
 * </pre>
 */
public class MatrixToLogoImageConfig {
    //logo默认边框颜色
    public static final Color DEFAULT_BORDER_COLOR = Color.lightGray;
    //logo默认边框宽度
    public static final int DEFAULT_BORDER = 0;
    //logo大小默认为照片的1/5
    public static final int DEFAULT_LOGO_PART = 5;

    private final int border;
    private final Color borderColor;
    private final int logoPart;

    public MatrixToLogoImageConfig() {
        this(DEFAULT_BORDER, DEFAULT_BORDER_COLOR, DEFAULT_LOGO_PART);
    }

    public MatrixToLogoImageConfig(int border) {
        this.border = border;
        this.borderColor = DEFAULT_BORDER_COLOR;
        this.logoPart = DEFAULT_LOGO_PART;
    }

    public MatrixToLogoImageConfig(int border, Color borderColor) {
        this.border = border;
        this.borderColor = borderColor;
        this.logoPart = DEFAULT_LOGO_PART;
    }

    public MatrixToLogoImageConfig(Integer border, Color borderColor, Integer logoPart) {
        this.border = border;
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

    public int getBorder() {
        return this.border;
    }

    public int getLogoPart() {
        return this.logoPart;
    }
}
