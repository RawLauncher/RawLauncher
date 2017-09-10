package com.sjcqs.rawlauncher.utils;

import java.text.Normalizer;

/**
 * Created by satyan on 8/24/17.
 */

public final class StringUtil {
    public final static double MAX_RATE = 0.3;
    private static final String TAG = StringUtil.class.getName();
    private static final double LONGER_PENALTY = .05d;

    public static double canBeSuggested(String input, String name){
        if (input.length() == 0) {
            return Double.MAX_VALUE;
        }
        if (input.length() == 1) {
            return input.equalsIgnoreCase(name) ? 1 : 0;
        }

        input = normalize(input); name = normalize(name);
        double distance = levenshteinDistance(input,name);
        double matchDistance = Double.MAX_VALUE;
        String nameSubStrings[] = name.split("\\s");
        for (String string : nameSubStrings) {
            if (string.length() >= input.length()) {
                String part2 = string.substring(0, input.length());
                if (input.equalsIgnoreCase(part2)) {
                    return input.length() > name.length() ? LONGER_PENALTY : 0;
                } else if (input.length() > 3) {
                    double value = levenshteinDistance(input, part2);
                    if (string.length() > input.length()) {
                        value += LONGER_PENALTY;
                    }
                    matchDistance = Math.min(matchDistance, value);
                }
            }
        }

        distance = Math.min(matchDistance,distance);

        return distance;
    }

    private static double levenshteinDistance(String a, String b){
        a = a.replaceAll("\\s","");
        b = b.replaceAll("\\s","");

        int d[][] = new int[a.length()][b.length()];

        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                d[i][j] = 0;
            }
        }

        for (int i = 1; i < a.length(); i++) {
            d[i][0] = i;
        }
        for (int i = 1; i < b.length(); i++) {
            d[0][i] = i;
        }

        for (int j = 1; j < b.length(); j++) {
            for (int i = 1; i < a.length(); i++) {
                int substitutionCost = 1;
                if (a.charAt(i) == b.charAt(j)){
                    substitutionCost = 0;
                }
                d[i][j] = Math.min(
                        Math.min(
                                d[i-1][j]+1,
                                d[i][j-1]+1),
                        d[i-1][j-1] + substitutionCost);
            }
        }
        return ((double) d[a.length()-1][b.length()-1]) / ((double)Math.max(a.length(),b.length()));
    }

    public static String normalize(String str){
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }
}
