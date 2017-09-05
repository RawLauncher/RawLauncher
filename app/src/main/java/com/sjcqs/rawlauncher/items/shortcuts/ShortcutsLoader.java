package com.sjcqs.rawlauncher.items.shortcuts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sjcqs.rawlauncher.R;
import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.ItemLoader;
import com.sjcqs.rawlauncher.items.apps.App;
import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.views.ShortcutLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by satyan on 9/5/17.
 */

class ShortcutsLoader extends ItemLoader {
    private static final String TAG = ShortcutsLoader.class.getName();
    private final AppManager appManager;

    ShortcutsLoader(Context context, AppManager appManager) {
        super(context);
        this.appManager = appManager;
    }

    @Override
    public List<Item> loadInBackground() {
        List<AppStats> frequentApps = new ArrayList<>();
        List<Item> shortcuts = new ArrayList<>(ShortcutLayout.MAX_SHORTCUT_NUMBER);
        frequentApps.clear();
        List<Item> apps = appManager.getItems();
        for (Item app : apps) {
            SharedPreferences pref = PreferenceManager
                    .getDefaultSharedPreferences(context);
            String label = app.getDiscriminator();
            long time = pref.getLong(
                    context.getString(R.string.time_shared_pref, label),
                    0);
            long count = pref.getLong(
                    context.getString(R.string.count_shared_pref, label),
                    0);

            AppStats stats = new AppStats((App) app, count, time);

            if (!frequentApps.contains(stats)) {
                frequentApps.add(stats);
            }
        }

        Collections.sort(frequentApps, AppStats.FREQUENCE_COMPARATOR);

        shortcuts.clear();
        for (int i = 0; i < ShortcutLayout.MAX_SHORTCUT_NUMBER; i++) {
            App app = frequentApps.get(i).app;
            Log.d(TAG, app.getLabel() + ": " + i + " " + frequentApps.get(i).count + " " + frequentApps.get(i).time);
            shortcuts.add(app);
        }
        items = shortcuts;
        return shortcuts;
    }

    @Override
    protected void onStartLoading() {
        if (items != null) {
            deliverResult(items);
        }


        if (takeContentChanged() || items == null) {
            final Handler h = new Handler();
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    if (!appManager.isLoaded()) {
                        h.postDelayed(this, 100);
                    } else {
                        forceLoad();
                    }
                }
            };
            h.post(task);
        }
    }


    private static class AppStats {
        static Comparator<AppStats> FREQUENCE_COMPARATOR = new Comparator<AppStats>() {
            @Override
            public int compare(AppStats a1, AppStats a2) {
                if (a1.count == a2.count) {
                    return -Long.compare(a1.time, a2.time);
                }
                return -Long.compare(a1.count, a2.count);
            }
        };
        final App app;
        long count;
        long time;

        private AppStats(App app, long count, long time) {
            this.app = app;
            this.count = count;
            this.time = time;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof App && app.getLabel().equals(((App) obj).getLabel());
        }
    }
}
