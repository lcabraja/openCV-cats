/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.model.DetailedImageViewHolder;
import hr.algebra.utils.ImageUtils;
import hr.algebra.utils.ViewUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class CameraImageViewController implements Initializable {

    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    private ScheduledExecutorService timer;

    @FXML
    private ImageView modifiedFrameTop;
    @FXML
    private ImageView modifiedFrameBottom;

    @FXML
    private ImageView originalFrame;
    private VideoCapture capture;
    private Boolean cameraActive;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.capture = new VideoCapture();
        faceCascade = new CascadeClassifier();
        faceCascade.load("resources/haarcascades/haarcascade_frontalcatface.xml"); // selects the haar cascade as the default cascade
        try {
            runCamera();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CameraImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void runCamera() throws FileNotFoundException {

        capture.open(0);

        if (this.capture.isOpened()) {
            cameraActive = true;
            Runnable imageUpdater = () -> {
                System.out.println("runnable @ " + getClass().toString());
                Mat frame = new Mat();

                // check if the capture is open
                if (this.capture.isOpened()) {
                    try {
                        // read the current frame
                        this.capture.read(frame);

                        // if the frame is not empty, process it
                        if (!frame.empty()) {
                            // face detection
                            detectAndDisplay(frame);
                            Image imageToShow = ImageUtils.mat2Image(frame);
                            ImageUtils.onFXThread(originalFrame.imageProperty(), imageToShow);
                        }
                    } catch (Exception e) {
                        // log the (full) error
                        System.err.println("Exception during the image elaboration: " + e);
                    }
                }
            };

            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(imageUpdater, 0, 500, TimeUnit.MILLISECONDS);
        }
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
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        Rect[] facesArray = faces.toArray();
        for (Rect facesArray1 : facesArray) {
            Imgproc.rectangle(frame, facesArray1.tl(), facesArray1.br(), new Scalar(0, 255, 0), 3);
        }
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

    // -------------------------------------------------------------------------
    // ----------------------------------                                UI code
    // -------------------------------------------------------------------------
    @FXML
    private void goBack() throws IOException {
        System.out.println("goBack @ " + getClass().toString());
        ViewUtils.loadView(getClass().getResource("views/MainMenu.fxml"));
    }

}
