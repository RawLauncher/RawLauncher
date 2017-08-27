package com.sjcqs.rawlauncher.items.apps;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.suggestions.Suggestion;
import com.sjcqs.rawlauncher.utils.LoaderUtils;
import com.sjcqs.rawlauncher.utils.StringUtil;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;
import com.sjcqs.rawlauncher.utils.interfaces.Reloadable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by satyan on 8/24/17.
 * Manage
 */

public class AppManager extends Manager implements Reloadable {
    private static final String TAG = AppManager.class.getName();

    public AppManager(Context context, LoaderManager loaderManager) {
        super(context, loaderManager);
        loaderManager.initLoader(LoaderUtils.APP_LOADER,null,this);
    }

    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        return new AppsLoader(context);
    }

    @Override
    public Intent getIntent(String str) {
        str = StringUtil.normalize(str);
        for (Item app : items) {
            if (str.equalsIgnoreCase(app.getLabel())){
                return app.getIntent();
            }
        }
        return null;
    }

    @Override
    public Collection<? extends Suggestion> getSuggestions(String input) {
        List<Suggestion> suggestions = new ArrayList<>();
        if (isLoaded()) {
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

    @Override
    public void reload() {
        loaderManager.restartLoader(LoaderUtils.APP_LOADER,null,this);
    }

    @Override
    public boolean isLoaded() {
        return items != null;
    }
}
