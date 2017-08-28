package com.sjcqs.rawlauncher.items.suggestions;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.items.device_settings.DeviceSettingManager;
import com.sjcqs.rawlauncher.items.search.InputSearchManager;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by satyan on 8/27/17.
 */

public class SuggestionLoader extends AsyncTaskLoader<List<Suggestion>> {
    private static final String TAG = SuggestionLoader.class.getName();
    private final List<Manager> managers;
    private final InputSearchManager searchManager;
    private String input;
    private List<Suggestion> items = null;


    public SuggestionLoader(Context context, String input, List<Manager> managers, InputSearchManager searchManager) {
        super(context);
        this.input = input;
        this.managers = managers;
        this.searchManager = searchManager;
    }

    @Override
    public List<Suggestion> loadInBackground() {
        items = new ArrayList<>();
        for (Manager manager : managers) {
            items.addAll(manager.getSuggestions(input));
        }

        if (items.isEmpty()){
            items.addAll(searchManager.getSuggestions(input));
        }
        return items;
    }

    @Override
    public void deliverResult(List<Suggestion> data) {
        if (isReset()){
            if (data != null){
                cleanUp(data);
            }
        }

        this.items = data;

        if (isStarted()){
            super.deliverResult(data);
        }

        if (data != null){
            cleanUp(data);
        }

    }

    @Override
    protected void onStartLoading() {
        if (items != null){
            deliverResult(items);
        }

        if (takeContentChanged() || items == null) {
            final Handler handler = new Handler();
            Runnable loadTask = new Runnable() { // wait for manager to load data
                @Override
                public void run() {
                    if (!searchManager.isLoaded()){
                        handler.postDelayed(this,100);
                    }
                    for (Manager manager : managers) {
                        if (!manager.isLoaded()){
                            handler.postDelayed(this,100);
                        }
                    }
                    forceLoad();
                }
            };
            handler.post(loadTask);
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (items != null){
            cleanUp(items);
            items = null;
        }
    }

    @Override
    public void onCanceled(List<Suggestion> data) {
        super.onCanceled(data);
        cleanUp(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    private void cleanUp(List<Suggestion> apps){
        // clean up used resources
    }

}
