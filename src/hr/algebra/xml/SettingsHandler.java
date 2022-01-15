/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.xml;

import hr.algebra.OpenCVCats;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author lcabraja
 */
public class SettingsHandler {

    private SettingsHandler() {
    }

    public static Settings getLastSettings() {
        Settings savedSettings = deserializeFromXML();
        return savedSettings == null ? new Settings() : savedSettings;
    }

    public static void updateSettings(Settings newSettings) {
        OpenCVCats.updateSettings(newSettings);
        serializeToXML(newSettings);
    }

    private static void serializeToXML(Settings settings) {
        try {
            createSettingsFile();
            try (FileOutputStream fos = new FileOutputStream("settings.xml"); XMLEncoder encoder = new XMLEncoder(fos)) {
                encoder.writeObject(settings);
            }
        } catch (IOException ex) {
            System.err.println("Cannot access settings file...");
            createSettingsFile();
        }
    }

    private static void createSettingsFile() {
        try {
            File settingsFile = new File("settings.xml");
            settingsFile.createNewFile();
        } catch (IOException ex) {
            System.err.println("Error while creating settings file");
        }
    }

    private static Settings deserializeFromXML() {
        Settings decodedSettings = null;
        try (FileInputStream fis = new FileInputStream("settings.xml"); XMLDecoder decoder = new XMLDecoder(fis)) {
            decodedSettings = (Settings) decoder.readObject();
        } catch (IOException ex) {
            System.err.println("Cannot access settings file...");
            createSettingsFile();
        }
        return decodedSettings;
    }
}
