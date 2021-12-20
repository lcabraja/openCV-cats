/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author lcabraja
 */
public class CachedFile implements Serializable {
    private String FilePath;
    private String ClassifierPath;

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }

    public String getClassifierPath() {
        return ClassifierPath;
    }

    public void setClassifierPath(String ClassifierPath) {
        this.ClassifierPath = ClassifierPath;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CachedFile other = (CachedFile) obj;
        if (!Objects.equals(this.FilePath, other.FilePath)) {
            return false;
        }
        if (!Objects.equals(this.ClassifierPath, other.ClassifierPath)) {
            return false;
        }
        return true;
    }
    
    
}
