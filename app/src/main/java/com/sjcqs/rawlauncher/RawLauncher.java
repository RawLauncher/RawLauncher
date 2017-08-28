package com.sjcqs.rawlauncher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.suggestions.SuggestionManager;
import com.sjcqs.rawlauncher.utils.LoaderUtils;
import com.sjcqs.rawlauncher.utils.interfaces.Manager;
import com.sjcqs.rawlauncher.utils.interfaces.OnItemLaunchedListener;
import com.sjcqs.rawlauncher.views.UserInputView;

import java.util.List;

/**
 * The main launcher context
 *
 * IDEAS: ouvrir quand application seule, trier application par utilisation, raccourcis
 */
public class RawLauncher extends AppCompatActivity {
    private static final String TAG = RawLauncher.class.getName();

    private UserInputView inputView;
    private List<Manager> managers;
    private SuggestionManager suggestionManager;
    private RecyclerView suggestionRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View rootView = getLayoutInflater().inflate(R.layout.activity_raw_launcher, null);
        setContentView(rootView);

        managers = LoaderUtils.loadManagers(this, getSupportLoaderManager());
        inputView = (UserInputView) findViewById(R.id.user_input_view);

        suggestionRecyclerView = (RecyclerView) findViewById(R.id.suggestions);
        suggestionManager = new SuggestionManager(this, getSupportLoaderManager(), managers);
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
                Intent intent = suggestionManager.getIntent(0);
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

    @Override
    protected void onResume() {
        super.onResume();
        inputView.showKeyboard(this);
        suggestionManager.clearSuggestions();
        for (Manager manager : managers) {
            manager.reload();
        }
    }
}
