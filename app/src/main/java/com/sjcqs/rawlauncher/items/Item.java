package com.sjcqs.rawlauncher.items;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.sjcqs.rawlauncher.utils.interfaces.Shortcutable;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by satyan on 8/24/17.
 */

public abstract class Item implements Shortcutable {

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
    protected final Drawable icon;
    protected final Intent intent;
    protected CharSequence label;

    protected Item(String label, Drawable drawable, Intent intent){
        this.label = label;
        this.icon = drawable;
        this.intent = intent;
    }

    public String getLabel(){
        return label.toString();
    }

    public Drawable getIcon(){
        return icon;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getInput() {
        return label.toString();
    }

    @Override
    public String toString() {
        return label + ": " + icon.toString();
    }

    @Override
    public boolean isShortcutable() {
        return false;
    }
}
