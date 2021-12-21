/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.model.CachedFile;
import hr.algebra.serving.Client;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.util.Pair;
import org.opencv.core.Rect;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class ListenerController implements Initializable {

    @FXML
    private static TextArea taMessageQueue;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Client.startListening();
        Client.setListener(ListenerController::updateText);
    }

    public static void updateText(Pair<CachedFile, Rect[]> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("Analyzed: ");
        sb.append(data.getKey().getFilePath());
        sb.append(" using: ");
        sb.append(data.getKey().getClassifierPath());
        sb.append(" found ");
        sb.append(data.getValue().length);
        sb.append(" faces.");
        taMessageQueue.appendText(sb.toString());
    }

}
