package com.sjcqs.rawlauncher.items.apps;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.suggestions.Suggestion;
import com.sjcqs.rawlauncher.items.suggestions.SuggestionList;
import com.sjcqs.rawlauncher.utils.StringUtil;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;
import com.sjcqs.rawlauncher.utils.interfaces.SuggestionUpdator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by satyan on 8/24/17.
 * Manage
 */

public class AppManager extends Manager implements SuggestionUpdator {
    private static final String TAG = AppManager.class.getName();

    public AppManager(AppCompatActivity activity) {
        super(activity);
        activity.getSupportLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        return new AppsLoader(activity);
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
        Log.d(TAG, "getSuggestions: "+input);
        List<Suggestion> suggestions = new ArrayList<>();
        for (Item app : items) {
            String str2 = app.getLabel();
            double rate = StringUtil.canBeSuggested(input,str2);
            if (rate < StringUtil.MAX_RATE ){
                suggestions.add(new Suggestion(app,rate));
            }
        }
        return suggestions;
    }

    @Override
    public SuggestionUpdate updateSuggestions(String input, List<Suggestion> current) {
        SuggestionList toAdd = new SuggestionList();
        SuggestionList toRemove = new SuggestionList();
//        if (isLoaded()) {
//            for (Suggestion item : current){
//                App app = (App) item.getItem();
//                String str2 = app.getLabel();
//                if (StringUtil.canBeSuggested(input,str2) > StringUtil.MAX_RATE && current.containsItem(app)){
//                    toRemove.add(item);
//                }
//
//            }
//            for (Item app : items) {
//                String str2 = app.getLabel();
//                double rate = StringUtil.canBeSuggested(input,str2);
//                if (rate < StringUtil.MAX_RATE && !current.containsItem(app)){
//                    toAdd.add(new Suggestion(app,rate));
//                }
//            }
//        }
        return new SuggestionUpdate(toRemove,toAdd);
    }

    @Override
    public boolean isLoaded() {
        return items != null;
    }
}
