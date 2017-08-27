package com.sjcqs.rawlauncher.items.device_settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.sjcqs.rawlauncher.utils.LoaderUtils;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;

import java.util.List;

/**
 * Created by satyan on 8/27/17.
 */

public class DeviceSettingManager extends Manager<DeviceSetting>{

    public DeviceSettingManager(Context context, LoaderManager loaderManager) {
        super(context, loaderManager);
        loaderManager.initLoader(LoaderUtils.DEVICE_SETTINGS_LOADER,null,this);
    }

    @Override
    public Loader<List<DeviceSetting>> onCreateLoader(int id, Bundle args) {
        return new DeviceSettingLoader(context);
    }
}
