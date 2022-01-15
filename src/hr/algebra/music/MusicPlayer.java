/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author lcabraja
 */
public class MusicPlayer {

    private MusicPlayer() {
    }

    public static void playEndingSound() {
        try {
            Media media = new Media(MusicPlayer.class.getResource("quack.mp3").toURI().toString());
            MediaPlayer player = new MediaPlayer(media);
            player.play();
        } catch (Exception ex) {
            System.err.println("Could not play ending tune...");
        }
    }
}
