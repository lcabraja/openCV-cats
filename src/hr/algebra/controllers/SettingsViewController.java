/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.OpenCVCats;
import hr.algebra.opencv.CascadeClassifierEnum;
import hr.algebra.utils.ViewUtils;
import hr.algebra.xml.Settings;
import hr.algebra.xml.SettingsHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author lcabraja
 */
public class SettingsViewController implements Initializable {

    private Settings localSettings;
    final ToggleGroup group = new ToggleGroup();

    @FXML
    private RadioButton rbHaar;
    @FXML
    private RadioButton rbLbp;
    @FXML
    private CheckBox cbRmi;
    @FXML
    private CheckBox cbTcp;
    @FXML
    private CheckBox cbLiveTcp;
    @FXML
    private TextField tbHost;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        localSettings = OpenCVCats.getSettings();
        setDefaultRadioButton();
        setServerCheckboxes();
        setHost();
    }

    private void setDefaultRadioButton() {
        rbHaar.setToggleGroup(group);
        rbLbp.setToggleGroup(group);

        switch (localSettings.getDefaultClassifier()) {
            case HAARCASCADE:
                rbHaar.selectedProperty().set(true);
                break;
            case LBPCASCADE:
                rbLbp.selectedProperty().set(true);
                break;
        }
    }

    private void setServerCheckboxes() {
        cbRmi.selectedProperty().set(localSettings.isAutoStartRMI());
        cbTcp.selectedProperty().set(localSettings.isAutoStartTCP());
        cbLiveTcp.selectedProperty().set(localSettings.isAutoStartLiveTCP());
    }

    private void setHost() {
        tbHost.setText(localSettings.getDefaultHost());
    }

    @FXML
    private void rbHaarChecked(ActionEvent event) {
        localSettings.setDefaultClassifier(CascadeClassifierEnum.HAARCASCADE);
        SettingsHandler.updateSettings(localSettings);
    }

    @FXML
    private void rbLbpChecked(ActionEvent event) {
        localSettings.setDefaultClassifier(CascadeClassifierEnum.LBPCASCADE);
        SettingsHandler.updateSettings(localSettings);
    }

    @FXML
    private void cbAutostart(ActionEvent event) {
        localSettings.setAutoStartRMI(cbRmi.selectedProperty().get());
        localSettings.setAutoStartTCP(cbTcp.selectedProperty().get());
        localSettings.setAutoStartLiveTCP(cbLiveTcp.selectedProperty().get());
        SettingsHandler.updateSettings(localSettings);
    }

    @FXML
    private void btnSaveHost(ActionEvent event) {
        localSettings.setDefaultHost(tbHost.getText().trim());
        SettingsHandler.updateSettings(localSettings);
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        System.out.println("goBack @ " + getClass().toString());
        ViewUtils.loadView(getClass().getResource("views/MainMenu.fxml"));
    }
}
