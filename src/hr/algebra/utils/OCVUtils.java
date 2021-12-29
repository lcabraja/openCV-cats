/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.awt.Rectangle;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class OCVUtils {

    private OCVUtils() {
    }

    public static Rect rectangleToRect(Rectangle recta) {
        return new Rect(recta.x, recta.y, recta.width, recta.height);
    }

    public static Rectangle rectToRectangle(Rect rect) {
        return new Rectangle(rect.x, rect.y, rect.width, rect.height);
    }

    public static Rect[] rectangleArrayToRectArray(Rectangle[] recta) {
        return Stream.of(recta).map((r) -> rectangleToRect(r)).toArray(Rect[]::new);
    }

    public static Rectangle[] rectArrayToRectangleArray(Rect[] rect) {
        return Stream.of(rect).map((r) -> rectToRectangle(r)).toArray(Rectangle[]::new);
    }

    public static List<Rect> rectangleListToRectList(List<Rectangle> recta) {
        return recta.stream().map((r) -> rectangleToRect(r)).collect(Collectors.toList());
    }

    public static List<Rectangle> rectListToRectangleList(List<Rect> rect) {
        return rect.stream().map((r) -> rectToRectangle(r)).collect(Collectors.toList());
    }
}
