package com.sjcqs.rawlauncher;

import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.sjcqs.rawlauncher.views.ShortcutLayout;
import com.sjcqs.rawlauncher.views.UserInputView;

/**
 * The main raw context
 *
 * IDEAS: ouvrir quand application seule, trier application par utilisation, raccourcis
 */
public class RawLauncher extends AppCompatActivity {
    private static final String TAG = RawLauncher.class.getName();

    private UserInputView inputView;
    private MainManager mainManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View rootView = getLayoutInflater().inflate(R.layout.activity_raw_launcher, null);
        setContentView(rootView);

        inputView = (UserInputView) findViewById(R.id.user_input_view);
        final RecyclerView suggestionRecyclerView = (RecyclerView) findViewById(R.id.suggestions);
        ShortcutLayout shortcutLayout = (ShortcutLayout) findViewById(R.id.shortcuts);
        mainManager = new MainManager(this, inputView, suggestionRecyclerView, shortcutLayout);

        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    final String str = charSequence.toString();
                    mainManager.suggest(str);
                } else {
                    mainManager.clearSuggestions();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            int margin;
            Drawable uninstallIcon;
            Drawable hideIcon;
            private boolean initiated = false;

            private void init() {
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

                margin = (int) getResources().getDimension(R.dimen.spacing_normal);

                initiated = true;
            }


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int position = viewHolder.getAdapterPosition();

                if (position == -1) {
                    // not interested in those
                    return;
                }

                View itemView = viewHolder.itemView;
                if (!initiated) {
                    init();
                }

                if (dX > 0) {
                    // draw red uninstallBg
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
                } else if (dX < 0 && mainManager.canBeUninstalled(position)) {
                    // draw red uninstallBg
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
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        mainManager.uninstallApp(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        mainManager.hideItem(position);
                        break;
                    default:
                        break;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(suggestionRecyclerView);

        suggestionRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                suggestionRecyclerView.scrollToPosition(0);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        inputView.hideKeyboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inputView.showKeyboard();
        if (inputView.getInput().length() == 0) {
            mainManager.clearSuggestions();
        }
        mainManager.reload();
    }
}
