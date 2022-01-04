/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import hr.algebra.model.CachedResult;
import hr.algebra.model.Solution;
import hr.algebra.utils.CollectionUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class MemCache implements Cache {

    private final Map<CachedResult, Solution> solutions = new HashMap<>();

    @Override
    public boolean contains(File imageFile, String classifierPath) {
        System.out.println("contains @ " + getClass().toString());
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        return solutions.containsKey(cf);
    }

    @Override
    public boolean containsSolution(File imageFile, String classifierPath) {
        System.out.println("containsSolution @ " + getClass().toString());
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        return solutions.get(cf) != null;
    }

    @Override
    public Optional<CachedResult> getFaceRects(File imageFile, String classifierPath) {
        System.out.println("getFaceRects @ " + getClass().toString());
        CachedResult cr = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        System.out.println(solutions.keySet());
        if (solutions.containsKey(cr)) {
            return Optional.of(CollectionUtils.getValueFromSet(solutions.keySet(), cr));
        }
        return Optional.empty();
    }

    @Override
    public void setFaceRects(File imageFile, Rect[] facesArray, String classifierPath) {
        System.out.println("setFaceRects @ " + getClass().toString());
        CachedResult cr = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
                setRects(facesArray);
            }
        };
        System.out.println(cr);
        solutions.put(cr, null);
    }

    @Override
    public void setSolution(Solution solution) {
        System.out.println("setSolution @ " + getClass().toString());
        solutions.put(solution.getCorrectSolution(), solution);
    }

    @Override
    public Optional<Solution> getSolution(CachedResult cr) {
        System.out.println("getSolution @ " + getClass().toString());
        return solutions.containsKey(cr) && solutions.get(cr) != null
                ? Optional.of(solutions.get(cr))
                : Optional.empty();
    }
}
