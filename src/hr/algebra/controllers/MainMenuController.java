/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.model.BulkImageViewHolder;
import hr.algebra.model.DetailedImageViewHolder;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.ViewUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class MainMenuController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void openSelectFile() throws IOException {
        System.out.println("openSelectFile @ " + getClass().toString());

        Optional<File> uploadFile = FileUtils.uploadFile(OpenCVCats.getMainStage(), null, FileUtils.Extensions.JPG);

        if (uploadFile.isPresent()) {
            OpenCVCats.getMainStage().setUserData(new DetailedImageViewHolder(
                    getClass().getResource("views/MainMenu.fxml"),
                    uploadFile.get()
            ));
        } else {
            return;
        }
        ViewUtils.loadView(getClass().getResource("views/DetailedImageView.fxml"));
    }

    @FXML
    private void openSelectDirectory() throws IOException {
        System.out.println("openSelectDirectory @ " + getClass().toString());

        Optional<File> uploadDirectory = FileUtils.uploadDirectory(OpenCVCats.getMainStage(), null);

        if (uploadDirectory.isPresent()) {
            OpenCVCats.getMainStage().setUserData(new BulkImageViewHolder(
                    uploadDirectory.get()
            ));
        } else {
            return;
        }
        ViewUtils.loadView(getClass().getResource("views/BulkImageView.fxml"));
    }

    @FXML
    private void openUseCamera() throws IOException {
        System.out.println("openUseCamera @ " + getClass().toString());
        ViewUtils.loadView(getClass().getResource("views/CameraImageView.fxml"));
    }
}
