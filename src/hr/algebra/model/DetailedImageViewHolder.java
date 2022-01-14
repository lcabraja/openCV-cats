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

    private final URL returnResource;
    private final File imageFile;
    private final SerializableImage image;
    private final Optional<Object> returnHolder;

    public DetailedImageViewHolder(URL returnResource, File imageFile, SerializableImage image) {
        this.returnResource = returnResource;
        this.imageFile = imageFile;
        this.image = image;
        this.returnHolder = Optional.empty();
    }

    public DetailedImageViewHolder(URL returnResource, File imageFile, SerializableImage image, Object returnHolder) {
        this.returnResource = returnResource;
        this.imageFile = imageFile;
        this.image = image;
        this.returnHolder = Optional.of(returnHolder);
    }

    public DetailedImageViewHolder(URL returnResource, File imageFile, SerializableImage image, Optional<Object> returnHolder) {
        this.returnResource = returnResource;
        this.imageFile = imageFile;
        this.image = image;
        this.returnHolder = returnHolder;
    }

    public URL getReturnResource() {
        return returnResource;
    }

    public File getImageFile() {
        return imageFile;
    }

    public SerializableImage getImage() {
        return image;
    }

    public Optional<Object> getReturnHolder() {
        return returnHolder;
    }
}
