/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class MainMenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void openSelectFile() throws IOException {
        System.out.println("openSelectFile @ " + getClass().toString());

        Optional<File> uploadFile = FileUtils.uploadFile(OpenCVCats.getMainStage(), null, FileUtils.Extensions.JPG);

        if (uploadFile.isPresent()) {
            OpenCVCats.getMainStage().setUserData(uploadFile.get());
        } else {
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("views/DetailedImageView.fxml"));
        Scene scene = new Scene(root);
        OpenCVCats.getMainStage().setScene(scene);
    }

    @FXML
    private void openSelectDirectory() throws IOException {
        System.out.println("openSelectDirectory @ " + getClass().toString());

        final DirectoryChooser directoryChooser
                = new DirectoryChooser();
        final File selectedDirectory
                = directoryChooser.showDialog(OpenCVCats.getMainStage());
        if (selectedDirectory != null) {
            selectedDirectory.getAbsolutePath();

            Parent root = FXMLLoader.load(getClass().getResource("views/BulkImageView.fxml"));
            Scene scene = new Scene(root);
            OpenCVCats.getMainStage().setScene(scene);
        }
    }

    @FXML
    private void openUseCamera() throws IOException {
        System.out.println("openUseCamera @ " + getClass().toString());
        Parent root = FXMLLoader.load(getClass().getResource("views/CameraImageView.fxml"));
        Scene scene = new Scene(root);
        OpenCVCats.getMainStage().setScene(scene);
    }
}
