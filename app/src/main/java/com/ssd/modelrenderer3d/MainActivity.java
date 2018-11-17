package com.ssd.modelrenderer3d;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start menu
        MainActivity.this.startActivity(new Intent(this, MenuActivity.class));
        MainActivity.this.finish();
    }
}
