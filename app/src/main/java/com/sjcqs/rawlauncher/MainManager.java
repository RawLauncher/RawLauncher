package com.sjcqs.rawlauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.items.shortcuts.ShortcutManager;
import com.sjcqs.rawlauncher.items.suggestions.SuggestionManager;
import com.sjcqs.rawlauncher.utils.ManagerUtils;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;
import com.sjcqs.rawlauncher.utils.interfaces.OnItemClickedListener;
import com.sjcqs.rawlauncher.utils.interfaces.Reloadable;
import com.sjcqs.rawlauncher.utils.interfaces.Suggestor;
import com.sjcqs.rawlauncher.views.ShortcutLayout;
import com.sjcqs.rawlauncher.views.UserInputView;

import java.util.Date;
import java.util.Map;

/**
 * Created by satyan on 8/23/17.
 * Manager for interaction between the app objects
 */

class MainManager implements OnItemClickedListener, Reloadable, Suggestor {

    private static final String TAG = MainManager.class.getName();
    private final RawLauncher raw;
    private final Map<Integer, Manager> managers;
    private final SuggestionManager suggestionManager;
    private final ShortcutManager shortcutManager;
    private final HistoryManager historyManager;

    private final UserInputView inputView;
    private boolean shortcutsUpToDate = false;

    MainManager(
            RawLauncher activity,
            final UserInputView inputView,
            final RecyclerView suggestionRecyclerView,
            ShortcutLayout shortcutLayout
    ) {
        this.raw = activity;
        this.inputView = inputView;

        LoaderManager loaderManager = raw.getSupportLoaderManager();
        managers = ManagerUtils.loadManagers(raw, loaderManager);
        suggestionManager = new SuggestionManager(raw, loaderManager, managers.values());
        suggestionRecyclerView.setAdapter(suggestionManager);

        shortcutManager = new ShortcutManager(
                raw,
                loaderManager,
                (AppManager) managers.get(ManagerUtils.MANAGER_APPS),
                shortcutLayout
        );

        suggestionManager.setOnItemClickedListener(this);

        shortcutManager.setOnItemClickedListener(this);

        inputView.setOnInputActionListener(new UserInputView.OnInputActionListener() {
            @Override
            public boolean onActionDone(String str) {
                return launchItem(0);
            }

            @Override
            public void onUpPressed() {
                String str = historyManager.previous();
                inputView.setInput(str);
            }

            @Override
            public void onDownPressed() {
                String str = historyManager.next();
                inputView.setInput(str);
            }
        });

        historyManager = new HistoryManager(raw);
    }

    private void updateItemStats(Item item) {
        if (item.canBeAShortcut()) {
            shortcutsUpToDate = false;
            Log.d(TAG, "updateItemStats: " + item.getLabel());
            SharedPreferences pref =
                    PreferenceManager.getDefaultSharedPreferences(raw);
            String label = item.getDiscriminator();
            long time;
            long count = pref.getLong(raw.getString(R.string.count_shared_pref, label), 0);

            count++;
            time = new Date().getTime();

            pref.edit()
                    .putLong(raw.getString(R.string.time_shared_pref, label), time)
                    .putLong(raw.getString(R.string.count_shared_pref, label), count)
                    .apply();
        }
    }

    @Override
    public void onItemClicked(final Item item) {
        historyManager.push(item.getLabel());
        inputView.clearInput();
        inputView.hideKeyboard();
        updateItemStats(item);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                raw.startActivity(item.getIntent());
            }
        }, 100);

    }

    @Override
    public void reload() {
        historyManager.reload();
        if (!shortcutsUpToDate) {
            shortcutManager.reload();
            shortcutsUpToDate = true;
        }
        for (Manager manager : managers.values()) {
            manager.reload();
        }
    }

    boolean canBeUninstalled(int position) {
        return suggestionManager.getItem(position).canBeUninstalled();
    }

    void hideItem(int position) {
        Item item = suggestionManager.hideItem(position);
        if (item != null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(raw);
            pref.edit()
                    .putBoolean(
                            raw.getString(
                                    R.string.hide_shared_pref,
                                    item.getDiscriminator()
                            ), true)
                    .apply();
        }
    }

    void uninstallApp(int position) {
        Item item = suggestionManager.uninstallApp(position);
        if (item != null) {
            Intent intent = item.getUninstallIntent();
            raw.startActivity(intent);
        }
    }

    boolean launchItem(int i) {
        Item item = suggestionManager.getItem(i);
        if (item != null) {
            onItemClicked(item);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void suggest(String input) {
        suggestionManager.suggest(input);
    }

    @Override
    public void clearSuggestions() {
        suggestionManager.clearSuggestions();
    }
}
