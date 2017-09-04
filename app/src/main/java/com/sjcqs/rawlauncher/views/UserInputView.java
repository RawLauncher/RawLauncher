package com.sjcqs.rawlauncher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjcqs.rawlauncher.R;
import com.sjcqs.rawlauncher.utils.MotionEventUtils;

public class UserInputView extends RelativeLayout {

    private static final String TAG = UserInputView.class.getName();
    private EditText userEditText;
    private ImageButton iconButton;
    private ImageButton clearButton;
    private OnActionDoneListener onActionDoneListener;

    private GestureDetectorCompat detector;
    private boolean requestFocus = true;

    public UserInputView(Context context) {
        super(context);
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
        String hint = "";
        Drawable imageDrawable = null;
        boolean showClearButton = false;
        if (attrs != null) {
            // Load attributes
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.UserInputView, defStyle, 0);
            try {
                hint = a.getString(R.styleable.UserInputView_inputHint);
                if (hint == null) {
                    hint = getResources().getString(R.string.hint_launch);
                }
                imageDrawable = a.getDrawable(R.styleable.UserInputView_inputIcon);

                requestFocus = a.getBoolean(R.styleable.UserInputView_requestFocus,true);

                showClearButton = a.getBoolean(R.styleable.UserInputView_showClearButton,true);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(R.layout.view_user_input,this);
        detector = new GestureDetectorCompat(context,new UserInputView.InputGestureListener());
        userEditText = findViewById(R.id.user_input);
        iconButton = findViewById(R.id.launcher_icon);
        clearButton = findViewById(R.id.button_clear);

        if (imageDrawable != null){
            setIconButton(imageDrawable);
        }

        if (!showClearButton){
            clearButton.setVisibility(GONE);
        }
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearInput();
            }
        });

        /*iconButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                context.startActivity(intent);
            }
        });*/

        setHint(hint);

        if (requestFocus) {
            showKeyboard(context);
        }

        userEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return detector.onTouchEvent(motionEvent);
            }
        });

        userEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_GO){
                    if (onActionDoneListener != null){
                        boolean consumed = onActionDoneListener.onActionDone(userEditText.getText().toString());
                        if (consumed) {
                            clearInput();
                        }
                        return !consumed;
                    }
                }
                return false;
            }
        });

        clearInput();
    }

    public void showKeyboard(Context context) {
        userEditText.requestFocus();
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(userEditText, InputMethodManager.SHOW_IMPLICIT);
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

    public void setIconButton(final Drawable drawable) {
        iconButton.post(new Runnable() {
            @Override
            public void run() {
                iconButton.setImageDrawable(drawable);
            }
        });
    }

    public void addTextChangedListener(TextWatcher watcher){
        userEditText.addTextChangedListener(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher){
        userEditText.removeTextChangedListener(watcher);
    }

    public void setOnActionDoneListener(OnActionDoneListener onActionDoneListener) {
        this.onActionDoneListener = onActionDoneListener;
    }

    public void clearOnActionDoneListener(){
        onActionDoneListener = null;
    }

    public String getInput() {
        return userEditText.getText().toString();
    }

    public void setInput(final String str) {
        userEditText.post(new Runnable() {
            @Override
            public void run() {
                userEditText.setText(str);
                userEditText.setSelection(str.length());
            }
        });
    }

    public interface OnActionDoneListener{
        /**
         * Call when the action done button is pressed
         * @param str the input text
         * @return true if the action was consumed, false otherwise
         */
        boolean onActionDone(String str);
    }

    private class InputGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent fromEvent, MotionEvent toEvent, float v, float v1) {
            MotionEventUtils.Direction direction = MotionEventUtils.getDirection(fromEvent, toEvent);
            switch (direction){
                case NORTH:
                    break;
                case SOUTH:
                    break;
                case EST:
                    break;
                case WEST:
                    clearInput();
                    break;
                default:
                    return false;
            }
            return false;
        }
    }
}
