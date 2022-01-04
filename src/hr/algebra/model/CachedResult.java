/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.utils.OCVUtils;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class CachedResult implements Serializable {

    private String filePath;
    private String classifierPath;
    private List<Rectangle> rectangles;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String FilePath) {
        this.filePath = FilePath;
    }

    public String getClassifierPath() {
        return classifierPath;
    }

    public void setClassifierPath(String ClassifierPath) {
        this.classifierPath = ClassifierPath;
    }

    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    public void setRectangles(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    public Rect[] getRects() {
        return (Rect[]) OCVUtils.rectangleListToRectList(rectangles).toArray();
    }

    public void setRects(Rect[] rectangles) {
        this.rectangles = Arrays.asList(OCVUtils.rectArrayToRectangleArray(rectangles));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash *= this.filePath.hashCode();
        hash *= this.classifierPath.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            System.out.println("1");
            return true;
        }
        if (obj == null) {
            System.out.println("null 2");
            return false;
        }
        if (getClass() != obj.getClass()) {
            System.out.println(getClass());
            System.out.println(obj.getClass());
            return false;
        }
        final CachedResult other = (CachedResult) obj;
        if (!Objects.equals(this.filePath, other.filePath)) {
            System.out.println("4");
            return false;
        }
        if (!Objects.equals(this.classifierPath, other.classifierPath)) {
            System.out.println("5");
            return false;
        }
        System.out.println("6");
        return true;
    }

    @Override
    public String toString() {
        return "CachedResult{" + "filePath=" + filePath + ", classifierPath=" + classifierPath + ", rectangles=" + rectangles + '}';
    }
}
