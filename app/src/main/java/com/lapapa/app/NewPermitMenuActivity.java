package com.lapapa.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewPermitMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_permit_menu);
    }

    public void proceedNewData(View view){
    }

    public void proceedOldData(View view){
        Intent proceed = new Intent(NewPermitMenuActivity.this, MainActivity.class);
        startActivity(proceed);
    }
}