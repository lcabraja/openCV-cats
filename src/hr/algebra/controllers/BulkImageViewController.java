/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.model.BulkImageViewHolder;
import hr.algebra.model.DetailedImageViewHolder;
import hr.algebra.model.SerializableImage;
import hr.algebra.rmi.DirectoryClient;
import hr.algebra.rmi.DirectoryServiceImpl;
import hr.algebra.serving.Client;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.ImageUtils;
import hr.algebra.utils.ViewUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
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
            initializeDirectories();
            // sets the selected image if present (when going back)
            if (holder.getSelectedIndex().isPresent()) {
                int index = (int) holder.getSelectedIndex().get();
                lvItems.getSelectionModel().select(index);
                lvItems.scrollTo(index);
                updateImage();
            }
        } catch (InterruptedException | FileNotFoundException ex) {
            Logger.getLogger(BulkImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeDirectories() {
        ObservableList<String> oblist;
        if (holder.getDirectoryContents() != null) {
            oblist = FXCollections.observableList(
                    holder
                            .getDirectoryContents()
                            .stream().map((file) -> file.getName())
                            .collect(Collectors.toList())
            );
            // set RMI data
            DirectoryServiceImpl.setStaticDirectory(holder.getSelectedDirectory().getAbsolutePath());
            DirectoryServiceImpl.setStaticFiles(new ArrayList<>(holder.getDirectoryContents()));
        } else {
            oblist = FXCollections.observableList(
                    new DirectoryClient().getFiles()
                            .stream().map((file) -> file.getName())
                            .collect(Collectors.toList())
            );
        }
        lvItems.setItems(oblist);

    }

    @FXML
    private void vwItemsClicked(MouseEvent event) throws FileNotFoundException, InterruptedException {
        updateImage();
    }

    @FXML
    private void vwItemsPressed(KeyEvent event) throws FileNotFoundException, InterruptedException {
        updateImage();
    }

    private File getSelectedImageFile() {
        String fileName = (String) lvItems.getSelectionModel().getSelectedItem();
        File file = new File(holder.getSelectedDirectory() + File.separator + fileName);
        return file;
    }

    private void updateImage() throws InterruptedException, FileNotFoundException {
        SerializableImage serializableImage = getCurrentSerializableImage();
        ImageUtils.onFXThread(ivPreview.imageProperty(), serializableImage.getImage());
    }

    private SerializableImage getCurrentSerializableImage() throws InterruptedException, FileNotFoundException {
        return getSerializableImage(getSelectedImageFile());
    }

    private SerializableImage getSerializableImage(File selectedImagefile) {
        try {
            return serializableImage(selectedImagefile);
        } catch (InterruptedException | ExecutionException | FileNotFoundException ex) {
            Logger.getLogger(BulkImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private SerializableImage serializableImage(File selectedImageFile) throws InterruptedException, ExecutionException, FileNotFoundException {
        if (holder.isOnline()) {
            return new SerializableImage(getImageFromNetwork(selectedImageFile));
        } else {
            return new SerializableImage(getImageFromFileSystem(selectedImageFile));
        }
    }

    private Image getImageFromNetwork(File selectedImageFile) throws InterruptedException, ExecutionException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<Image> result = es.submit(
                () -> new Client().requestImageSync(selectedImageFile).getImage()
        );
        return result.get();
    }

    private Image getImageFromFileSystem(File file) throws FileNotFoundException {
        InputStream is = new FileInputStream(file);
        Image image = new Image(is);
        return image;
    }

    // ------------------------------------------------------------- Buttons ---
    @FXML
    private void goToDetailsPage() throws IOException, InterruptedException {
        System.out.println("goToDetailsPage @ " + getClass().toString());
        File uploadFile = getSelectedImageFile();
        OpenCVCats.getMainStage().setUserData(new DetailedImageViewHolder(
                getClass().getResource("views/BulkImageView.fxml"),
                uploadFile,
                getCurrentSerializableImage(),
                // TODO: ask if its better to reconstruct the object here or to modify the index parameter
                new BulkImageViewHolder(
                        holder.getSelectedDirectory(),
                        FileUtils.listDirectoryContents(holder.getSelectedDirectory()),
                        holder.isOnline(),
                        lvItems.getSelectionModel().getSelectedIndex()
                )
        ));
        ViewUtils.loadView(getClass().getResource("views/DetailedImageView.fxml"));
    }

    @FXML
    private void goBack() throws IOException {
        System.out.println("goBack @ " + getClass().toString());
        ViewUtils.loadView(getClass().getResource("views/MainMenu.fxml"));
    }
}
