/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import hr.algebra.OpenCVCats;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author lcabraja
 */
public final class ViewUtils {

    private ViewUtils() {
    }

    public static void loadView(final URL resource) throws IOException {
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        OpenCVCats.getMainStage().setScene(scene);
    }
}
