/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import hr.algebra.controllers.MainMenuController;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author lcabraja
 */
public class DocumentationUtils {

    static final String PACKAGE_LOCATION = ".\\src";

    private DocumentationUtils() {
    }

    public static void generirajDokumentaciju() {
        System.out.println("generirajDokumentaciju @ " + DocumentationUtils.class.toString());
        
        StringBuilder builder = new StringBuilder();

        builder.append("<!DOCTYPE html>\n");
        builder.append("<html>\n");
        builder.append("<head>\n");
        builder.append("<title>Documentation</title>\n");
        builder.append("</head>\n");
        builder.append("<body>\n");
        builder.append("<H1>Package list and basic info</H1>\n");

        String[] filesAndFoldersInSrc = new File(PACKAGE_LOCATION).list();
        recursivelyGenerateDocumentation(builder, 2, "");

        for (String packageName : filesAndFoldersInSrc) {
            String[] classes = new File(PACKAGE_LOCATION + "\\" + packageName).list();
            for (String className : classes) {
            }
        }

        builder.append("</body>\n");
        builder.append("</html>\n");

        try (FileWriter fw = new FileWriter("documentation.html")) {
            fw.write(builder.toString());
            spawnSuccessAlert();
        } catch (IOException ex) {
            spawnExceptionAlert();
        }
    }

    public static void spawnSuccessAlert() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Documentation successfully generated");
        alert.setHeaderText("Class documentation has been successfully generated in the file \"documentation.html\"");
        alert.setContentText("Press OK to view");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            File htmlFile = new File("documentation.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
        }
    }

    public static void spawnExceptionAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Failed to generate documentation");
        alert.setHeaderText("There was an error generating the documentation file \"documentation.html\"");
        alert.setContentText("Please check that the application ahs filesystem access or that the disk is not full and try again");
        alert.showAndWait();
    }

    private static void recursivelyGenerateDocumentation(StringBuilder builder, int header, String relativeLocation) {
        File workFile = new File(PACKAGE_LOCATION + "\\" + relativeLocation);
        String fullyQualifiedClassName = getFullyQualifiedClassName(relativeLocation);

        if (workFile.isDirectory()) {
            builder.append("<H").append(header).append(">");
            builder.append(fullyQualifiedClassName);
            builder.append("</H").append(header).append(">\n");

            for (String recursiveLevel : workFile.list()) {
                System.out.println("recursiveLevel " + recursiveLevel);
                if (relativeLocation.isEmpty()) {
                    recursivelyGenerateDocumentation(builder, header + 1, recursiveLevel);
                } else {
                    recursivelyGenerateDocumentation(builder, header + 1, relativeLocation + "\\" + recursiveLevel);
                }
            }
        } else {
            if (!relativeLocation.endsWith(".java")) {
                return;
            }
            //System.out.println(fullyQualifiedClassName + " H" + String.valueOf(header));
            appendHTMLforClass(builder, fullyQualifiedClassName, header);
        }
    }

    private static String getFullyQualifiedClassName(String relativeLocation) {
        String fullyQualifiedClassName;
        try {
            String[] split;
            if (relativeLocation.endsWith(".java")) {
                split = relativeLocation.substring(0, relativeLocation.length() - 5).split("\\\\");
            } else {
                split = relativeLocation.split("\\\\");
            }
            fullyQualifiedClassName = Arrays
                    .stream(split)
                    .collect(Collectors.joining("."));
        } catch (Exception ex) {
            fullyQualifiedClassName = relativeLocation;
        }
        return fullyQualifiedClassName;
    }

    private static void appendHTMLforClass(StringBuilder builder, String fullyQualifiedClassName, int header) throws SecurityException {
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

    private static void fromModifierValue(int modifiers, final StringBuilder builder) {
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
