package com.sjcqs.rawlauncher.items;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by satyan on 8/24/17.
 */

public abstract class Item {
    public static final Comparator<? super Item> ALPHA_COMPARATOR = new Comparator<Item>() {
        private final Collator COLLATOR = Collator.getInstance(Locale.getDefault());

        @Override
        public int compare(Item app1, Item app2) {
            return (COLLATOR.compare(app1.getLabel(), app2.getLabel()));
        }
    };
    public static final Comparator<? super Item> REVERSE_ALPHA_COMPARATOR = new Comparator<Item>() {
        private final Collator COLLATOR = Collator.getInstance(Locale.getDefault());

        @Override
        public int compare(Item app1, Item app2) {
            return -(COLLATOR.compare(app1.getLabel(), app2.getLabel()));
        }
    };

    protected CharSequence label;
    protected Drawable icon;

    public String getLabel(){
        return label.toString();
    }

    public Drawable getIcon(){
        return icon;
    }

    public abstract Intent getIntent();

    public String getInput() {
        return label.toString();
    }
}
