package com.sjcqs.rawlauncher.utils;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.items.device_settings.DeviceSettingManager;
import com.sjcqs.rawlauncher.items.search.InputSearchManager;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyan on 8/27/17.
 */

public final class LoaderUtils {
    public final static int SUGGESTION_LOADER = 0;
    public final static int APP_LOADER = 1;
    public final static int DEVICE_SETTINGS_LOADER = 2;
    public final static int SEARCH_LOADER = 3;

    public static List<Manager> loadManagers(Context context, LoaderManager loaderManager) {
        List<Manager> managers = new ArrayList<>();
        //APPS
        AppManager appManager = new AppManager(context,loaderManager);
        managers.add(appManager);
        //DEVICE SETTINGS
        DeviceSettingManager deviceSettingManager = new DeviceSettingManager(context,loaderManager);
        managers.add(deviceSettingManager);
        return managers;
    }

}
