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

    InputSearch(String label, Drawable icon, Intent intent, String link, double priority) {
        super(label, icon, intent);
        this.name = label;
        this.link = link;
        this.priority = priority;
    }

    InputSearch(String label, Drawable icon, String link, double priority) {
        this(label, icon, new Intent(Intent.ACTION_VIEW), link, priority);
    }

    @Override
    public Intent getIntent() {
        Uri uri = Uri.parse(link+input);
        return intent.setData(uri);
    }

    public void setInput(String input) {
        input = input.replace(name + ":", "");
        label = name + ": " + input;
        this.input = Uri.encode(input);
    }

    public double getPriority() {
        return priority;
    }

    @Override
    public String getDiscriminator() {
        return name;
    }
}
