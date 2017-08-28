package com.sjcqs.rawlauncher.items.device_settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.suggestions.Suggestion;
import com.sjcqs.rawlauncher.utils.LoaderUtils;
import com.sjcqs.rawlauncher.utils.StringUtil;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by satyan on 8/27/17.
 * Manage device settings shortcuts
 */

public class DeviceSettingManager extends Manager<DeviceSetting>{

    public static final double PENALTY = 0.3;

    public DeviceSettingManager(Context context, LoaderManager loaderManager) {
        super(context, loaderManager);
        loaderManager.initLoader(LoaderUtils.DEVICE_SETTINGS_LOADER,null,this);
    }

    @Override
    public Loader<List<DeviceSetting>> onCreateLoader(int id, Bundle args) {
        return new DeviceSettingLoader(context);
    }

    @Override
    public Collection<? extends Suggestion> getSuggestions(String input) {
        List<Suggestion> suggestions = new ArrayList<>();
        for (Item app : items) {
            String str2 = app.getLabel();
            double rate = StringUtil.canBeSuggested(input, str2);
            if (rate < StringUtil.MAX_RATE) {
                suggestions.add(new Suggestion(app, rate + PENALTY));
            }
        }

        return suggestions;
    }
}
