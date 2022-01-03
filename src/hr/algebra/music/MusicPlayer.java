/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.music;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author lcabraja
 */
public class MusicPlayer {

    private MusicPlayer() {
    }

    public static void playEndingSound() throws URISyntaxException {
        Media media = new Media(MusicPlayer.class.getResource("quack.mp3").toURI().toString());
        System.out.println(media.getSource());
        MediaPlayer player = new MediaPlayer(media);
        player.play();
    }
}
