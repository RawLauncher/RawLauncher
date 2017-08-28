package com.sjcqs.rawlauncher.items.suggestions;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjcqs.rawlauncher.R;
import com.sjcqs.rawlauncher.RawLauncher;
import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.items.device_settings.DeviceSettingManager;
import com.sjcqs.rawlauncher.items.search.InputSearchManager;
import com.sjcqs.rawlauncher.utils.LoaderUtils;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;
import com.sjcqs.rawlauncher.utils.interfaces.OnItemLaunchedListener;
import com.sjcqs.rawlauncher.utils.interfaces.Suggestor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by satyan on 8/25/17.
 */

public class SuggestionManager extends  RecyclerView.Adapter<SuggestionManager.ItemHolder> implements LoaderManager.LoaderCallbacks<List<Suggestion>>,Suggestor {
    private static final String TAG = SuggestionManager.class.getName();
    private final Context context;
    private final LoaderManager loaderManager;
    private final List<Manager> managers;
    private final InputSearchManager searchManager;
    private OnItemLaunchedListener onItemLaunchedListener;

    private List<Suggestion> suggestions;

    public SuggestionManager(Context context, LoaderManager supportLoaderManager, List<Manager> managers) {
        this.context = context;
        this.loaderManager = supportLoaderManager;
        searchManager = new InputSearchManager(context,loaderManager);
        this.managers = managers;
        suggestions = new ArrayList<>();
    }


    @Override
    public void suggest(String input) {
        Bundle bundle = new Bundle();
        bundle.putString(context.getString(R.string.arg_input),input);
        suggestions.clear();
        loaderManager.restartLoader(LoaderUtils.SUGGESTION_LOADER,bundle,this);
    }

    @Override
    public void clearSuggestions() {
        int size = suggestions.size();
        suggestions.clear();
        loaderManager.destroyLoader(LoaderUtils.SUGGESTION_LOADER);
        notifyItemRangeRemoved(0,size);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        final Item item = suggestions.get(position);
        if (item != null) {
            holder.setIcon(item.getIcon());
            holder.setLabel(item.getLabel());
            holder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    onItemLaunchedListener.onItemLaunched(item);
                    context.startActivity(item.getIntent());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public void clearOnItemLaunchedListener() {
        onItemLaunchedListener = null;
    }

    public void setOnItemLaunchedListener(OnItemLaunchedListener listener) {
        this.onItemLaunchedListener = listener;
    }

    public Intent getIntent(int i) {
        try {
            Suggestion suggestion = suggestions.get(i);
            return  suggestion.getIntent();
        } catch (IndexOutOfBoundsException e){
            Log.w(TAG, "getIntent: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public Loader<List<Suggestion>> onCreateLoader(int id, Bundle args) {
        String str = args.getString(context.getString(R.string.arg_input));
        return new SuggestionLoader(context, str, managers,searchManager);
    }

    @Override
    public void onLoadFinished(Loader<List<Suggestion>> loader, List<Suggestion> data) {
        suggestions.clear();
        suggestions.addAll(data);
        Collections.sort(suggestions,Suggestion.SUGGESTION_COMPARATOR);
        notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Suggestion>> loader) {
        //suggestions.clear();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        View view;
        private TextView labelView;
        private ImageView iconView;

        ItemHolder(View itemView) {
            super(itemView);
            view = itemView;
            labelView = itemView.findViewById(R.id.label);
            iconView = itemView.findViewById(R.id.item_icon);
        }

        void setIcon(Drawable drawable){
            iconView.setImageDrawable(drawable);
        }

        void setLabel(String text){
            labelView.setText(text);
        }

        void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}
