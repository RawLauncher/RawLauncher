package com.sjcqs.rawlauncher.items;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by satyan on 8/27/17.
 */

public abstract class ItemLoader<T> extends AsyncTaskLoader<T> {

    protected T items;

    public ItemLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(T data) {
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
    public void onCanceled(T data) {
        super.onCanceled(data);
        cleanUp(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    private void cleanUp(T apps){
        // clean up used resources
    }
}
