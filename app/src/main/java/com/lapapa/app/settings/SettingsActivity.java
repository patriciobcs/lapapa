package com.lapapa.app.settings;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.lapapa.app.R;

import org.jetbrains.annotations.NotNull;

public class SettingsActivity extends AppCompatActivity {

    public static final int MODIFIER = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            SettingsFragment sf = new SettingsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, sf)
                    .commit();

            //findViews(sf.getView().getRootView());
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            findViews(getListView());
            return view;
        }

        public void findViews(View v) {
            try {
                if (v instanceof ViewGroup) {
                    ViewGroup vg = (ViewGroup) v;
                    for (int i = 0; i < vg.getChildCount(); i++) {
                        View child = vg.getChildAt(i);
                        // recursively call this method
                        findViews(child);
                    }
                } else if (v instanceof TextView) {
                    //do whatever you want ...
                    String textSize = getContext().getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).getString("text_size", "0");
                    int textModifier = Integer.parseInt(textSize)*MODIFIER;
                    TextView tv = ((TextView)v);
                    if(tv.getText().equals("LaPaPa")){
                        return;
                    }
                    float sp = tv.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
                    Log.d("findViewsSettings", "Found TV with text "+ tv.getText() + " and text size " + sp);
                    Log.d("findViewsSettings", "Adding up "+ textModifier);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,sp + textModifier);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}