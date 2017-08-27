package com.sjcqs.rawlauncher.items.apps;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import com.sjcqs.rawlauncher.items.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by satyan on 8/24/17.
 */

public class AppsLoader extends AsyncTaskLoader<List<App>> {
    public static final int GET_NO_TAG = 0;
    private List<App> apps;
    private final PackageManager packageManager;

    public AppsLoader(Context context) {
        super(context);
        packageManager = context.getPackageManager();
    }


    @Override
    public List<App> loadInBackground() {
        final Context context = getContext();

        List<ApplicationInfo> infos = packageManager.getInstalledApplications(GET_NO_TAG);

        if (infos == null){
            infos = new ArrayList<>();
        }

        List<App> items = new ArrayList<>(infos.size());
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

    @Override
    public void deliverResult(List<App> data) {
        if (isReset()){
            if (data != null){
                cleanUp(data);
            }
        }

        this.apps = data;

        if (isStarted()){
            super.deliverResult(data);
        }

        if (data != null){
            cleanUp(data);
        }

    }

    @Override
    protected void onStartLoading() {
        if (apps != null){
            deliverResult(apps);
        }

        if (takeContentChanged() || apps == null){
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (apps != null){
            cleanUp(apps);
            apps = null;
        }
    }

    @Override
    public void onCanceled(List<App> data) {
        super.onCanceled(data);
        cleanUp(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    private void cleanUp(List<App> apps){
        // clean up used resources
    }
}
