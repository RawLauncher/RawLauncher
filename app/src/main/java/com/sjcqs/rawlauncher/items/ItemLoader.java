package com.sjcqs.rawlauncher.items;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by satyan on 8/27/17.
 */

public abstract class ItemLoader<T extends Item> extends AsyncTaskLoader<List<T>> {

    protected List<T> items;
    protected Context context;

    public ItemLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void deliverResult(List<T> data) {
        if (isReset()){
            if (data != null){
                cleanUp(data);
            }
        }

        this.items = data;

        if (isStarted()){
            super.deliverResult(data);
        }

        if (data != null){
            cleanUp(data);
        }

    }

    @Override
    protected void onStartLoading() {
        if (items != null){
            deliverResult(items);
        }

        if (takeContentChanged() || items == null){
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (items != null){
            cleanUp(items);
            items = null;
        }
    }

    @Override
    public void onCanceled(List<T> data) {
        super.onCanceled(data);
        cleanUp(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    private void cleanUp(List<T> apps){
        // clean up used resources
    }
}
