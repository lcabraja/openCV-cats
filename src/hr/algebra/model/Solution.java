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
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class Solution implements Serializable {

    private List<Rectangle> rectangles;
    private List<Boolean> correctangles;
    private CachedResult correctSolution;

    public Solution() {
        this.rectangles = null;
        this.correctangles = null;
        this.correctSolution = null;
    }

    private Solution(List<Boolean> correctangles, CachedResult correctSolution) {
        this.correctangles = correctangles;
        this.correctSolution = correctSolution;
    }

    public Solution(List<Rectangle> rectangles, List<Boolean> correctangles, CachedResult correctSolution) {
        this(correctangles, correctSolution);
        this.rectangles = rectangles;
    }

    public Solution(Rect[] rectangles, List<Boolean> correctangles, CachedResult correctSolution) {
        this(correctangles, correctSolution);
        this.rectangles = Arrays.asList(OCVUtils.rectArrayToRectangleArray(rectangles));
    }

    // getters + setters
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

    public List<Boolean> getCorrectangles() {
        return correctangles;
    }

    public void setCorrectangles(List<Boolean> correctangles) {
        this.correctangles = correctangles;
    }

    public CachedResult getCorrectSolution() {
        return correctSolution;
    }

    public void setCorrectSolution(CachedResult correctSolution) {
        this.correctSolution = correctSolution;
    }

    // overrides
    @Override
    public String toString() {
        return "Solution{" + "correctangles=" + correctangles + ", correctSolution=" + correctSolution + '}';
    }
}
