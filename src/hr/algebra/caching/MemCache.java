/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import hr.algebra.model.CachedResult;
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

//    private Map<CachedFile, Rect[]>> 
    private final Map<CachedResult, Rect[]> faces = new HashMap<>();

    @Override
    public boolean contains(File imageFile, String classifierPath) {
        System.out.println("contains @ " + getClass().toString());
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        if (faces.containsKey(cf)) {
            return faces.containsKey(cf);
        }
        return false;
    }

    @Override
    public Optional<Rect[]> getFaceRects(File imageFile, String classifierPath) {
        System.out.println("getFaceRects @ " + getClass().toString());
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        if (faces.containsKey(cf)) {
            return Optional.of(faces.get(cf));
        }
        return Optional.empty();
    }

    @Override
    public void setFaceRects(File imageFile, Rect[] facesArray, String classifierPath) {
        System.out.println("setFaceRects @ " + getClass().toString());
        CachedResult cf = new CachedResult() {
            {
                setFilePath(imageFile.getAbsolutePath());
                setClassifierPath(classifierPath);
            }
        };
        faces.put(cf, facesArray);
    }
}
