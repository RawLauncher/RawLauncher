package com.sjcqs.rawlauncher;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.items.device_settings.DeviceSettingManager;
import com.sjcqs.rawlauncher.items.suggestions.SuggestionManager;
import com.sjcqs.rawlauncher.utils.interfaces.OnItemLaunchedListener;
import com.sjcqs.rawlauncher.views.UserInputView;

/**
 * The main launcher context
 */
public class RawLauncher extends AppCompatActivity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private static final String TAG = RawLauncher.class.getName();
    private UserInputView inputView;
    private AppManager appManager;
    private DeviceSettingManager deviceSettingManager;
    private SuggestionManager suggestionManager;
    private RecyclerView suggestionRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View rootView = getLayoutInflater().inflate(R.layout.activity_raw_launcher, null);
        setContentView(rootView);
        appManager = new AppManager(this, getSupportLoaderManager());
        deviceSettingManager = new DeviceSettingManager(this,getSupportLoaderManager());

        inputView = (UserInputView) findViewById(R.id.user_input_view);

        suggestionRecyclerView = (RecyclerView) findViewById(R.id.suggestions);
        suggestionManager = new SuggestionManager(this, getSupportLoaderManager(), appManager, deviceSettingManager);
        suggestionRecyclerView.setAdapter(suggestionManager);

        suggestionRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (suggestionManager.getItemCount() > 0){
                    suggestionRecyclerView.scrollToPosition(0);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
            }
        });

        inputView.setOnActionDoneListener(new UserInputView.OnActionDoneListener() {
            @Override
            public boolean onActionDone(String str) {
                Intent intent = appManager.getIntent(str);
                if (intent == null){
                    intent = suggestionManager.getIntent(0);
                }
                if (intent != null){
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        suggestionManager.setOnItemLaunchedListener(new OnItemLaunchedListener() {
            @Override
            public void onItemLaunched(Item item) {
                inputView.clearInput();
            }
        });

        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0){
                    final String str = charSequence.toString();
                    inputView.post(new Runnable() {
                        @Override
                        public void run() {
                            suggestionManager.suggest(str);
                        }
                    });
                } else {
                    suggestionManager.clearSuggestions();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View view, View view1) {
                Log.d(TAG, "onGlobalFocusChanged:");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        inputView.showKeyboard(this);
        appManager.reload();
    }
}
