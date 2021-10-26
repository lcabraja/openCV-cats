/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import hr.algebra.OpenCVCats;
import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author lcabraja
 */
public final class FileUtils {

    private FileUtils() {
    }

    public static final String DEFAULT_FILE_TITLE = "Select File";

    public static Optional<File> uploadFile(Stage stage, String title, FileUtils.Extensions... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle((title == null) ? DEFAULT_FILE_TITLE : title);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        if (extensions.length > 0) {
            Stream.of(extensions).forEach(extension -> {
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter(
                                extension.getTitle(), extension.getExtension()
                        )
                );
            });
        }

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            return Optional.of(file);
        }
//        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
//        chooser.setFileFilter(new FileNameExtensionFilter(description, extensions));
//        chooser.setDialogTitle(UPLOAD);
//        chooser.setApproveButtonText(UPLOAD);
//        chooser.setApproveButtonToolTipText(UPLOAD);
//        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = chooser.getSelectedFile();
//            String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
//            return Arrays.asList(extensions).contains(extension.toLowerCase()) ? Optional.of(selectedFile) : Optional.empty();            
//        }
        return Optional.empty();
    }

    public static Optional<File> uploadDirectory(Stage stage, String title) {
        final DirectoryChooser directoryChooser
                = new DirectoryChooser();
        directoryChooser.setTitle((title == null) ? DEFAULT_FILE_TITLE : title);
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            return Optional.of(selectedDirectory);
        }
        return Optional.empty();
    }

    public enum Extensions {
        ALLIMAGES("All Images", "*.png;*.jpg;*.jpeg;*.bmp"),
        PNG("PNG Files", "*.png"),
        JPG("JPG & JPEG Files", "*.jpg;*.jpeg"),
        BMP("BMP Files", "*.bmp");

        private final String title;
        private final String extension;

        private Extensions(String title, String extension) {
            this.title = title;
            this.extension = extension;
        }

        public String getTitle() {
            return title;
        }

        public String getExtension() {
            return extension;
        }
    }
}
