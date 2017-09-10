package com.sjcqs.rawlauncher;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sjcqs.rawlauncher.utils.interfaces.Reloadable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyan on 9/6/17.
 */

public class HistoryManager implements Reloadable {
    private static final String TAG = HistoryManager.class.getName();
    private static final int HISTORY_SIZE = 20;
    private final Context context;
    private String lastItem;
    private int index = 0;
    private List<String> history = new ArrayList<>();

    public HistoryManager(Context context) {
        this.context = context;
    }


    @Override
    public void reload() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int size = pref.getInt(context.getString(R.string.history_size_shared_preferences), 0);
        history.clear();
        for (int i = 0; i < size && i < HISTORY_SIZE; i++) {
            String item = pref
                    .getString(context.getString(R.string.history_item_shared_preferences, i), "");
            if (!item.isEmpty()) {
                history.add(item);
            }
            lastItem = item;
            Log.d(TAG, index + ": " + item);
        }
        pref.edit()
                .putInt(context.getString(R.string.history_size_shared_preferences), history.size())
                .apply();
        index = history.size() - 1;
    }

    public String previous() {

        Log.d(TAG, "next: " + (index + 1) + " / " + history.size());
        String str = "";

        if (index < 0) {
            return "";
        }
        if (index >= history.size()) {
            index = history.size() - 1;
        }
        if (history.size() > index && index >= 0) {
            str = history.get(index);
        }
        index--;
        Log.d(TAG, index + ": " + str);
        return str;
    }

    public String next() {
        Log.d(TAG, "next: " + (index + 1) + " / " + history.size());
        String str = "";
        if (index >= history.size()) {
            return "";
        }

        if (index < 0) {
            index = 0;
        }
        if (history.size() > index && index >= 0) {
            str = history.get(index);
        }
        index++;
        Log.d(TAG, index + ": " + str);
        return str;
    }

    public void push(String str) {
        if (str.isEmpty() || str.equals(lastItem)) {
            return;
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        history.add(str);
        int diff = history.size() - HISTORY_SIZE;
        if (diff > 0) {
            history = history.subList(diff, history.size());
        }
        lastItem = str;
        index = history.size() - 1;
        for (int i = 0; i < history.size(); i++) {
            editor.putString(
                    context.getString(R.string.history_item_shared_preferences, index),
                    history.get(i)
            );
        }
        editor.putInt(context.getString(R.string.history_size_shared_preferences), history.size());
        editor.apply();
    }
}
