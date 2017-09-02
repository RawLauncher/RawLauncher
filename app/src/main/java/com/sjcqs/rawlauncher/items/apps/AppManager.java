package com.sjcqs.rawlauncher.items.apps;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.utils.ManagerUtils;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;

import java.util.List;


/**
 * Created by satyan on 8/24/17.
 * Manage
 */

public class AppManager extends Manager {
    private static final String TAG = AppManager.class.getName();

    public AppManager(Context context, LoaderManager loaderManager) {
        super(context, loaderManager);
        loaderManager.initLoader(ManagerUtils.ID_APP_LOADER, null, this);
    }

    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        return new AppsLoader(context);
    }

    @Override
    public void reload() {
        loaderManager.restartLoader(ManagerUtils.ID_APP_LOADER, null, this);
    }
}
