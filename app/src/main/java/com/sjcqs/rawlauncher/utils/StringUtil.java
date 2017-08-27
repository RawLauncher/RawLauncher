package com.sjcqs.rawlauncher.utils;

import android.util.Log;

import java.text.Normalizer;

/**
 * Created by satyan on 8/24/17.
 */

public final class StringUtil {
    public final static double MAX_RATE = 0.5;
    private static final String TAG = StringUtil.class.getName();

    public static double canBeSuggested(String input, String name){
        input = normalize(input); name = normalize(name);
        double distance = levenshteinDistance(input,name);
        boolean nameMatch = false;
        String strings[] = name.split("\\s");
        for (String string : strings) {
            boolean wordMatch = false;
            for (int i = 1; i <= input.length(); i++) {
                String part1 = input.substring(0, i);
                if (string.length() >= part1.length()) {
                    String part2 = string.substring(0, i);
                    wordMatch = part1.equalsIgnoreCase(part2);
                }
            }
            nameMatch = wordMatch || nameMatch;
        }

        if (nameMatch || distance <= MAX_RATE){
            distance = nameMatch ? 0 : distance;
        }

        return distance;
    }

    private static double levenshteinDistance(String a, String b){
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
