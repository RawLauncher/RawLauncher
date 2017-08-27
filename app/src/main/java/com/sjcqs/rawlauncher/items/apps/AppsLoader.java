package com.sjcqs.rawlauncher.items.apps;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.ItemLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by satyan on 8/24/17.
 */

public class AppsLoader extends ItemLoader<List<Item>> {
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

            if (packageManager.getLaunchIntentForPackage(pkg) != null){
                App app = new App(context,info);
                app.loadLabel();
                items.add(app);
            }
        }

        Collections.sort(items, Item.ALPHA_COMPARATOR);
        return items;
    }

    private void cleanUp(List<App> apps){
        // clean up used resources
    }
}
