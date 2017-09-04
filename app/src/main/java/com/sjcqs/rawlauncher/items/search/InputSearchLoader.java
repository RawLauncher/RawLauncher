package com.sjcqs.rawlauncher.items.search;

import android.content.Context;
import android.content.Intent;
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
    public static final double BASE_PRIORITY = 10;
    private static final double LOW_PRIORITY = 15;
    private static final double MEDIUM_PRIORITY = 16;
    private static final double HIGH_PRIORITY = 17;
    private static final SearchData[] SEARCHES_DATA = {
            new SearchData("Play Store", "market://search?q=", null, R.drawable.ic_google_play_black_24dp, HIGH_PRIORITY),
            new SearchData("Google", "http://www.google.com/#q=", null, R.drawable.ic_google_black_24dp, HIGH_PRIORITY),
            new SearchData("Duck Duck Go", "https://duckduckgo.com/?q=", null, R.drawable.ic_duckduckgo_24dp, MEDIUM_PRIORITY),
            new SearchData("Youtube", "https://www.youtube.com/results?search_query=", null, R.drawable.ic_youtube_play_black_24dp, MEDIUM_PRIORITY),
            new SearchData("Maps", "geo:0,0?q=", "com.google.android.apps.maps", R.drawable.ic_place_black_24dp, MEDIUM_PRIORITY),
    };

    InputSearchLoader(Context context) {
        super(context);
    }

    @Override
    public List<Item> loadInBackground() {
        items = new ArrayList<>();
        for (SearchData data : SEARCHES_DATA) {
            Drawable icon = context.getDrawable(data.src);
            if (icon != null){
                icon.setTint(ResourcesCompat.getColor(context.getResources(),R.color.color_secondary,null));
            }
            Item search;
            if (data.packageName == null) {
                search = new InputSearch(data.label, icon, data.link, data.priority);
            } else {
                //Uri gmmIntentUri = Uri.parse("geo:0,0?q=1600 Amphitheatre Parkway, Mountain+View, California");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage(data.packageName);
                search = new InputSearch(data.label, icon, intent, data.link, data.priority);
            }
            items.add(search);
        }
        return items;
    }

    private static class SearchData {
        final String label;
        final String link;
        final String packageName;
        final double priority;
        final int src;

        private SearchData(String label, String link, String packageName, int drawable, double priority) {
            this.label = label;
            this.link = link;
            this.packageName = packageName;
            this.src = drawable;
            this.priority = priority;
        }

    }
}
