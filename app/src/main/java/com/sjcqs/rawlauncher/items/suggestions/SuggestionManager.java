package com.sjcqs.rawlauncher.items.suggestions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.search.InputSearchManager;
import com.sjcqs.rawlauncher.utils.ManagerUtils;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;
import com.sjcqs.rawlauncher.utils.interfaces.OnItemClickedListener;
import com.sjcqs.rawlauncher.utils.interfaces.Suggestor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by satyan on 8/25/17.
 * Manage suggestions
 */

public class SuggestionManager extends  RecyclerView.Adapter<SuggestionManager.ItemHolder> implements LoaderManager.LoaderCallbacks<List<Suggestion>>,Suggestor {
    private static final String TAG = SuggestionManager.class.getName();
    private final Context context;
    private final LoaderManager loaderManager;
    private final Collection<Manager> managers;
    private final InputSearchManager searchManager;
    private OnItemClickedListener onItemClickedListener;

    private List<Suggestion> suggestions;
    private boolean idle = true;

    public SuggestionManager(Context context, LoaderManager supportLoaderManager, Collection<Manager> managers) {
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
        loaderManager.restartLoader(ManagerUtils.ID_SUGGESTION_LOADER, bundle, this);
    }

    @Override
    public void clearSuggestions() {
        suggestions.clear();
        loaderManager.destroyLoader(ManagerUtils.ID_SUGGESTION_LOADER);
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        final Suggestion item = suggestions.get(position);
        if (item != null) {
            holder.setIcon(item.getIcon());
            holder.setLabel(item.getLabel());
            holder.setOnActionListener(new OnActionListener() {
                @Override
                public void onClicked() {
                    if (onItemClickedListener != null) {
                        onItemClickedListener.onItemClicked(item.getItem());
                    }
                }

                @Override
                public boolean onHide() {
                    return false;
                }

                @Override
                public boolean onRemove() {
                    return false;
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public void clearOnItemLaunchedListener() {
        onItemClickedListener = null;
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        this.onItemClickedListener = listener;
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

    public Item uninstallApp(int position) {
        Item item = getItem(position);
        if (item.canBeUninstalled()) {
            suggestions.remove(position);
            notifyItemRemoved(position);
            return item;
        }
        return null;
    }

    public Item hideItem(int position) {
        Item item = getItem(position);
        notifyItemRemoved(position);
        suggestions.remove(position);
        return item;
    }

    public Item getItem(int i) {
        try {
            return suggestions.get(i).getItem();
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.w(TAG, "getItem: ", e);
            return null;
        }
    }

    public Suggestion getSuggestion(int position) {
        try {
            return suggestions.get(position);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.w(TAG, "getSuggestion: ", e);
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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        for (Suggestion suggestion : data) {
            boolean found =
                    pref.getBoolean(
                            context.getString(
                                    R.string.hide_shared_pref,
                                    suggestion.getDiscriminator()
                            ),
                            false
                    );
            if (!found) {
                suggestions.add(suggestion);
            }
        }
        Collections.sort(suggestions, Suggestion.SUGGESTION_COMPARATOR);
        notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Suggestion>> loader) {

    }

    public boolean isIdle() {
        return idle;
    }


    interface OnActionListener {
        void onClicked();

        boolean onHide();

        boolean onRemove();
    }

    public class ItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private TextView labelView;
        private ImageView iconView;
        private OnActionListener onActionListener;
        private boolean enabled = true;

        ItemHolder(View itemView) {
            super(itemView);
            labelView = itemView.findViewById(R.id.label);
            iconView = itemView.findViewById(R.id.item_icon);
        }

        void setIcon(Drawable drawable){
            iconView.setImageDrawable(drawable);
        }

        void setLabel(String text){
            labelView.setText(text);
        }

        void setOnActionListener(OnActionListener listener) {
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.onActionListener = listener;
        }

        public void clearActionListener() {
            this.onActionListener = null;
        }

        @Override
        public void onClick(View view) {
            if (enabled) {
                onActionListener.onClicked();
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
