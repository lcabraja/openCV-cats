/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.opencv;

/**
 *
 * @author lcabraja
 */
public enum CascadeClassifierEnum {
    HAARCASCADE("resources/haarcascades/haarcascade_frontalcatface.xml"),
    LBPCASCADE("resources/lbpcascades/lbpcascade_frontalcatface.xml");

    private final String cascadePath;

    CascadeClassifierEnum(String cascadePath) {
        this.cascadePath = cascadePath;
    }

    @Override
    public String toString() {
        return cascadePath;
    }

}
