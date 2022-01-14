/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.util.Arrays;
import java.util.Random;
import org.opencv.core.Scalar;

/**
 *
 * @author lcabraja
 */
public enum ColorUtils {

    RED("ff0000"), GREEN("00ff00"), BLUE("0000ff"), WHITE("ffffff"), GRAY("444444"), BLACK("000000");

    private final Scalar color;

    ColorUtils(String hexcode) {
        this.color = hexToScalar(hexcode);
    }

    public Scalar getScalarValue() {
        return this.color;
    }

    private static final Random R = new Random();
    private static final String[] STATIC_COLOR_LIST = {
        "ff0000", "00ff00", "0000ff", "ffff00", "ff00ff", "00ffff", "000000",
        "800000", "008000", "000080", "808000", "800080", "008080", "808080",
        "c00000", "00c000", "0000c0", "c0c000", "c000c0", "00c0c0", "c0c0c0",
        "400000", "004000", "000040", "404000", "400040", "004040", "404040",
        "200000", "002000", "000020", "202000", "200020", "002020", "202020",
        "600000", "006000", "000060", "606000", "600060", "006060", "606060",
        "a00000", "00a000", "0000a0", "a0a000", "a000a0", "00a0a0", "a0a0a0",
        "e00000", "00e000", "0000e0", "e0e000", "e000e0", "00e0e0", "e0e0e0"
    };

    public static Scalar hexToScalar(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        int red = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue = Integer.parseInt(hex.substring(4, 6), 16);
        return new Scalar(red, green, blue);
    }

    public static String formatNumberAsTwoDigitHex(double number) {
        return formatNumberAsTwoDigitHex((Number) (int) number);
    }

    public static String formatNumberAsTwoDigitHex(Number number) {
        String hex = Integer.toHexString((int) number);
        if (hex.length() == 0) {
            return "00";
        }
        if (hex.length() == 1) {
            return "0" + hex;
        }
        if (hex.length() > 2) {
            return hex.substring(0, 2);
        }
        return hex;
    }

    public static String scalarToHex(Scalar scalar) {
        String red = formatNumberAsTwoDigitHex(scalar.val[0]);
        String green = formatNumberAsTwoDigitHex(scalar.val[1]);
        String blue = formatNumberAsTwoDigitHex(scalar.val[2]);
        return red + green + blue;
    }

    public static String gettRandomColorHex() {
        return STATIC_COLOR_LIST[R.nextInt(STATIC_COLOR_LIST.length)];
    }

    public static String getDeterministicColorHex() {
        return STATIC_COLOR_LIST[0];
    }

    public static String getDeterministicColorHex(String lastColor) {
        int index = Arrays.asList(STATIC_COLOR_LIST).indexOf(lastColor);
        if (index >= 0 && index < STATIC_COLOR_LIST.length) {
            try {
                return STATIC_COLOR_LIST[index + 1];
            } catch (Exception ex) {
                getDeterministicColorHex();
            }
        }
        return getDeterministicColorHex();
    }

    public static Scalar getRandomColorScalar() {
        return hexToScalar(gettRandomColorHex());
    }

    public static Scalar getDeterministicColorScalar() {
        return hexToScalar(getDeterministicColorHex());
    }

    public static Scalar getDeterministicColorScalar(Scalar lastColor) {
        return hexToScalar(getDeterministicColorHex(scalarToHex(lastColor)));
    }

    public static Scalar getDeterministicColorScalar(String lastColor) {
        return hexToScalar(getDeterministicColorHex(lastColor));
    }
}
