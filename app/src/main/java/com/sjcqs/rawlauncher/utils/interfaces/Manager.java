package com.sjcqs.rawlauncher.utils.interfaces;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.suggestions.Suggestion;
import com.sjcqs.rawlauncher.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by satyan on 8/24/17.
 * Manage apps, settings, shortcuts, etc.
 */

public abstract class Manager implements LoaderManager.LoaderCallbacks<List<Item>>{
    private static final String TAG = Manager.class.getName();

    protected final Context context;
    protected final LoaderManager loaderManager;
    protected List<Item> items;

    protected Manager(Context context, LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
        items = data;
    }

    @Override
    public void onLoaderReset(Loader<List<Item>> loader) {
        items = null;
    }

    public boolean isLoaded(){
        return items != null;
    }

    public Intent getIntent(String str) {
        str = StringUtil.normalize(str);
        for (Item item : items) {
            if (str.equalsIgnoreCase(item.getLabel())){
                return item.getIntent();
            }
        }
        return null;
    }


    public Collection<Suggestion> getSuggestions(String input){
        List<Suggestion> suggestions = new ArrayList<>();
        if (items != null) {
            for (Item app : items) {
                String str2 = app.getLabel();
                double rate = StringUtil.canBeSuggested(input, str2);

                if (rate < StringUtil.MAX_RATE) {
                    suggestions.add(new Suggestion(app, rate));
                }
            }
        }
        return suggestions;
    }

    public void reload() {

    }

    public List<Item> getItems() {
        return items;
    }

    public List<Item> getItems(int fromIndex, int toIndex) {
        return items.subList(fromIndex, toIndex);
    }

    public Item getItem(int index) {
        return items.get(index);
    }
}
