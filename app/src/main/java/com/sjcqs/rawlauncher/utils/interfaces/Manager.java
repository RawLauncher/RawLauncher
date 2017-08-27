package com.sjcqs.rawlauncher.utils.interfaces;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.suggestions.Suggestion;

import java.util.Collection;
import java.util.List;

/**
 * Created by satyan on 8/24/17.
 */

public abstract class Manager implements LoaderManager.LoaderCallbacks<List<Item>>{

    protected final AppCompatActivity activity;
    protected List<Item> items;

    protected Manager(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
        items = data;
    }

    @Override
    public void onLoaderReset(Loader<List<Item>> loader) {
        items = null;
    }

    public abstract boolean isLoaded();

    public abstract Intent getIntent(String str);

    public abstract Collection<? extends Suggestion> getSuggestions(String input);
}
