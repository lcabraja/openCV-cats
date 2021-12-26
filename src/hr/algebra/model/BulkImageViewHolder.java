/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.File;
import java.util.Optional;

/**
 *
 * @author lcabraja
 */
public class BulkImageViewHolder {

    private final File selectedDirectory;
    private final Optional<Integer> selectedIndex;

    public BulkImageViewHolder(File selectedDirectory) {
        this.selectedDirectory = selectedDirectory;
        this.selectedIndex = Optional.empty();
    }

    public BulkImageViewHolder(File selectedDirectory, Optional<Integer> selectedIndex) {
        this.selectedDirectory = selectedDirectory;
        this.selectedIndex = selectedIndex;
    }

    public BulkImageViewHolder(File selectedDirectory, int selectedIndex) {
        this.selectedDirectory = selectedDirectory;
        this.selectedIndex = Optional.of(selectedIndex);
    }

    public File getSelectedDirectory() {
        return selectedDirectory;
    }

    public Optional<Integer> getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public String toString() {
        return "BulkImageViewHolder{" + "selectedDirectory=" + selectedDirectory + ", selectedIndex=" + selectedIndex + '}';
    }
}
