package com.lapapa.app.new_permit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lapapa.app.get_permit.GetPermitActivity;
import com.lapapa.app.R;

public class NewPermitMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_permit_menu);
    }

    public void proceedNewData(View view){
    }

    public void proceedOldData(View view){
        Intent proceed = new Intent(NewPermitMenuActivity.this, GetPermitActivity.class);
        startActivity(proceed);
    }
}