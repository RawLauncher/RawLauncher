package com.sjcqs.rawlauncher.items.apps;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;

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

    @Override
    public boolean canBeAShortcut() {
        return true;
    }

    @Override
    public boolean canBeUninstalled() {
        return true;
    }

    @Override
    public Intent getUninstallIntent() {
        return new Intent(Intent.ACTION_DELETE, Uri.fromParts("package",
                info.packageName, null));
    }
}
