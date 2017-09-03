package com.sjcqs.rawlauncher.items.device_settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v4.content.res.ResourcesCompat;

import com.sjcqs.rawlauncher.R;
import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.ItemLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyan on 8/27/17.
 */

class DeviceSettingLoader extends ItemLoader {
    private final static String[][] SETTINGS_ACTION = {
            {Settings.ACTION_ACCESSIBILITY_SETTINGS,"Accessibility"},
            {Settings.ACTION_ADD_ACCOUNT,"Add Account"},
            {Settings.ACTION_AIRPLANE_MODE_SETTINGS,"Airplane Mode"},
            {Settings.ACTION_APN_SETTINGS,"APN"},
            {Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,"Application Development"},
            {Settings.ACTION_BATTERY_SAVER_SETTINGS,"Battery Saver"},
            {Settings.ACTION_BLUETOOTH_SETTINGS,"Bluetooth"},
            {Settings.ACTION_CAPTIONING_SETTINGS,"Captioning"},
            {Settings.ACTION_CAST_SETTINGS,"Cast"},
            {Settings.ACTION_DATA_ROAMING_SETTINGS,"Data Roaming"},
            {Settings.ACTION_DATE_SETTINGS,"Date"},
            {Settings.ACTION_DEVICE_INFO_SETTINGS,"Device Info"},
            {Settings.ACTION_DISPLAY_SETTINGS,"Display"},
            {Settings.ACTION_HOME_SETTINGS,"Home"},
            {Settings.ACTION_INPUT_METHOD_SETTINGS,"Keyboard"},
            {Settings.ACTION_INTERNAL_STORAGE_SETTINGS,"Internal Storage"},
            {Settings.ACTION_LOCALE_SETTINGS,"Language"},
            {Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS,"Manage Applications"},
            {Settings.ACTION_NFC_SETTINGS,"NFC"},
            {Settings.ACTION_PRINT_SETTINGS,"Print"},
            {Settings.ACTION_PRIVACY_SETTINGS,"Privacy"},
            {Settings.ACTION_QUICK_LAUNCH_SETTINGS,"Quick Launch"},
            {Settings.ACTION_SEARCH_SETTINGS,"InputSearch"},
            {Settings.ACTION_SECURITY_SETTINGS,"Security"},
            {Settings.ACTION_SOUND_SETTINGS,"Sound"},
            {Settings.ACTION_SYNC_SETTINGS,"Sync"},
            {Settings.ACTION_VOICE_INPUT_SETTINGS,"Voice Input"},
            {Settings.ACTION_WIFI_SETTINGS,"WiFi"},
            {Settings.ACTION_WIRELESS_SETTINGS,"Wireless"}
    };
    private static final String TAG = DeviceSetting.class.getName();

    public DeviceSettingLoader(Context context) {
        super(context);
    }

    @Override
    public List<Item> loadInBackground() {
        items = new ArrayList<>();
        Drawable icon = context.getDrawable(R.drawable.ic_settings_black_24dp);
        if (icon != null){
            icon.setTint(ResourcesCompat.getColor(context.getResources(),R.color.color_secondary,null));
        }

        for (String[] action : SETTINGS_ACTION) {
            Intent intent = new Intent(action[0]);
            DeviceSetting setting = new DeviceSetting(action[0],action[1],icon, intent);
            items.add(setting);
        }
        return items;
    }
}
