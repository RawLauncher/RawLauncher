package com.sjcqs.rawlauncher.items.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.sjcqs.rawlauncher.R;
import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.ItemLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyan on 8/28/17.
 */

class InputSearchLoader extends ItemLoader {
    private static final double LOW_PRIORITY = 1.3;
    private static final double MEDIUM_PRIORITY = 1.2;
    private static final double HIGH_PRIORITY = 1.1;
    InputSearchLoader(Context context) {
        super(context);
    }

    private static final SearchData[] SEARCHES_DATA = {
            new SearchData("Play Store", "market://search?q=", R.drawable.ic_google_play_black_24dp,HIGH_PRIORITY),
            new SearchData("Google","http://www.google.com/#q=",R.drawable.ic_google_black_24dp,HIGH_PRIORITY),
            new SearchData("Duck Duck Go","https://duckduckgo.com/?q=",R.drawable.ic_duckduckgo_24dp,MEDIUM_PRIORITY),
            new SearchData("Youtube","https://www.youtube.com/results?search_query=",R.drawable.ic_youtube_play_black_24dp,MEDIUM_PRIORITY)
    };

    /*private static final String YOUTUBE_PREFIX = "https://www.youtube.com/results?search_query=";
    private static final String GOOGLE_PREFIX = ;
    private static final String PLAYSTORE_PREFIX = "market://search?q=";
    private static final String PLAYSTORE_BROWSER_PREFIX = "https://play.google.com/store/search?q=";
    private static final String DUCKDUCKGO_PREFIX = "https://duckduckgo.com/?q=";*/

    @Override
    public List<Item> loadInBackground() {
        items = new ArrayList<>();
        for (SearchData data : SEARCHES_DATA) {
            Drawable icon = context.getDrawable(data.src);
            if (icon != null){
                icon.setTint(ResourcesCompat.getColor(context.getResources(),R.color.color_secondary,null));
            }
            Item search = new InputSearch(data.label, icon, data.link, data.priority);
            items.add(search);
        }
        return items;
    }

    private static class SearchData {
        final String label;
        final String link;
        final double priority;
        final int src;

        private SearchData(String label, String link, int drawable, double priority) {
            this.label = label;
            this.link = link;
            this.src = drawable;
            this.priority = priority;
        }

    }
}
