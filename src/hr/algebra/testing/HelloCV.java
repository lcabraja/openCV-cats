/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.testing;

import hr.algebra.utils.ColorUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 *
 * @author lcabraja
 */
public class HelloCV {

    public static void main(String[] args) {
//        importOpenCVTest();
        testColors();
    }

    private static void importOpenCVTest() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = \n" + mat.dump());
    }

    private static void testColors() {
        Scalar HexToScalar = ColorUtils.getDeterministicColorScalar();
        System.out.println("hex" + HexToScalar.toString());
        System.out.println("scalar: " + ColorUtils.scalarToHex(HexToScalar));
    }
}
