/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.File;
import java.net.URL;
import java.util.Optional;

/**
 *
 * @author lcabraja
 */
public class DetailedImageViewHolder {

    private URL returnResource;
    private File imageFile;
    private Optional<Object> returnHolder;

    public DetailedImageViewHolder(URL returnResource, File imageFile) {
        this.returnResource = returnResource;
        this.imageFile = imageFile;
        this.returnHolder = Optional.empty();
    }

    public DetailedImageViewHolder(URL returnResource, File imageFile, Object returnHolder) {
        this(returnResource, imageFile);
        this.returnHolder = Optional.of(returnHolder);
    }
    
    public DetailedImageViewHolder(URL returnResource, File imageFile, Optional<Object> returnHolder) {
        this(returnResource, imageFile);
        this.returnHolder = returnHolder;
    }

    public URL getReturnResource() {
        return returnResource;
    }

    public File getImageFile() {
        return imageFile;
    }

    public Optional<Object> getReturnHolder() {
        return returnHolder;
    }
}
