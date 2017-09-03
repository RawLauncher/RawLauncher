package com.sjcqs.rawlauncher.items.apps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.ItemLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by satyan on 8/24/17.
 * Load application on the device
 */
class AppsLoader extends ItemLoader {
    public static final int GET_NO_TAG = 0;
    private final PackageManager packageManager;

    public AppsLoader(Context context) {
        super(context);
        packageManager = context.getPackageManager();
    }


    @Override
    public List<Item> loadInBackground() {
        final Context context = getContext();

        List<ApplicationInfo> infos = packageManager.getInstalledApplications(GET_NO_TAG);

        if (infos == null){
            infos = new ArrayList<>();
        }

        List<Item> items = new ArrayList<>(infos.size());
        for (ApplicationInfo info : infos) {
            String pkg = info.packageName;
            String label;
            Drawable icon;
            Intent intent;

            CharSequence sequence = info.loadLabel(context.getPackageManager());
            label = sequence  != null ? sequence.toString() : info.packageName;

            icon = info.loadIcon(context.getPackageManager());
            icon = icon != null ?
                    icon
                    : context.getResources().getDrawable(android.R.drawable.sym_def_app_icon,null);
            intent = packageManager.getLaunchIntentForPackage(info.packageName);

            if (packageManager.getLaunchIntentForPackage(pkg) != null){
                App app = new App(info,label,icon,intent);
                items.add(app);
            }
        }

        Collections.sort(items, Item.ALPHA_COMPARATOR);
        return items;
    }
}