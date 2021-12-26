/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.model.BulkImageViewHolder;
import hr.algebra.model.DetailedImageViewHolder;
import hr.algebra.model.UIStateHolder;
import hr.algebra.rmi.ToggleService;
import hr.algebra.serving.Client;
import hr.algebra.serving.Server;
import hr.algebra.utils.DocumentationUtils;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.SerializationUtils;
import hr.algebra.utils.ViewUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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

    Optional<UIStateHolder> state;

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
            OpenCVCats.getMainStage().setUserData(new DetailedImageViewHolder(
                    getClass().getResource("views/MainMenu.fxml"),
                    uploadFile.get()
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
            OpenCVCats.getMainStage().setUserData(new BulkImageViewHolder(
                    uploadDirectory.get()
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
        DocumentationUtils.generateDocumentation();
    }

    private void loadLastValues() {
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
        UIStateHolder newState;
        if (state.isPresent()) {
            newState = new UIStateHolder(state.get().getLastFile(), Optional.of(folder));
        } else {
            newState = new UIStateHolder(Optional.empty(), Optional.of(folder));
        }
        SerializationUtils.updateSerializedItem(newState, SerializationUtils.UI_SERIALIZATION);
    }

    private void saveLastFile(String file) {
        UIStateHolder newState;
        if (state.isPresent()) {
            newState = new UIStateHolder(Optional.of(file), state.get().getLastFolder());
        } else {
            newState = new UIStateHolder(Optional.of(file), Optional.empty());
        }
        SerializationUtils.updateSerializedItem(newState, SerializationUtils.UI_SERIALIZATION);
    }

    @FXML
    public void btnListenToOtherPeopleWorking() throws IOException {
        System.out.println("listenToOtherPeopleWorking @ " + getClass().toString());
        ViewUtils.loadView(getClass().getResource("views/ListenerView.fxml"));
    }

    @FXML
    private void openLastFile(ActionEvent event) throws IOException {
        if (state.isPresent() && state.get().getLastFile().isPresent()) {
            OpenCVCats.getMainStage().setUserData(new DetailedImageViewHolder(
                    getClass().getResource("views/MainMenu.fxml"),
                    new File(state.get().getLastFile().get())
            ));
            ViewUtils.loadView(getClass().getResource("views/DetailedImageView.fxml"));
        }
    }

    @FXML
    private void openLastFolder(ActionEvent event) throws IOException {
        if (state.isPresent() && state.get().getLastFolder().isPresent()) {
            OpenCVCats.getMainStage().setUserData(
                    new BulkImageViewHolder(
                            new File(state.get().getLastFolder().get())));
            ViewUtils.loadView(getClass().getResource("views/BulkImageView.fxml"));
        }
    }

    @FXML
    private void clearSerialization(ActionEvent event) {
        new File(SerializationUtils.UI_SERIALIZATION).delete();
        loadLastValues();
    }

    @FXML
    private void makeJndiHappen(ActionEvent event) {
//        System.out.println("Client started!");
//        
//        Registry registry;
//        try {
//            registry = LocateRegistry.getRegistry();
//            System.out.println("Registry retrieved!");
//            ToggleService server = (ToggleService) registry
//                .lookup("MessengerService");
//            System.out.println("Service retrieved!");
//            server.toggleCss();
//        } catch (RemoteException ex) {
//            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NotBoundException ex) {
//            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
