/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.model.SerializableImage;
import hr.algebra.serving.LiveClient;
import hr.algebra.serving.LiveServer;
import hr.algebra.utils.ImageUtils;
import hr.algebra.utils.ViewUtils;
import hr.algebra.opencv.CascadeClassifierEnum;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private static final int MAX_TESTED = 10;
    private static int cameraCount;
    private final int currentCamera = 0;

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
    private boolean hasCamera = true;

    /**
     * Initializes the controller class.
     */
    int countCameras() {
        int i = -1;
        VideoCapture temp_camera = new VideoCapture(0);
        while (i < MAX_TESTED && temp_camera.isOpened()) {
            temp_camera.release();
            temp_camera.open(++i);
        }
        return i;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cameraCount = countCameras();
        faceCascade = new CascadeClassifier();
        faceCascade.load(CascadeClassifierEnum.HAARCASCADE.toString()); // selects the haar cascade as the default cascade
        runCamera();
    }

    private void runCamera() {
        this.capture = new VideoCapture(0);
        Runnable imageUpdater = () -> {
            System.out.println("runnable @ " + getClass().toString());
            Mat frame = new Mat();

            try {
                // read the current frame
                if (cameraCount > 0 && hasCamera) {
                    this.capture.read(frame);

                    // if the frame is not empty, process it
                    if (!frame.empty()) {
                        // face detection
                        System.out.println("if" + currentCamera);
                        detectAndDisplay(frame);
                        Image imageToShow = ImageUtils.mat2Image(frame);
                        ImageUtils.onFXThread(originalFrame.imageProperty(), imageToShow);
                        LiveServer.serializableImage = new SerializableImage(imageToShow);
                    } else {
                        System.out.println("else");
                        if (currentCamera >= cameraCount) {
                            hasCamera = false;
                        } else {
                            this.capture.release();
                            this.capture.open(++cameraCount);
                        }
                    }
                } else {
                    System.out.println("Reguesting new image frame");
                    LiveClient.requestImage(originalFrame.imageProperty());
                }
            } catch (Exception e) {
                // log the (full) error
                System.err.println("Exception during the image capture: " + e);
            }
        };

        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(imageUpdater, 0, 100, TimeUnit.MILLISECONDS);
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
        stopAcquisition();
        ViewUtils.loadView(getClass().getResource("views/MainMenu.fxml"));
    }

}
