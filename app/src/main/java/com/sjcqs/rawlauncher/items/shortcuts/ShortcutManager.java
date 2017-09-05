package com.sjcqs.rawlauncher.items.shortcuts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.utils.ManagerUtils;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;
import com.sjcqs.rawlauncher.utils.interfaces.OnItemClickedListener;
import com.sjcqs.rawlauncher.utils.interfaces.Reloadable;
import com.sjcqs.rawlauncher.views.ShortcutLayout;

import java.util.List;

/**
 * Created by satyan on 9/1/17.
 * Manage app shortcuts
 */

public class ShortcutManager extends Manager implements Reloadable {
    private static final String TAG = ShortcutManager.class.getName();
    private final ShortcutLayout shortcutLayout;
    private final AppManager appManager;
    private OnItemClickedListener onItemClickedListener;

    public ShortcutManager(
            final Context context, LoaderManager loaderManager,
            final AppManager appManager, final ShortcutLayout shortcutLayout
    ) {
        super(context, loaderManager);

        this.shortcutLayout = shortcutLayout;
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
                    Item app = items.get(id);
                    if (onItemClickedListener != null) {
                        onItemClickedListener.onItemClicked(app);
                    }
                    return true;
                } catch (IndexOutOfBoundsException e) {
                    Log.w(TAG, "Shortcut #" + id + " doesn't exist", e);
                }
                return false;
            }
        });
    }

    @Override
    public void reload() {
        loaderManager.restartLoader(ManagerUtils.ID_SHORTCUTS_LOADER, null, this);
    }

    public void clearOnItemLaunchedListener() {
        onItemClickedListener = null;
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        this.onItemClickedListener = listener;
    }

    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        return new ShortcutsLoader(context, appManager);
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
        super.onLoadFinished(loader, data);
        for (int i = 0; i < ShortcutLayout.MAX_SHORTCUT_NUMBER && i < items.size(); i++) {
            Item item = items.get(i);
            shortcutLayout.setIcon(i, item.getIcon());
        }
        shortcutLayout.setVisibility(View.VISIBLE);
    }
}
