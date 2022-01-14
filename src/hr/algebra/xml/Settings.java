/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.xml;

import hr.algebra.opencv.CascadeClassifierEnum;

/**
 *
 * @author lcabraja
 */
public class Settings {

    private CascadeClassifierEnum defaultClassifier;
    private String defaultHost;
    private boolean autoStartRMI;
    private boolean autoStartTCP;
    private boolean autoStartLiveTCP;

    public Settings() {
        this.defaultClassifier = CascadeClassifierEnum.HAARCASCADE;
        this.defaultHost = "localhost";
        this.autoStartRMI = true;
        this.autoStartTCP = true;
        this.autoStartLiveTCP = true;
    }

    public Settings(CascadeClassifierEnum defaultClassifier, String defaultHost, boolean autoStartRMI, boolean autoStartTCP, boolean autoStartLiveTCP) {
        this.defaultClassifier = defaultClassifier;
        this.defaultHost = defaultHost;
        this.autoStartRMI = autoStartRMI;
        this.autoStartTCP = autoStartTCP;
        this.autoStartLiveTCP = autoStartLiveTCP;
    }

    public CascadeClassifierEnum getDefaultClassifier() {
        return defaultClassifier;
    }

    public void setDefaultClassifier(CascadeClassifierEnum defaultClassifier) {
        this.defaultClassifier = defaultClassifier;
    }

    public String getDefaultHost() {
        return defaultHost;
    }

    public void setDefaultHost(String defaultHost) {
        this.defaultHost = defaultHost;
    }

    public boolean isAutoStartRMI() {
        return autoStartRMI;
    }

    public void setAutoStartRMI(boolean autoStartRMI) {
        this.autoStartRMI = autoStartRMI;
    }

    public boolean isAutoStartTCP() {
        return autoStartTCP;
    }

    public void setAutoStartTCP(boolean autoStartTCP) {
        this.autoStartTCP = autoStartTCP;
    }

    public boolean isAutoStartLiveTCP() {
        return autoStartLiveTCP;
    }

    public void setAutoStartLiveTCP(boolean autoStartLiveTCP) {
        this.autoStartLiveTCP = autoStartLiveTCP;
    }

}
