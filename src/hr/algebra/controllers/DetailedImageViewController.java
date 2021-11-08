/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.model.BulkImageViewHolder;
import hr.algebra.model.DetailedImageViewHolder;
import hr.algebra.utils.ImageUtils;
import hr.algebra.utils.ViewUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    // -------------------------------------------------------------------------
    // ----------------------------------                                 fields
    // -------------------------------------------------------------------------
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

    private int absoluteFaceSize = 0;
    private DetailedImageViewHolder holder;
    private CascadeClassifier faceCascade;
    private BufferedImage bufferedImage;
    private File setImage;

    // -------------------------------------------------------------------------
    // ----------------------------------                                  inits
    // -------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize @ " + getClass().toString());
        initFields();
        analyzeImage();
    }

    private void initFields() {
        holder = ((DetailedImageViewHolder) OpenCVCats.getMainStage().getUserData());
        rbHaar.setToggleGroup(group);
        rbLbp.setToggleGroup(group);
        rbHaar.setSelected(true);
        faceCascade = new CascadeClassifier();
        radioSelection("resources/haarcascades/haarcascade_frontalcatface.xml"); // selects the haar cascade as the default cascade
    }

    // -------------------------------------------------------------------------
    // ----------------------------------                              rendering
    // -------------------------------------------------------------------------
    private void analyzeImage() {
        try {
            setImage = holder.getImageFile();
            bufferedImage = SwingFXUtils.fromFXImage(new Image(new FileInputStream(setImage)), null);
            if (bufferedImage != null) {
                Mat frame = ImageUtils.bufferedImageToMat(bufferedImage);
                detectAndDrawRects(frame);
                Image imageToShow = ImageUtils.mat2Image(frame);
                updateImageView(imageToShow);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DetailedImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void detectAndDrawRects(Mat frame) {
        System.out.println("detectAndDrawRects @ " + getClass().toString());
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
        // scan for faces 
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        // extract face rectangles
        Rect[] facesArray = faces.toArray();
        // draw rectangles
        for (Rect facesArray1 : facesArray) {
            Imgproc.rectangle(frame, facesArray1.tl(), facesArray1.br(), new Scalar(0, 255, 0), 3);
        }
    }

    private void updateImageView(Image imageToShow) {
        System.out.println("updateImageView @ " + getClass().toString());
        ImageUtils.onFXThread(originalFrame.imageProperty(), imageToShow);
    }

    // -------------------------------------------------------------------------
    // ----------------------------------                                UI code
    // -------------------------------------------------------------------------
    @FXML
    private void goBack() throws IOException {
        System.out.println("goBack @ " + getClass().toString());
        if (holder.getReturnHolder().isPresent()) {
            OpenCVCats.getMainStage().setUserData(holder.getReturnHolder().get());
        }
        ViewUtils.loadView(holder.getReturnResource());
    }

    @FXML
    protected void haarSelected(Event event) {
        System.out.println("haarSelected @ " + getClass().toString());
        if (rbLbp.isSelected()) {
            rbLbp.setSelected(false);
        }
        radioSelection("resources/haarcascades/haarcascade_frontalcatface.xml");
    }

    @FXML
    protected void lbpSelected(Event event) {
        System.out.println("lbpSelected @ " + getClass().toString());
        if (rbHaar.isSelected()) {
            rbHaar.setSelected(false);
        }
        radioSelection("resources/lbpcascades/lbpcascade_frontalcatface.xml");
    }

    private void radioSelection(String classifierPath) {
        System.out.println("checkboxSelection @ " + getClass().toString());
        faceCascade.load(classifierPath);
    }
}
