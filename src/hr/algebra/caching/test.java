/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.caching;

import java.util.HashMap;

/**
 *
 * @author lcabraja
 */
public class test {

    public static void main(String[] args) {
        HashMap<String, String> ss = new HashMap<>();
        ss.put("test", "11");
        ss.put("test", "22");
        System.out.println(ss.get("test"));

    }

}
