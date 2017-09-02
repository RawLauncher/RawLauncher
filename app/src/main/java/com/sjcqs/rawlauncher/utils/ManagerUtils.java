package com.sjcqs.rawlauncher.utils;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v4.util.ArrayMap;

import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.items.device_settings.DeviceSettingManager;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;

import java.util.Map;

/**
 * Created by satyan on 8/27/17.
 */

public final class ManagerUtils {
    public final static int ID_SUGGESTION_LOADER = 0;
    public final static int ID_APP_LOADER = 1;
    public final static int ID_DEVICE_SETTINGS_LOADER = 2;
    public final static int ID_SEARCH_LOADER = 3;

    public final static int MANAGER_APPS = 0;
    public final static int MANAGER_DEVICE_SETTINGS = 1;

    public static Map<Integer, Manager> loadManagers(Context context, LoaderManager loaderManager) {
        Map<Integer, Manager> managers = new ArrayMap<>();
        //APPS
        AppManager appManager = new AppManager(context,loaderManager);
        managers.put(MANAGER_APPS, appManager);
        //DEVICE SETTINGS
        DeviceSettingManager deviceSettingManager = new DeviceSettingManager(context,loaderManager);
        managers.put(MANAGER_DEVICE_SETTINGS, deviceSettingManager);
        return managers;
    }

}
