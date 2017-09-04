package com.sjcqs.rawlauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.sjcqs.rawlauncher.items.Item;
import com.sjcqs.rawlauncher.items.apps.AppManager;
import com.sjcqs.rawlauncher.items.shortcuts.ShortcutManager;
import com.sjcqs.rawlauncher.items.suggestions.Suggestion;
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

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            public int margin;
            public Drawable uninstallIcon;
            public Drawable hideIcon;
            private ColorDrawable uninstallBg;
            private ColorDrawable hideBg;
            private boolean initiated = false;

            private void init() {
                uninstallBg = new ColorDrawable(
                        ResourcesCompat.getColor(getResources(), R.color.color_danger, null)
                );
                hideBg = new ColorDrawable(
                        ResourcesCompat.getColor(getResources(), R.color.color_white, null)
                );

                uninstallIcon = ContextCompat.getDrawable(
                        RawLauncher.this, R.drawable.ic_delete_sweep_black_36dp
                );
                uninstallIcon.setColorFilter(
                        ResourcesCompat.getColor(getResources(), R.color.color_danger, null),
                        PorterDuff.Mode.SRC_ATOP
                );

                hideIcon = ContextCompat.getDrawable(
                        RawLauncher.this, R.drawable.ic_eye_off_black_36dp
                );
                hideIcon.setColorFilter(
                        ResourcesCompat.getColor(getResources(), R.color.color_warning, null),
                        PorterDuff.Mode.SRC_ATOP
                );

                margin = (int) RawLauncher.this.getResources().getDimension(R.dimen.spacing_normal);

                initiated = true;
            }


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int position = viewHolder.getAdapterPosition();
                Item item = suggestionManager.getItem(position);
                View itemView = viewHolder.itemView;
                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                if (dX > 0) {
                    // draw red uninstallBg
                    hideBg.setBounds(
                            0, itemView.getTop(),
                            (int) dX, itemView.getBottom()
                    );
                    //hideBg.draw(c);
                    int itemHeight = itemView.getBottom() - itemView.getTop();
                    int intrinsicWidth = hideIcon.getIntrinsicWidth();
                    int intrinsicHeight = hideIcon.getIntrinsicWidth();

                    int start = margin + intrinsicWidth;
                    int end = margin;
                    int top = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                    int bottom = top + intrinsicHeight;
                    hideIcon.setBounds(start, top, end, bottom);

                    if (dX > start) {
                        hideIcon.draw(c);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                } else if (dX < 0 && item.canBeUninstalled()) {
                    // draw red uninstallBg
                    uninstallBg.setBounds(
                            itemView.getRight() + (int) dX, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom()
                    );
                    //uninstallBg.draw(c);

                    // draw x mark
                    int itemHeight = itemView.getBottom() - itemView.getTop();
                    int intrinsicWidth = uninstallIcon.getIntrinsicWidth();
                    int intrinsicHeight = uninstallIcon.getIntrinsicWidth();

                    int start = itemView.getRight() - margin - intrinsicWidth;
                    int end = itemView.getRight() - margin;
                    int top = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                    int bottom = top + intrinsicHeight;
                    uninstallIcon.setBounds(start, top, end, bottom);

                    uninstallIcon.draw(c);
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Suggestion item = suggestionManager.getSuggestion(position);
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        if (item.getItem().canBeUninstalled()) {
                            suggestionManager.uninstallApp(item);
                        }
                        break;
                    case ItemTouchHelper.RIGHT:
                        suggestionManager.hideItem(item);
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RawLauncher.this);
                        pref.edit().putBoolean(getString(R.string.hide_shared_pref, item.getDiscriminator()), true).apply();
                        break;
                    default:
                        break;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(suggestionRecyclerView);
        ShortcutLayout shortcutLayout = (ShortcutLayout) findViewById(R.id.shortcuts);
        shortcutManager = new ShortcutManager(this, (AppManager) managers.get(ManagerUtils.MANAGER_APPS), shortcutLayout);

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

        suggestionRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (suggestionManager.getItemCount() > 0) {
                    suggestionRecyclerView.scrollToPosition(0);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
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
        if (item.canBeAShortcut()) {
            SharedPreferences pref =
                    PreferenceManager.getDefaultSharedPreferences(this);
            String label = item.getDiscriminator();
            long time;
            long count = pref.getLong(getString(R.string.count_shared_pref, label), 0);

            count++;
            time = new Date().getTime();

            pref.edit()
                    .putLong(getString(R.string.time_shared_pref, label), time)
                    .putLong(getString(R.string.count_shared_pref, label), count)
                    .apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        inputView.showKeyboard(this);
        shortcutManager.update();
        if (inputView.getInput().length() == 0) {
            suggestionManager.clearSuggestions();
        }
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
