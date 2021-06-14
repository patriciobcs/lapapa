package com.lapapa.app.firstOpen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lapapa.app.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StartFragment.newInstance(0))
                    .commitNow();
        }
    }
}