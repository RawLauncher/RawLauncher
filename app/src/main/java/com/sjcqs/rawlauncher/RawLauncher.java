package com.sjcqs.rawlauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.items.shortcuts.ShortcutManager;
import com.sjcqs.rawlauncher.items.suggestions.SuggestionManager;
import com.sjcqs.rawlauncher.utils.ManagerUtils;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;
import com.sjcqs.rawlauncher.utils.interfaces.OnItemLaunchedListener;
import com.sjcqs.rawlauncher.views.ShortcutLayout;
import com.sjcqs.rawlauncher.views.UserInputView;

import java.util.Date;
import java.util.Map;

/**
 * The main launcher context
 *
 * IDEAS: ouvrir quand application seule, trier application par utilisation, raccourcis
 */
public class RawLauncher extends AppCompatActivity implements OnItemLaunchedListener {
    private static final String TAG = RawLauncher.class.getName();

    private UserInputView inputView;
    private Map<Integer, Manager> managers;
    private SuggestionManager suggestionManager;
    private RecyclerView suggestionRecyclerView;
    private ShortcutManager shortcutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View rootView = getLayoutInflater().inflate(R.layout.activity_raw_launcher, null);
        setContentView(rootView);

        managers = ManagerUtils.loadManagers(this, getSupportLoaderManager());
        inputView = (UserInputView) findViewById(R.id.user_input_view);

        suggestionRecyclerView = (RecyclerView) findViewById(R.id.suggestions);
        suggestionManager = new SuggestionManager(this, getSupportLoaderManager(), managers.values());
        suggestionRecyclerView.setAdapter(suggestionManager);

        ShortcutLayout shortcutLayout = (ShortcutLayout) findViewById(R.id.shortcuts);
        shortcutManager = new ShortcutManager(this, (AppManager) managers.get(ManagerUtils.MANAGER_APPS), shortcutLayout);

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
                final Intent intent = suggestionManager.getIntent(0);
                if (intent != null){
                    Item item = suggestionManager.getItem(0);
                    updateItemStats(item);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        suggestionManager.setOnItemLaunchedListener(this);

        shortcutManager.setOnItemLaunchedListener(this);

        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0){
                    final String str = charSequence.toString();
                    suggestionManager.suggest(str);
                } else {
                    suggestionManager.clearSuggestions();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updateItemStats(Item item) {
        if (item.isShortcutable()) {
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
            String label = item.getLabel();
            long time;
            long count = sharedPreferences.getLong(label + getString(R.string.count_shared_pref), 0);

            count++;
            time = new Date().getTime();

            Log.d(TAG, label + ": " + time + "; " + count);


            sharedPreferences.edit()
                    .putLong(label + getString(R.string.time_shared_pref), time)
                    .putLong(label + getString(R.string.count_shared_pref), count)
                    .apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        inputView.showKeyboard(this);
        shortcutManager.update();
        suggestionManager.clearSuggestions();
        for (Manager manager : managers.values()) {
            manager.reload();
        }
    }

    @Override
    public void onItemLaunched(Item item) {
        inputView.clearInput();
        updateItemStats(item);
    }
}
