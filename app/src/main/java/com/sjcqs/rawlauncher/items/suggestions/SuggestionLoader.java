package com.sjcqs.rawlauncher.items.suggestions;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.items.device_settings.DeviceSettingManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyan on 8/27/17.
 */

public class SuggestionLoader extends AsyncTaskLoader<List<Suggestion>> {
    private static final String TAG = SuggestionLoader.class.getName();
    private String input;
    private final AppManager appManager;
    private final DeviceSettingManager deviceSettingManager;
    private List<Suggestion> items = null;

    public SuggestionLoader(Context context, String input, AppManager appManager, DeviceSettingManager deviceSettingManager) {
        super(context);
        this.input = input;
        this.appManager = appManager;
        this.deviceSettingManager = deviceSettingManager;
    }

    @Override
    public List<Suggestion> loadInBackground() {
        items = new ArrayList<>();
        // DEVICE SETTINGS
        while (!deviceSettingManager.isLoaded()){
            try {
                wait(100);
            } catch (InterruptedException e){
                Log.e(TAG, "loadInBackground: ", e);
            }
        }
        items.addAll(deviceSettingManager.getSuggestions(input));
        // APPS
        while (!appManager.isLoaded()){
            try {
                wait(100);
            } catch (InterruptedException e) {
                Log.e(TAG, "loadInBackground: ", e);
            }
        }
        items.addAll(appManager.getSuggestions(input));

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

        if (takeContentChanged() || items == null){
            forceLoad();
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
