package com.sjcqs.rawlauncher.items.suggestions;

import com.sjcqs.rawlauncher.items.Item;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by satyan on 8/25/17.
 */

public class Suggestion extends Item{
    static final Comparator<? super Suggestion> SUGGESTION_COMPARATOR =
            new Comparator<Suggestion>() {
                private final Collator COLLATOR = Collator.getInstance(Locale.getDefault());

                @Override
                public int compare(Suggestion item1, Suggestion item2) {
                    double rate1 = item1.getRate(), rate2 = item2.getRate();
                    int rateCmp = Double.compare(rate1,rate2);
                    return (rateCmp != 0 ?
                            rateCmp : (COLLATOR.compare(item1.getLabel(), item2.getLabel())));
                }
            };
    private final Item item;
    private final double rate;


    public Suggestion(Item item, double rate) {
        super(item.getLabel(),item.getIcon(), item.getIntent());
        this.rate = rate;
        this.item = item;
    }

    public double getRate() {
        return rate;
    }

    public Item getItem() {
        return item;
    }
}
