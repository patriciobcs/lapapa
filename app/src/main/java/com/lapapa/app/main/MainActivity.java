package com.lapapa.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lapapa.app.R;
import com.lapapa.app.firstOpen.StartActivity;
import com.lapapa.app.new_permit.NewPermitMenuActivity;
import com.lapapa.app.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        final Toolbar toolbar = findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        Boolean isFirstStart = getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).getBoolean("isFirstStart", true);
        if(isFirstStart){
            Intent goToFirstOpen = new Intent(MainActivity.this, StartActivity.class);
            startActivity(goToFirstOpen);

            getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).edit().putBoolean("isFirstStart", false).commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent gotoSettings = new Intent(this, SettingsActivity.class);
                startActivity(gotoSettings);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
