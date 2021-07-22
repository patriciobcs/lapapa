package com.lapapa.app.new_permit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lapapa.app.get_permit.GetPermitActivity;
import com.lapapa.app.R;

public class NewPermitMenuActivity extends AppCompatActivity {
    public static final int MODIFIER = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_permit_menu);

        View rootView = findViewById(android.R.id.content).getRootView();
        findViews(rootView);

        SharedPreferences sharedPref = getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE);
        if(!sharedPref.getBoolean("has_data", false)){
            Log.d("NewPermit", "No data Found");
            proceedNewData(findViewById(R.id.same_data));
        }
        TextView nombre = findViewById(R.id.same_data_name);
        nombre.setText(sharedPref.getString("name", "No Data Found"));
    }

    public void proceedNewData(View view){
    }

    public void proceedOldData(View view){
        Intent proceed = new Intent(NewPermitMenuActivity.this, GetPermitActivity.class);
        startActivity(proceed);
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
                String textSize = getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).getString("text_size", "0");
                int textModifier = Integer.parseInt(textSize)*MODIFIER;
                TextView tv = ((TextView)v);
                if(tv.getText().equals("LaPaPa")){
                    return;
                }
                float sp = tv.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
                Log.d("findViews", "Found TV with text "+ tv.getText() + " and text size " + sp);
                Log.d("findViews", "Adding up "+ textModifier);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,sp + textModifier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}