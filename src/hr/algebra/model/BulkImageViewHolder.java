/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author lcabraja
 */
public class BulkImageViewHolder {

    private final File selectedDirectory;
    private final List<File> directoryContents;
    private final Optional<Integer> selectedIndex;
    private final boolean online;

    public BulkImageViewHolder(File selectedDirectory, List<File> directoryContents) {
        this.selectedDirectory = selectedDirectory;
        this.directoryContents = directoryContents;
        this.selectedIndex = Optional.empty();
        online = false;
    }

    public BulkImageViewHolder(File selectedDirectory, List<File> directoryContents, Optional<Integer> selectedIndex) {
        this.selectedDirectory = selectedDirectory;
        this.directoryContents = directoryContents;
        this.selectedIndex = selectedIndex;
        online = false;
    }

    public BulkImageViewHolder(File selectedDirectory, List<File> directoryContents, int selectedIndex) {
        this.selectedDirectory = selectedDirectory;
        this.directoryContents = directoryContents;
        this.selectedIndex = Optional.of(selectedIndex);
        online = false;
    }

    public BulkImageViewHolder(File selectedDirectory, List<File> directoryContents, boolean online) {
        this.selectedDirectory = selectedDirectory;
        this.directoryContents = directoryContents;
        this.selectedIndex = Optional.empty();
        this.online = online;
    }

    public BulkImageViewHolder(File selectedDirectory, List<File> directoryContents, boolean online, Optional<Integer> selectedIndex) {
        this.selectedDirectory = selectedDirectory;
        this.directoryContents = directoryContents;
        this.selectedIndex = selectedIndex;
        this.online = online;
    }

    public BulkImageViewHolder(File selectedDirectory, List<File> directoryContents, boolean online, int selectedIndex) {
        this.selectedDirectory = selectedDirectory;
        this.directoryContents = directoryContents;
        this.selectedIndex = Optional.of(selectedIndex);
        this.online = online;
    }

    public File getSelectedDirectory() {
        return selectedDirectory;
    }

    public Optional<Integer> getSelectedIndex() {
        return selectedIndex;
    }

    public List<File> getDirectoryContents() {
        return directoryContents;
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        return "BulkImageViewHolder{" + "selectedDirectory=" + selectedDirectory + ", selectedIndex=" + selectedIndex + '}';
    }
}
