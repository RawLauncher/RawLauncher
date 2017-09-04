package com.sjcqs.rawlauncher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjcqs.rawlauncher.R;
import com.sjcqs.rawlauncher.utils.interfaces.OnActionListener;

/**
 * Created by satyan on 9/1/17.
 */

public class ShortcutView extends RelativeLayout {
    private static final String TAG = ShortcutView.class.getName();
    private OnActionListener onActionListener;
    private GestureDetectorCompat detector;
    private TextView labelText;
    private ImageButton iconButton;

    public ShortcutView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ShortcutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShortcutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyle) {
        Drawable icon = null;
        String label = null;

        if (attrs != null) {
            // Load attributes
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.UserInputView, defStyle, 0);
            try {
                icon = a.getDrawable(R.styleable.ShortcutView_shortcutIcon);
                label = a.getString(R.styleable.ShortcutView_shortcutLabel);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(R.layout.view_shortcut, this);
        detector = new GestureDetectorCompat(context, new ShortcutView.InputGestureListener());
        detector.setIsLongpressEnabled(true);


        labelText = findViewById(R.id.shortcut_label);
        setLabel(label);

        iconButton = findViewById(R.id.shortcut_icon);
        if (icon != null) {
            setIcon(icon);
        }
        iconButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return detector.onTouchEvent(motionEvent);
            }
        });
    }

    /**
     * Set the label for the shortcut, if null the label is hidden
     *
     * @param label the label to give to the shortcut
     */
    public void setLabel(@Nullable String label) {
        if (label == null) {
            labelText.setVisibility(GONE);
        } else {
            labelText.setVisibility(VISIBLE);
            labelText.setText(label);
        }
    }

    /**
     * Set the icon for the shortcut
     *
     * @param icon icon given
     */
    public void setIcon(@NonNull Drawable icon) {
        iconButton.setImageDrawable(icon);
    }

    public OnActionListener getOnActionListener() {
        return onActionListener;
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    private class InputGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return onActionListener != null && onActionListener.onPressed();
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            if (onActionListener != null) {
                onActionListener.onLongPressed();
            }
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    }
}
