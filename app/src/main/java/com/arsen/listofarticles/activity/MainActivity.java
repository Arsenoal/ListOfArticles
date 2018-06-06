package com.arsen.listofarticles.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App)getApplication()).getNetComponent().inject(this);
    }
}