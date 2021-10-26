/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.utils.ViewUtils;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class BulkImageViewController implements Initializable {
    
    @FXML
    private ListView lvItems;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize @ " + getClass().toString());
        try {
            File directory = ((File) OpenCVCats.getMainStage().getUserData());
            FileFilter fileFilter = new FileFilter() {
                public boolean accept(File dir) {
                    if (dir.isFile()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            File[] list = directory.listFiles(fileFilter);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            Stream.of(list).forEach((item) -> {
                oblist.add(item.toString());
            });
            lvItems.setItems(oblist);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("initialize.catch @ " + getClass().toString());
        }
    }
    
    @FXML
    private void vwItemsClicked(MouseEvent event) {
        
    }
    
    @FXML
    private void goToDetailsPage() throws IOException {
        System.out.println("goToDetailsPage @ " + getClass().toString());
        Optional<File> uploadFile = getSelectedFile();
        if (uploadFile.isPresent()) {
            OpenCVCats.getMainStage().setUserData(uploadFile.get());
        } else {
            return;
        }
        final String view = "views/DetailedImageView.fxml";
        ViewUtils.loadView(getClass().getResource("views/DetailedImageView.fxml"));
    }
    
    @FXML
    private void goBack() throws IOException {
        System.out.println("openUseCamera");
        ViewUtils.loadView(getClass().getResource("views/MainMenu.fxml"));
    }
    
    private Optional<File> getSelectedFile() {
        return Optional.empty();
    }
}
