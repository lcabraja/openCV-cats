/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Optional;
import org.opencv.core.Rect;

/**
 *
 * @author lcabraja
 */
public class CachedImage implements Serializable {

    private File file;
    private Rect[] results;
    private Optional<byte[]> image;

    private static final long serialVersionUID = 1L;

    public CachedImage() {
    }

    public CachedImage(File file, Rect[] results) {
        this.file = file;
        this.results = results;
    }

    public CachedImage(File file, Rect[] results, byte[] image) {
        this.file = file;
        this.results = results;
        this.image = Optional.of(image);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Rect[] getResults() {
        return results;
    }

    public void setResults(Rect[] results) {
        this.results = results;
    }

    public Optional<byte[]> getImage() {
        return image;
    }

    public void setImage(byte[] Image) {
        this.image = Image.length == 0 ? Optional.empty() : Optional.of(Image);
    }

    private String rectsToStrings() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.results.length; i++) {
            sb.append(this.results[i].x);
            sb.append(',');
            sb.append(this.results[i].y);
            sb.append(',');
            sb.append(this.results[i].width);
            sb.append(',');
            sb.append(this.results[i].height);
            sb.append(';');
        }
        return sb.toString();
    }

    private Rect[] stringToRects(String string) {
        final String[] strings = string.split(";");
        Rect[] results = new Rect[strings.length];
        for (int i = 0; i < strings.length; i++) {
            final String[] csv = strings[i].split(",");
            results[i] = new Rect(
                    Integer.parseInt(csv[0]),
                    Integer.parseInt(csv[1]),
                    Integer.parseInt(csv[2]),
                    Integer.parseInt(csv[3])
            );
        }
        return results;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(getFile());
        oos.writeUTF(rectsToStrings());
        oos.write(image);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        setFile((File) ois.readObject());
        setResults(stringToRects(ois.readUTF()));
        ois.read(image);
    }

}
