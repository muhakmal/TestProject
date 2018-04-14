package com.baskom.testproject;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

public class MainActivity extends AppCompatActivity {
    int flag = 0;
    private static final String TEXT_KEY = "text";
    private JellyToolbar toolbar;
    private AppCompatEditText editText;
    private JellyListener jellyListener = new JellyListener() {
        @Override
        public void onToolbarExpandingStarted() {
            super.onToolbarExpandingStarted();
            toolbar.setContentView(editText);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            flag = 1;
        }

        @Override
        public void onToolbarCollapsed() {
            super.onToolbarCollapsed();

        }

        @Override
        public void onCancelIconClicked() {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (TextUtils.isEmpty(editText.getText())) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                toolbar.collapse();
                flag = 0;
            } else {
                editText.getText().clear();
            }imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ;

        toolbar = (JellyToolbar) findViewById(R.id.toolbar);
        toolbar.setJellyListener(jellyListener);
        toolbar.getToolbar().setPadding(0, getStatusBarHeight(), 0, 0);

        editText = (AppCompatEditText) LayoutInflater.from(this).inflate(R.layout.edit_text, null);
        editText.setBackgroundResource(R.color.colorTransparent);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Toast.makeText(MainActivity.this,
                            "jelly's said " + editText.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TEXT_KEY, editText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editText.setText(savedInstanceState.getString(TEXT_KEY));
        editText.setSelection(editText.getText().length());
    }

    @Override
    public void onBackPressed() {
        if (flag == 0) {
            super.onBackPressed();
        } else if (flag == 1)
            jellyListener.onCancelIconClicked();
    }
}
