package com.sjcqs.rawlauncher.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.sjcqs.rawlauncher.R;
import com.sjcqs.rawlauncher.utils.interfaces.OnActionListener;

/**
 * Created by satyan on 9/1/17.
 */

public class ShortcutLayout extends RelativeLayout {
    public final static int MAX_SHORTCUT_NUMBER = 5;
    private static final String TAG = ShortcutLayout.class.getName();
    ShortcutView[] shortcutViews = new ShortcutView[MAX_SHORTCUT_NUMBER];
    boolean shortcutPressable[] = new boolean[MAX_SHORTCUT_NUMBER];

    OnShortcutActionListener onShortcutActionListener;

    public ShortcutLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShortcutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShortcutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            /*final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.ShortcutsLayout, defStyle, 0);
            try {
                icon = a.getDrawable(R.styleable.ShortcutView_shortcutIcon);
                label = a.getString(R.styleable.ShortcutView_shortcutLabel);
            } finally {
                a.recycle();
            }*/
        }
        LayoutInflater.from(context).inflate(R.layout.layout_shortcuts, this);

        shortcutViews[0] = findViewById(R.id.shortcut_01);
        shortcutViews[1] = findViewById(R.id.shortcut_02);
        shortcutViews[2] = findViewById(R.id.shortcut_03);
        shortcutViews[3] = findViewById(R.id.shortcut_04);
        shortcutViews[4] = findViewById(R.id.shortcut_05);

        for (int i = 0; i < shortcutViews.length; i++) {
            shortcutPressable[i] = true;
            final int id = i;
            shortcutViews[i].setOnActionListener(new OnActionListener() {
                @Override
                public boolean onPressed() {
                    return shortcutPressable[id]
                            && onShortcutActionListener != null
                            && onShortcutActionListener.onPressed(id);
                }

                @Override
                public void onLongPressed() {
                    if (onShortcutActionListener != null) {
                        shortcutPressable[id] = onShortcutActionListener.onLongPressed(id);
                    }
                }
            });
        }
    }

    public OnShortcutActionListener getOnShortcutActionListener() {
        return onShortcutActionListener;
    }

    public void setOnShortcutActionListener(OnShortcutActionListener onShortcutActionListener) {
        this.onShortcutActionListener = onShortcutActionListener;
    }

    /**
     * Set the label for the shortcut #id, if null the label is hidden
     *
     * @param id    shortcut's id
     * @param label the label to give to the shortcut
     */
    public void setLabel(int id, @Nullable String label) {
        shortcutViews[id].setLabel(label);
    }

    /**
     * Set the icon for the shortcut #id
     *
     * @param id   shortcut's id
     * @param icon icon given
     */
    public void setIcon(int id, @NonNull Drawable icon) {
        shortcutViews[id].setIcon(icon);
    }

    public interface OnShortcutActionListener {
        boolean onLongPressed(int id);

        boolean onPressed(int id);
    }
}
