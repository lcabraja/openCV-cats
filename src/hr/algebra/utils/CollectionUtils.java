/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.util.Set;

/**
 *
 * @author lcabraja
 */
public class CollectionUtils {

    private CollectionUtils() {
    }
    
    public static <T> T getValueFromSet(Set<T> set, T value) {
        return set.stream().filter(value::equals).findAny().orElse(null);
    }
}
