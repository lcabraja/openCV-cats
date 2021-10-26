/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.utils.ImageUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.core.Size;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class DetailedImageViewController implements Initializable {

    private File setImage;

    @FXML
    private ImageView originalFrame;
    @FXML
    private ImageView modifiedFrameTop;
    @FXML
    private ImageView modifiedFrameBottom;
    @FXML
    private RadioButton rbHaar;
    @FXML
    private RadioButton rbLbp;

    final ToggleGroup group = new ToggleGroup();

    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    private BufferedImage bufferedImage;
    private ScheduledExecutorService timer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize @ " + getClass().toString());
        initFields();
        try {
            initImageAnalysis();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DetailedImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Runnable setupRunnable() {
        System.out.println("setupRunnable @ " + getClass().toString());
        OpenCVCats.getMainStage()
                .setOnCloseRequest((WindowEvent t) -> {
                    Platform.exit();
                    System.exit(0);
                });
        return () -> {
            try {
                System.out.println("runnable @ " + getClass().toString());
                if (bufferedImage != null) {
                    Mat frame = grabFrame();
                    Image imageToShow = ImageUtils.mat2Image(frame);
                    updateImageView(imageToShow);
                }
            } catch (Exception e) {
                System.out.println("setupRunnable.catch @ " + getClass().toString());
            }
        };
    }

    private void updateImageView(Image imageToShow) {
        System.out.println("updateImageView @ " + getClass().toString());
        ImageUtils.onFXThread(originalFrame.imageProperty(), imageToShow);
    }

    private Image initImage() throws FileNotFoundException {
        //return new Image("https://i.imgur.com/QUxuGcL.jpeg");
        setImage = ((File) OpenCVCats.getMainStage().getUserData());
        InputStream is = new FileInputStream(setImage);
        return new Image(is);
    }

    private void initImageAnalysis() throws FileNotFoundException {
        radioSelection("resources/haarcascades/haarcascade_frontalface_alt.xml");
        Image image = initImage();
        bufferedImage = SwingFXUtils.fromFXImage(image, null);
        Runnable imageUpdater = setupRunnable();
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(imageUpdater, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void initFields() {
        rbHaar.setToggleGroup(group);
        rbLbp.setToggleGroup(group);
        rbHaar.setSelected(true);
        faceCascade = new CascadeClassifier();
        absoluteFaceSize = 0;
    }

    private void detectAndDisplay(Mat frame) {
        System.out.println("detectAndDisplay @ " + getClass().toString());
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);
        // compute minimum face size (20% of the frame height, in our case)
        if (absoluteFaceSize == 0) {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0) {
                absoluteFaceSize = Math.round(height * 0.2f);
            }
        }
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        Rect[] facesArray = faces.toArray();
        for (Rect facesArray1 : facesArray) {
            Imgproc.rectangle(frame, facesArray1.tl(), facesArray1.br(), new Scalar(0, 255, 0), 3);
        }
    }

    @FXML
    private void goBack() throws IOException {
        System.out.println("goBack @ " + getClass().toString());
        stopAcquisition();
        Parent root = FXMLLoader.load(getClass().getResource("views/MainMenu.fxml"));
        Scene scene = new Scene(root);
        OpenCVCats.getMainStage().setScene(scene);
    }

    @FXML
    protected void haarSelected(Event event) {
        System.out.println("haarSelected @ " + getClass().toString());
        if (rbLbp.isSelected()) {
            rbLbp.setSelected(false);
        }
        radioSelection("resources/haarcascades/haarcascade_frontalface_alt.xml");
    }

    @FXML
    protected void lbpSelected(Event event) {
        System.out.println("lbpSelected @ " + getClass().toString());
        if (rbHaar.isSelected()) {
            rbHaar.setSelected(false);
        }
        radioSelection("resources/lbpcascades/lbpcascade_frontalface.xml");
    }

    private void radioSelection(String classifierPath) {
        System.out.println("checkboxSelection @ " + getClass().toString());
        faceCascade.load(classifierPath);
    }

    private Mat grabFrame() {
        System.out.println("grabFrame @ " + getClass().toString());
        Mat frame;
        frame = ImageUtils.bufferedImageToMat(bufferedImage);
        if (!frame.empty()) {
            detectAndDisplay(frame);
        }
        return frame;
    }

    private void stopAcquisition() {
        if (timer != null && !timer.isShutdown()) {
            try {
                timer.shutdown();
                timer.awaitTermination(33, TimeUnit.MILLISECONDS);

            } catch (InterruptedException ex) {
                Logger.getLogger(DetailedImageViewController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
