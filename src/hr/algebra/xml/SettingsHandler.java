/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.xml;

import hr.algebra.OpenCVCats;

/**
 *
 * @author lcabraja
 */
public class SettingsHandler {
    
    private SettingsHandler() {
    }

    public static Settings getLastSettings() {
        // get from file if exists
        return new Settings();
    }

    public static void updateSettings(Settings newSettings) {
        // save to file
        OpenCVCats.updateSettings(newSettings);
    }
}
