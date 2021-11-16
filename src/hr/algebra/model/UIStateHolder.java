/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

/**
 *
 * @author lcabraja
 */
public class UIStateHolder implements Serializable {

    private Optional<String> lastFile;
    private Optional<String> lastFolder;

    public UIStateHolder(Optional<String> lastFile, Optional<String> lastFolder) {
        this.lastFile = lastFile;
        this.lastFolder = lastFolder;
    }

    public UIStateHolder(String lastFile, String lastFolder) {
        this.lastFile = lastFile == null ? Optional.empty() : Optional.of(lastFile);
        this.lastFolder = lastFolder == null ? Optional.empty() : Optional.of(lastFolder);
    }

    public Optional<String> getLastFile() {
        return lastFile;
    }

    public Optional<String> getLastFolder() {
        return lastFolder;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(getLastFile().orElse(""));
        oos.writeUTF(getLastFolder().orElse(""));
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        String tempLastFile = ois.readUTF();
        lastFile = tempLastFile.trim().isEmpty() ? Optional.empty() : Optional.of(tempLastFile);
        String tempLastFolder = ois.readUTF();
        lastFolder = tempLastFolder.trim().isEmpty() ? Optional.empty() : Optional.of(tempLastFolder);
    }

    @Override
    public String toString() {
        return "UIStateHolder{" + "lastFile=" + lastFile + ", lastFolder=" + lastFolder + '}';
    }
}
