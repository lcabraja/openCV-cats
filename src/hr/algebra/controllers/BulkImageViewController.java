/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.ViewUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class BulkImageViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
