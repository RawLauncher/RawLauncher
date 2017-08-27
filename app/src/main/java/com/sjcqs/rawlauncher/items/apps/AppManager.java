package com.sjcqs.rawlauncher.items.apps;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sjcqs.rawlauncher.items.suggestions.Suggestion;
import com.sjcqs.rawlauncher.items.suggestions.SuggestionList;
import com.sjcqs.rawlauncher.utils.StringUtil;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;
import com.sjcqs.rawlauncher.utils.interfaces.SuggestionUpdator;

import java.util.List;


/**
 * Created by satyan on 8/24/17.
 * Manage
 */

public class AppManager extends Manager implements LoaderManager.LoaderCallbacks<List<App>>,SuggestionUpdator {
    private static final String TAG = AppManager.class.getName();
    private List<App> apps;

    public AppManager(AppCompatActivity activity) {
        super(activity);
        activity.getSupportLoaderManager().initLoader(0,null,this);
    }

    @Override
    public boolean isLoaded() {
        return apps != null;
    }

    @Override
    public Loader<List<App>> onCreateLoader(int id, Bundle args) {
        return new AppsLoader(activity);
    }

    @Override
    public void onLoadFinished(Loader<List<App>> loader, List<App> data) {
        apps = data;
    }

    @Override
    public void onLoaderReset(Loader<List<App>> loader) {
        apps = null;
    }

    @Override
    public Intent getIntent(String str) {
        str = StringUtil.normalize(str);
        for (App app : apps) {
            if (str.equalsIgnoreCase(app.getLabel())){
                return app.getIntent();
            }
        }
        return null;
    }


    @Override
    public SuggestionUpdate updateSuggestions(String input, SuggestionList current) {
        SuggestionList toAdd = new SuggestionList();
        SuggestionList toRemove = new SuggestionList();
        if (isLoaded()) {
            for (Suggestion item : current){
                App app = (App) item.getItem();
                String str2 = app.getLabel();
                if (StringUtil.canBeSuggested(input,str2) > StringUtil.MAX_RATE && current.containsItem(app)){
                    toRemove.add(item);
                }

            }
            for (App app : apps) {
                String str2 = app.getLabel();
                double rate = StringUtil.canBeSuggested(input,str2);
                if (rate < StringUtil.MAX_RATE && !current.containsItem(app)){
                    toAdd.add(new Suggestion(app,rate));
                }
            }
        }
        return new SuggestionUpdate(toRemove,toAdd);
    }
}
