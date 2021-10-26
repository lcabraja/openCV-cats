/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import java.io.IOException;
import java.net.URL;
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
public class CameraImageViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
     @FXML
    private void goBack() throws IOException {
        System.out.println("goBack @ " + getClass().toString());
        Parent root = FXMLLoader.load(getClass().getResource("views/MainMenu.fxml"));
        Scene scene = new Scene(root);
        OpenCVCats.getMainStage().setScene(scene);
    }
    
}
