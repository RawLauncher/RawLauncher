package com.sjcqs.rawlauncher.items.apps;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.sjcqs.rawlauncher.items.Item;

/**
 * Created by satyan on 8/24/17.
 */

public class App extends Item{
    private final ApplicationInfo info;

    private boolean mounted = false;

    App(ApplicationInfo info, String label, Drawable icon, Intent intent){
        super(label,icon,intent);
        this.info = info;

    }

    private String getPackageName(){
        return info.packageName;
    }
}
