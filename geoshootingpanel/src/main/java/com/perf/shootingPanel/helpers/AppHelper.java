/*
 *   GEODynamics
 *   Author - James Suderman
 *   Date - 06/23/2020
 */

package com.perf.shootingPanel.helpers;

public class AppHelper {

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
