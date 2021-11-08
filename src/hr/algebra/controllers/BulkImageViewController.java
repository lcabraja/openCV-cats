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
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class BulkImageViewController implements Initializable {

    @FXML
    private ListView lvItems;
    @FXML
    private ImageView ivPreview;

    private BulkImageViewHolder holder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize @ " + getClass().toString());
        try {
            holder = ((BulkImageViewHolder) OpenCVCats.getMainStage().getUserData());
            FileFilter fileFilter
                    = (File dir) -> dir.isFile() && FileUtils.extensionOf(dir, FileUtils.Extensions.ALLIMAGES);
            File[] list = holder.getSelectedDirectory().listFiles(fileFilter);
            ObservableList<String> oblist = FXCollections.observableArrayList();
            Stream.of(list).forEach((item) -> {
                String[] path = item.toString().split("\\\\");
                String fileName = path[path.length - 1];
                oblist.add(fileName.trim());
            });
            lvItems.setItems(oblist);
            if (holder.getSelectedIndex().isPresent()) {
                lvItems.getSelectionModel().select((int) holder.getSelectedIndex().get());
                updateImage();
            }
        } catch (Exception e) {
            System.out.println("initialize.catch @ " + getClass().toString() + "\n" + e.toString());
        }
    }

    @FXML
    private void vwItemsClicked(MouseEvent event) throws FileNotFoundException {
        updateImage();
    }

    @FXML
    private void vwItemsPressed(KeyEvent event) throws FileNotFoundException {
        updateImage();
    }

    private void updateImage() throws FileNotFoundException {
        File file = getSelectedImageFile();
        InputStream is = new FileInputStream(file);
        Image image = new Image(is);
        ivPreview.setImage(image);
    }

    private File getSelectedImageFile() {
        String fileName = (String) lvItems.getSelectionModel().getSelectedItem();
        File file = new File(holder.getSelectedDirectory() + File.separator + fileName);
        return file;
    }

    @FXML
    private void goToDetailsPage() throws IOException {
        System.out.println("goToDetailsPage @ " + getClass().toString());
        Optional<File> uploadFile = getSelectedFile();
        if (uploadFile.isPresent()) {
            OpenCVCats.getMainStage().setUserData(new DetailedImageViewHolder(
                    getClass().getResource("views/BulkImageView.fxml"),
                    uploadFile.get(),
                    new BulkImageViewHolder(
                            holder.getSelectedDirectory(),
                            lvItems.getSelectionModel().getSelectedIndex()
                    )
            ));
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
        try {
            File selectedFile = getSelectedImageFile();
            InputStream is = new FileInputStream(selectedFile);
            return Optional.of(selectedFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BulkImageViewController.class.getName()).log(Level.SEVERE, null, ex);
            return Optional.empty();
        }
    }
}
