package com.sjcqs.rawlauncher.items.shortcuts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.sjcqs.rawlauncher.R;
import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.apps.App;
import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.utils.interfaces.OnItemLaunchedListener;
import com.sjcqs.rawlauncher.views.ShortcutLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by satyan on 9/1/17.
 * Manage app shortcuts
 */

public class ShortcutManager {
    private static final String TAG = ShortcutManager.class.getName();
    private final ShortcutLayout shortcutLayout;
    private final Context context;
    private final AppManager appManager;
    private List<App> shortcuts = new ArrayList<>(ShortcutLayout.MAX_SHORTCUT_NUMBER);
    private List<AppStats> sortedApps = new ArrayList<>();
    private OnItemLaunchedListener onItemLaunchedListener;

    public ShortcutManager(final Context context, final AppManager appManager, final ShortcutLayout shortcutLayout) {
        this.shortcutLayout = shortcutLayout;
        this.context = context;
        this.appManager = appManager;

        shortcutLayout.setOnShortcutActionListener(new ShortcutLayout.OnShortcutActionListener() {
            @Override
            public boolean onLongPressed(int id) {
                // FEATURE: Open shortcuts settings
                return false;
            }

            @Override
            public boolean onPressed(int id) {
                try {
                    App app = shortcuts.get(id);
                    if (onItemLaunchedListener != null) {
                        onItemLaunchedListener.onItemLaunched(app);
                    }
                    context.startActivity(app.getIntent());
                    return true;
                } catch (IndexOutOfBoundsException e) {
                    Log.w(TAG, "Shortcut #" + id + " doesn't exist", e);
                }
                return false;
            }
        });
        update();
    }

    public void update() {
        shortcutLayout.setVisibility(View.GONE);
        final Handler h = new Handler();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                if (!appManager.isLoaded()) {
                    h.post(this);
                } else {
                    sortedApps.clear();
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

                        if (!sortedApps.contains(stats)) {
                            sortedApps.add(stats);
                        }
                    }

                    Collections.sort(sortedApps, AppStats.COMPARATOR);

                    shortcuts.clear();
                    for (int i = 0; i < ShortcutLayout.MAX_SHORTCUT_NUMBER; i++) {
                        App app = sortedApps.get(i).app;
                        Log.d(TAG, app.getLabel() + ": " + i);
                        shortcutLayout.setIcon(i, app.getIcon());
                        shortcuts.add(app);
                    }
                    shortcutLayout.setVisibility(View.VISIBLE);
                }
            }
        };
        h.post(task);
    }

    public void clearOnItemLaunchedListener() {
        onItemLaunchedListener = null;
    }

    public void setOnItemLaunchedListener(OnItemLaunchedListener listener) {
        this.onItemLaunchedListener = listener;
    }

    private static class AppStats {
        static Comparator<AppStats> COMPARATOR = new Comparator<AppStats>() {
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
