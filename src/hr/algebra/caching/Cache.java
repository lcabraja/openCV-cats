/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import hr.algebra.model.CachedResult;
import hr.algebra.model.Solution;
import java.io.File;
import java.util.Optional;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public interface Cache {
    public boolean contains(File imageFile, String classifierPath);
    public boolean containsSolution(File imageFile, String classifierPath);
    
    public void setFaceRects(File imageFile, Rect[] facesArray, String classifierPath);
    public Optional<CachedResult> getFaceRects(File imageFile, String classifierPath);
    
    public void setSolution(Solution solution);
    public Optional<Solution> getSolution(CachedResult cr);
}
