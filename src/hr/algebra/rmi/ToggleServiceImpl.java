/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.rmi;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.Scene;

/**
 *
 * @author lcabraja
 */
public class ToggleServiceImpl implements ToggleService {

    private List<Scene> scenes = new LinkedList<>();
    private List<String> themes = new LinkedList<>();

    public void subscribe(Scene scene) {
        scenes.add(scene);
    }

    public void unsubscribe(Scene scene) {
        scenes.remove(scene);
    }
}
