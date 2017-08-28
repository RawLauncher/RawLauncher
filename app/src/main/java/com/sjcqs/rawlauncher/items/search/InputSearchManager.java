package com.sjcqs.rawlauncher.items.search;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.suggestions.Suggestion;
import com.sjcqs.rawlauncher.utils.LoaderUtils;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by satyan on 8/28/17.
 */

public class InputSearchManager extends Manager {

    private static final String TAG = InputSearchManager.class.getName();

    public InputSearchManager(Context context, LoaderManager loaderManager) {
        super(context, loaderManager);
        loaderManager.restartLoader(LoaderUtils.SEARCH_LOADER, null, this);
    }

    @Override
    public Collection<Suggestion> getSuggestions(String input) {
        Log.d(TAG, "getSuggestions: ");
        List<Suggestion> suggestions = new ArrayList<>();
        for (Item item : items) {
            InputSearch inputSearch = (InputSearch) item;
            inputSearch.setInput(input);
            Suggestion suggestion = new Suggestion(inputSearch,1d);
            suggestions.add(suggestion);
        }
        return suggestions;
    }

    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        return new InputSearchLoader(context);
    }

    @Override
    public void reload() {
        loaderManager.restartLoader(LoaderUtils.SEARCH_LOADER, null, this);
    }
}
