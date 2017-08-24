package com.sjcqs.rawlauncher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjcqs.rawlauncher.R;

public class UserInputView extends RelativeLayout {

    private EditText userEditText;
    private ImageView iconView;

    private Drawable imageDrawable;
    private String hint;
    private boolean requestFocus = true;


    public UserInputView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_user_input,this);
        init(context,null,0);
    }

    public UserInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public UserInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(final Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            // Load attributes
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.UserInputView, defStyle, 0);
            try {
                hint = a.getString(R.styleable.UserInputView_UserInputView_hint);
                if (hint == null) {
                    hint = getResources().getString(R.string.hint_launch);
                }
                imageDrawable = a.getDrawable(R.styleable.UserInputView_UserInputView_icon);

                requestFocus = a.getBoolean(R.styleable.UserInputView_UserInputView_requestFocus,true);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(R.layout.view_user_input,this);
        userEditText = findViewById(R.id.user_input);
        iconView = findViewById(R.id.launcher_icon);

        if (imageDrawable != null){
            setIconView(imageDrawable);
        }

        setHint(hint);

        if (requestFocus) {
            userEditText.requestFocus();
            InputMethodManager imm =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(userEditText, InputMethodManager.SHOW_IMPLICIT);
        }

        clearInput();
    }

    public void setHint(final String hint) {
        userEditText.post(new Runnable() {
            @Override
            public void run() {
                userEditText.setHint(hint);
            }
        });
    }

    public void setRequestFocus(boolean requestFocus) {
        this.requestFocus = requestFocus;
    }

    public void clearInput(){
        setInput("");
    }

    public void setIconView(final Drawable drawable) {
        iconView.post(new Runnable() {
            @Override
            public void run() {
                iconView.setImageDrawable(drawable);
            }
        });
    }

    public void setInput(final String str) {
        userEditText.post(new Runnable() {
            @Override
            public void run() {
                userEditText.setText(str);
            }
        });
    }

    private void setOnEditorActionListener(TextView.OnEditorActionListener listener){
        userEditText.setOnEditorActionListener(listener);
    }


}
