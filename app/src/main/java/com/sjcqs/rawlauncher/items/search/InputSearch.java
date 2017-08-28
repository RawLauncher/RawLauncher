package com.sjcqs.rawlauncher.items.search;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.sjcqs.rawlauncher.items.Item;

/**
 * Created by satyan on 8/28/17.
 */

public class InputSearch extends Item {
    private static final String TAG = InputSearch.class.getName();
    private final String link;
    private String input;
    private String name;

    public InputSearch(String label, Drawable icon, String link) {
        super(label, icon, null);
        this.name = label;
        this.link = link;
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
}
