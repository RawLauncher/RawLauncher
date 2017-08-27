package com.sjcqs.rawlauncher.utils.interfaces;

/**
 * Created by satyan on 8/24/17.
 * Use to describe data that can be loaded
 */

public interface DataLoadingListener<T> {
    /**
     * Data was loaded
     * @param data the loaded data
     */
    void onDataLoaded(T data);

    /**
     * Data was reset
     */
    void onDataReset();
}
