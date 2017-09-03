package com.sjcqs.rawlauncher.items.suggestions;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;

import com.sjcqs.rawlauncher.items.search.InputSearchManager;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by satyan on 8/27/17.
 * Load suggestions using user input and managers
 */
class SuggestionLoader extends AsyncTaskLoader<List<Suggestion>> {
    private static final String TAG = SuggestionLoader.class.getName();
    private final Collection<Manager> managers;
    private final InputSearchManager searchManager;
    private String input;
    private List<Suggestion> items = null;


    public SuggestionLoader(Context context, String input, Collection<Manager> managers, InputSearchManager searchManager) {
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
        return items;
    }

    @Override
    public void deliverResult(List<Suggestion> data) {
        if (isReset()) {
            if (data != null) {
                cleanUp(data);
            }
        }

        this.items = data;

        if (items != null) {
            items.addAll(searchManager.getSuggestions(input));
        }

        if (isStarted()) {
            super.deliverResult(items);
        }

        if (data != null) {
            cleanUp(data);
        }


    }

    @Override
    protected void onStartLoading() {
        if (items != null) {
            deliverResult(items);
        }

        if (takeContentChanged() || items == null) {
            final Handler handler = new Handler();
            Runnable loadTask = new Runnable() { // wait for manager to load data
                @Override
                public void run() {
                    if (!searchManager.isLoaded()) {
                        handler.postDelayed(this, 100);
                    }
                    for (Manager manager : managers) {
                        if (!manager.isLoaded()) {
                            handler.postDelayed(this, 100);
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
        if (items != null) {
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

    private void cleanUp(List<Suggestion> apps) {
        // clean up used resources
    }
}
