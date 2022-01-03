/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author lcabraja
 */
public class Solution implements Serializable {

    private List<Rectangle> correctangles;
    private CachedResult correctSolution;

    public Solution() {
        this.correctangles = null;
        this.correctSolution = null;
    }

    public Solution(List<Rectangle> correctangles, CachedResult correctSolution) {
        this.correctangles = correctangles;
        this.correctSolution = correctSolution;
    }

    public List<Rectangle> getCorrectangles() {
        return correctangles;
    }

    public void setCorrectangles(List<Rectangle> correctangles) {
        this.correctangles = correctangles;
    }

    public CachedResult getCorrectSolution() {
        return correctSolution;
    }

    public void setCorrectSolution(CachedResult correctSolution) {
        this.correctSolution = correctSolution;
    }

    @Override
    public String toString() {
        return "Solution{" + "correctangles=" + correctangles + ", correctSolution=" + correctSolution + '}';
    }
}
