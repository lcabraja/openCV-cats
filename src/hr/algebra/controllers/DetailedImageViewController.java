/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.caching.Cache;
import hr.algebra.model.DetailedImageViewHolder;
import hr.algebra.utils.ColorUtils;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.ImageUtils;
import hr.algebra.utils.ViewUtils;
import hr.algebra.opencv.CascadeClassifierEnum;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
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

    private static final String TEMP_PREFIX = "hr.algebra.controllers";

    // -------------------------------------------------------------------------
    // ----------------------------------                                 fields
    // -------------------------------------------------------------------------
    @FXML
    private ImageView originalFrame;
    @FXML
    private Label lbRectangles;
    @FXML
    private RadioButton rbHaar;
    @FXML
    private RadioButton rbLbp;

    final ToggleGroup group = new ToggleGroup();

    private final Cache cache = OpenCVCats.cache;

    private int absoluteFaceSize = 0;
    private DetailedImageViewHolder holder;
    private CascadeClassifier faceCascade;
    private String faceCascadePath;
    private BufferedImage bufferedImage;
    private File setImage;
    private Image imageToShow;
    private Mat frame;

    Rect[] facesArray;
    private String lastRectColor = null;
    @FXML
    private ListView<String> lvRectangles;

    // -------------------------------------------------------------------------
    // ----------------------------------                                  inits
    // -------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize @ " + getClass().toString());
        initFields();
        //test();
    }

    private void initFields() {
        holder = ((DetailedImageViewHolder) OpenCVCats.getMainStage().getUserData());
        rbHaar.setToggleGroup(group);
        rbLbp.setToggleGroup(group);
        rbHaar.setSelected(true);
        faceCascade = new CascadeClassifier();
        radioSelection(CascadeClassifierEnum.HAARCASCADE.toString());
    }

    // -------------------------------------------------------------------------
    // ----------------------------------                              rendering
    // -------------------------------------------------------------------------
    // https://stackoverflow.com/questions/19548363/image-saved-in-javafx-as-jpg-is-pink-toned
    private void fixJpegAlphaChannel() {
        try {
            bufferedImage = SwingFXUtils.fromFXImage(holder.getImage().getImage(), null);
            BufferedImage imageRGB = new BufferedImage(
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                    BufferedImage.OPAQUE);
            Graphics2D graphics = imageRGB.createGraphics();
            graphics.drawImage(bufferedImage, 0, 0, null);
            File tempFile = File.createTempFile(TEMP_PREFIX, "." + FileUtils.Extensions.JPG.toString());
            ImageIO.write(imageRGB, "jpg", tempFile);
            graphics.dispose();
            bufferedImage = SwingFXUtils.fromFXImage(new Image(new FileInputStream(tempFile)), null);
        } catch (IOException ex) {
            Logger.getLogger(DetailedImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void analyzeImage() {
        setImage = holder.getImageFile();
        fixJpegAlphaChannel();
        if (bufferedImage != null) {
            frame = ImageUtils.bufferedImageToMat(bufferedImage);
            if (cache.contains(setImage, faceCascadePath)) {
                Optional<Rect[]> potentialFaces = cache.getFaceRects(setImage, faceCascadePath);
                if (potentialFaces.isPresent()) {
                    facesArray = potentialFaces.get();
                }
            } else {
                detectRects(frame);
                cache.setFaceRects(setImage, facesArray, faceCascadePath);
            }
            updateRectangleList();
            drawRectangles(frame);
            updateImageView(ImageUtils.mat2Image(frame));
            displayStatistics();
        }
    }

    private void detectRects(Mat frame) {
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
        facesArray = faces.toArray();
    }

    private void drawRectangles(Mat frame) {
        // draw rectangles
        Scalar color;
        lastRectColor = ColorUtils.getDeterministicColorHex();
        for (Rect face : facesArray) {
            color = ColorUtils.hexToScalar(lastRectColor);
            lastRectColor = ColorUtils.getDeterministicColorHex(lastRectColor);
            Imgproc.rectangle(frame, face.tl(), face.br(), color, 3);
        }
    }

    private void updateRectangleList() {
        List<String> faceList = new ArrayList<>();
        Rect temp;
        for (int i = 0; i < facesArray.length; i++) {
            temp = facesArray[i];
            faceList.add(i + ". " + temp.width + "x" + temp.height);
        }
        ObservableList<String> oblist = FXCollections.observableList(faceList);
        lvRectangles.setItems(oblist);
    }

    private void displayStatistics() {
        switch (facesArray.length) {
            case 0:
                lbRectangles.setText("Detected no cats");
                break;
            case 1:
                lbRectangles.setText("Detected 1 cat");
                break;
            default:
                lbRectangles.setText(String.format("Detected %d cats", facesArray.length));
                break;
        }
    }

    private void displayRectangles(boolean colored) {
        Text text = new Text("");
        text.setFill(Color.web(ColorUtils.getDeterministicColorHex(lastRectColor)));
    }

    private void updateImageView(Image imageToShow) {
        System.out.println("updateImageView @ " + getClass().toString());
        ImageUtils.onFXThread(originalFrame.imageProperty(), imageToShow);
    }

    // -------------------------------------------------------------------------
    // ----------------------------------                                UI code
    // -------------------------------------------------------------------------
    @FXML
    private void lvRectanglesClicked(MouseEvent event) {

    }

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
        radioSelection(CascadeClassifierEnum.HAARCASCADE.toString());
    }

    @FXML
    protected void lbpSelected(Event event) {
        System.out.println("lbpSelected @ " + getClass().toString());
        if (rbHaar.isSelected()) {
            rbHaar.setSelected(false);
        }
        radioSelection(CascadeClassifierEnum.LBPCASCADE.toString());
    }

    private void radioSelection(String classifierPath) {
        System.out.println("checkboxSelection @ " + getClass().toString());
        faceCascadePath = classifierPath;
        faceCascade.load(classifierPath);
        analyzeImage();
    }
}
