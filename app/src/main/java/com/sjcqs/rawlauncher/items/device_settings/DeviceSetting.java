package com.sjcqs.rawlauncher.items.device_settings;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.sjcqs.rawlauncher.items.Item;

/**
 * Created by satyan on 8/27/17.
 */

public class DeviceSetting extends Item {

    private final String action;

    public DeviceSetting(String action, String label, Drawable drawable, Intent intent) {
        super(label,drawable,intent);
        this.action = action;
    }

    @Override
    public String getInput() {
        return super.getInput();
    }

    @Override
    public String toString() {
        return super.toString() + "("+ action + ")";
    }
}
