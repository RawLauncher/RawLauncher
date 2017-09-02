package com.sjcqs.rawlauncher.items.search;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.sjcqs.rawlauncher.items.Item;

/**
 * Created by satyan on 8/28/17.
 */

class InputSearch extends Item {
    private static final String TAG = InputSearch.class.getName();
    private final String link;
    private String input;
    private String name;
    private double priority;

    public InputSearch(String label, Drawable icon, String link, double priority) {
        super(label, icon, null);
        this.name = label;
        this.link = link;
        this.priority = priority;
    }

    @Override
    public Intent getIntent() {
        Uri uri = Uri.parse(link+input);
        return new Intent(Intent.ACTION_VIEW,uri);
    }

    public void setInput(String input) {
        label = name + ": " + input;
        this.input = input.replaceAll("\\s","+");
    }

    public double getPriority() {
        return priority;
    }
}
