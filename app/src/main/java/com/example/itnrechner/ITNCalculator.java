package com.example.itnrechner;

public class ITNCalculator {

    public static float calcITNDif(float you, float opponent, boolean won) {
        float x;
        if (won) {
            x = opponent - you;
        } else {
            x = you - opponent;
        }
        return (float)(0.2501 / (1 + 1.9251 * Math.pow(Math.E, 2.3716*x)));
    }

    public static float calcITNDifAfterSik(float itnDif, String sikYou, String sikOpponent) {
        if (sikYou.equals("Sicher") && sikOpponent.equals("Sicher") || sikYou.equals("Unsicher") && sikOpponent.equals("Unsicher")) {
            return itnDif;
        }
        if (sikYou.equals("Sicher") && sikOpponent.equals("Unsicher")) {
            return (float)(itnDif * 0.5);
        } else {
            return itnDif * 2;
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Float.parseFloat(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
