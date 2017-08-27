package com.sjcqs.rawlauncher.utils.interfaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by satyan on 8/24/17.
 */

public abstract class Manager {

    protected final AppCompatActivity activity;

    protected Manager(AppCompatActivity activity) {
        this.activity = activity;
    }

    public abstract boolean isLoaded();

    public abstract Intent getIntent(String str);
}
