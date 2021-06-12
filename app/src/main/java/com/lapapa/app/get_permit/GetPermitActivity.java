package com.lapapa.app.get_permit;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.lapapa.app.R;
import com.lapapa.app.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetPermitActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    public String ReadTextFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
        String line = reader.readLine();
        StringBuilder fullText = new StringBuilder();
        while(line != null){
            fullText.append(line);
            fullText.append(" ");
            line = reader.readLine();
        }
        return fullText.toString();
    }

    public String getJS(String filename) {
        try {
            return "javascript:" + ReadTextFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    // Possible Utilization CA
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_D:
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    // Possible Utilization CA
    public void simulateClick(float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[1];
        MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
        pp1.id = 0;
        pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
        properties[0] = pp1;
        MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[1];
        MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
        pc1.x = x;
        pc1.y = y;
        pc1.pressure = 1;
        pc1.size = 1;
        pointerCoords[0] = pc1;
        MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, 1, properties,
                pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
        dispatchTouchEvent(motionEvent);

        motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, 1, properties,
                pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );
        dispatchTouchEvent(motionEvent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}