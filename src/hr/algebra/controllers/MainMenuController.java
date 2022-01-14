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
import hr.algebra.model.UIStateHolder;
import hr.algebra.rmi.DirectoryClient;
import hr.algebra.threading.ThreadHelper;
import hr.algebra.utils.DocumentationUtils;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.ImageUtils;
import hr.algebra.utils.JNDIUtils;
import hr.algebra.utils.SerializationUtils;
import hr.algebra.utils.ViewUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class MainMenuController implements Initializable {

    @FXML
    private Button btnLastFile;
    @FXML
    private Button btnLastFolder;

    @FXML
    private ImageView ivGlassPane;

    Optional<UIStateHolder> state;
    ThreadHelper threadHelper;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnLastFile.setVisible(false);
        btnLastFolder.setVisible(false);
        loadLastValues();
    }

    @FXML
    private void openSelectFile() throws IOException {
        System.out.println("openSelectFile @ " + getClass().toString());

        Optional<File> uploadFile = FileUtils.uploadFile(OpenCVCats.getMainStage(), null, FileUtils.Extensions.JPG);

        if (uploadFile.isPresent()) {
            OpenCVCats
                    .getMainStage()
                    .setUserData(new DetailedImageViewHolder(
                            getClass().getResource("views/MainMenu.fxml"),
                            uploadFile.get(),
                            new SerializableImage(new Image(new FileInputStream(uploadFile.get())))
                    ));
            saveLastFile(uploadFile.get().getAbsolutePath());
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
            OpenCVCats
                    .getMainStage()
                    .setUserData(new BulkImageViewHolder(
                            uploadDirectory.get(),
                            JNDIUtils.listDirectoryContents(uploadDirectory.get(), FileUtils.Extensions.JPG)
                    ));
            saveLastDirectory(uploadDirectory.get().getAbsolutePath());
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

    @FXML
    private void openDocumentation() {
        System.out.println("openDocumentation @ " + getClass().toString());
        DocumentationUtils.generateDocumentation();
    }

    private void loadLastValues() {
        System.out.println("loadLastValues @ " + getClass().toString());
        Optional<UIStateHolder> serializedFile
                = SerializationUtils.<UIStateHolder>fetchSerializaedItem(SerializationUtils.UI_SERIALIZATION);
        if (serializedFile.isPresent()) {
            state = serializedFile;
            btnLastFile.setVisible(state.get().getLastFile().isPresent());
            btnLastFolder.setVisible(state.get().getLastFolder().isPresent());

        } else {
            state = Optional.empty();
            btnLastFile.setVisible(false);
            btnLastFolder.setVisible(false);
        }
    }

    private void saveLastDirectory(String folder) {
        System.out.println("saveLastDirectory @ " + getClass().toString());
        UIStateHolder newState;
        if (state.isPresent()) {
            newState = new UIStateHolder(state.get().getLastFile(), Optional.of(folder));
        } else {
            newState = new UIStateHolder(Optional.empty(), Optional.of(folder));
        }
        SerializationUtils.updateSerializedItem(newState, SerializationUtils.UI_SERIALIZATION);
    }

    private void saveLastFile(String file) {
        System.out.println("saveLastFile @ " + getClass().toString());
        UIStateHolder newState;
        if (state.isPresent()) {
            newState = new UIStateHolder(Optional.of(file), state.get().getLastFolder());
        } else {
            newState = new UIStateHolder(Optional.of(file), Optional.empty());
        }
        SerializationUtils.updateSerializedItem(newState, SerializationUtils.UI_SERIALIZATION);
    }

    @FXML
    private void connectToNetwork(ActionEvent event) throws IOException {
        System.out.println("connectToNetwork @ " + getClass().toString());
        DirectoryClient stub = new DirectoryClient();
        final String directoryPath = stub.getDirectoryPath();
        if (directoryPath != null) {
            File directory = new File(directoryPath);
            OpenCVCats
                    .getMainStage()
                    .setUserData(new BulkImageViewHolder(
                            directory,
                            null,
                            true
                    ));
            saveLastDirectory(directoryPath);
            ViewUtils.loadView(getClass().getResource("views/BulkImageView.fxml"));
        }
    }

    @FXML
    private void openLastFile(ActionEvent event) throws IOException {
        System.out.println("openLastFile @ " + getClass().toString());
        if (state.isPresent() && state.get().getLastFile().isPresent()) {
            final File file = new File(state.get().getLastFile().get());
            OpenCVCats
                    .getMainStage()
                    .setUserData(new DetailedImageViewHolder(
                            getClass().getResource("views/MainMenu.fxml"), file,
                            new SerializableImage(new Image(new FileInputStream(file)))
                    ));
            ViewUtils.loadView(getClass().getResource("views/DetailedImageView.fxml"));
        }
    }

    @FXML
    private void openLastFolder(ActionEvent event) throws IOException {
        System.out.println("openLastFolder @ " + getClass().toString());
        if (state.isPresent() && state.get().getLastFolder().isPresent()) {
            File directory = new File(state.get().getLastFolder().get());
            OpenCVCats
                    .getMainStage()
                    .setUserData(new BulkImageViewHolder(
                            directory,
                            JNDIUtils.listDirectoryContents(directory, FileUtils.Extensions.JPG)
                    ));
            ViewUtils.loadView(getClass().getResource("views/BulkImageView.fxml"));
        }
    }

    @FXML
    private void downloadFolder(ActionEvent event) {
        if (!threadHelper.launched) {
            System.out.println("downloadFolder @ " + getClass().toString());
//        Optional<File> uploadDirectory = FileUtils.uploadDirectory(OpenCVCats.getMainStage(), null);
//        Optional<File> downloadDirectory = FileUtils.uploadDirectory(OpenCVCats.getMainStage(), null);
//        if (uploadDirectory.isPresent() && downloadDirectory.isPresent()) {
//            ThreadHelper.setFilesToSend(JNDIUtils.listDirectoryContents(uploadDirectory.get(), FileUtils.Extensions.JPG));
            List<File> filesToSend = JNDIUtils.listDirectoryContents(new File("C:\\Users\\lcabraja\\Desktop\\soruce"), FileUtils.Extensions.JPG);
            File destination = new File("C:\\Users\\lcabraja\\Desktop\\temp");
            threadHelper = new ThreadHelper(filesToSend, destination);
            threadHelper.launchThreads(10);
//        }
        } else {
            System.out.println(threadHelper);
        }
    }

    @FXML
    private void openSettings(ActionEvent event) throws IOException {
        System.out.println("openSettings @ " + getClass().toString());
        ViewUtils.loadView(getClass().getResource("views/SettingsView.fxml"));
    }

    @FXML
    private void clearSerialization(ActionEvent event) {
        System.out.println("clearSerialization @ " + getClass().toString());
        new File(SerializationUtils.UI_SERIALIZATION).delete();
        loadLastValues();
    }
}
