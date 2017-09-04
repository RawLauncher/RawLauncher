package com.sjcqs.rawlauncher.items.search;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.suggestions.Suggestion;
import com.sjcqs.rawlauncher.utils.ManagerUtils;
import com.sjcqs.rawlauncher.utils.StringUtil;
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
        loaderManager.restartLoader(ManagerUtils.ID_SEARCH_LOADER, null, this);
    }

    @Override
    public Collection<Suggestion> getSuggestions(String input) {
        List<Suggestion> suggestions = new ArrayList<>();
        for (Item item : items) {
            InputSearch search = (InputSearch) item;
            double rate = StringUtil.canBeSuggested(search.getDiscriminator(), input);
            if (rate <= StringUtil.MAX_RATE) {
                if (rate != 0) {
                    rate += InputSearchLoader.BASE_PRIORITY;
                }
            } else {
                rate = search.getPriority();
            }
            InputSearch inputSearch = (InputSearch) item;
            inputSearch.setInput(input);
            Suggestion suggestion = new Suggestion(inputSearch, rate);
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
        loaderManager.restartLoader(ManagerUtils.ID_SEARCH_LOADER, null, this);
    }
}
