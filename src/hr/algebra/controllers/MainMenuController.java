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
import hr.algebra.utils.ReflectionUtils;
import hr.algebra.utils.ViewUtils;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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

    @FXML
    private void openDocumentation() {
        generirajDokumentaciju();
    }

    static final String PACKAGE_LOCATION = ".\\src";

    public void generirajDokumentaciju() {

        StringBuilder builder = new StringBuilder();

        builder.append("<!DOCTYPE html>\n");
        builder.append("<html>\n");
        builder.append("<head>\n");
        builder.append("<title>Documentation</title>\n");
        builder.append("</head>\n");
        builder.append("<body>\n");
        builder.append("<h1>Package list and basic info</h1>\n");
        //(builder.append("<p>This is a paragraph.</p>\n");

        String[] filesAndFoldersInSrc = new File(PACKAGE_LOCATION).list();

        for (String packageName : filesAndFoldersInSrc) {
            String[] classes = new File(PACKAGE_LOCATION + "\\" + packageName).list();
            for (String className : classes) {
                recursivelyGenerateDocumentation(builder, 2, packageName + "\\" + className);
            }
        }

        builder.append("</body>\n");
        builder.append("</html>\n");

        try (FileWriter zapisivac = new FileWriter("documentation.html")) {
            zapisivac.write(builder.toString());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Documentation successfully generated");
            alert.setHeaderText(
                    "Class documentation has been successfully generated in the file \"documentation.html\"");
            alert.setContentText(
                    "Press OK to view");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                File htmlFile = new File("documentation.html");
                Desktop.getDesktop().browse(htmlFile.toURI());
            }

        } catch (IOException ex) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Neuspješno generiranje dokumentacije");
            alert.setHeaderText(
                    "Dogodila se pogreška tijekom generiranja datoteke"
                    + " s dokumentacijom!");
            alert.setContentText(
                    "Datoteka \"dokumentacija.html\" nije uspješno "
                    + "generirana!");

            alert.showAndWait();
        }
    }

    private void recursivelyGenerateDocumentation(StringBuilder builder, int header, String relativeLocation) {
        File workFile = new File(PACKAGE_LOCATION + "\\" + relativeLocation);
        if (workFile.isDirectory()) {
            builder.append("<H").append(header).append(">");
            builder.append(relativeLocation);
            builder.append("</H").append(header).append(">\n");

            for (String recursiveLevel : workFile.list()) {
                recursivelyGenerateDocumentation(builder, header + 1, relativeLocation + "\\" + recursiveLevel);
            }
        } else {
            if (!relativeLocation.endsWith(".java")) {
                return;
            }
            String[] split = relativeLocation.substring(0, relativeLocation.length() - 5).split("\\\\");
            String fullyQualifiedClassName = Arrays
                    .stream(split)
                    .collect(Collectors.joining("."));
            System.out.println(fullyQualifiedClassName + " " + String.valueOf(header));
            appendHTMLforClass(builder, fullyQualifiedClassName, header);
        }
    }

    private void appendHTMLforClass(StringBuilder builder, String fullyQualifiedClassName, int header) throws SecurityException {
        builder.append("<H").append(header).append(">");
        builder.append(fullyQualifiedClassName);
        builder.append("</H").append(header).append(">");

        try {
            Class clazz = Class.forName(fullyQualifiedClassName);
            ReflectionUtils.readMembersInfo(clazz, builder, "<br/>\n");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fromModifierValue(int modifiers, final StringBuilder builder) {
        if (Modifier.isFinal(modifiers)) {
            builder.append("final ");
        }
        if (Modifier.isPrivate(modifiers)) {
            builder.append("private ");
        }
        if (Modifier.isProtected(modifiers)) {
            builder.append("protected ");
        }
        if (Modifier.isPublic(modifiers)) {
            builder.append("public ");
        }
        if (Modifier.isStatic(modifiers)) {
            builder.append("static ");
        }
        if (Modifier.isAbstract(modifiers)) {
            builder.append("abstract ");
        }
        if (Modifier.isSynchronized(modifiers)) {
            builder.append("synchronized ");
        }
    }
}
