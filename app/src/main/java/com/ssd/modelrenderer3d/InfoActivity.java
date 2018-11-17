package com.ssd.modelrenderer3d;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView infoText = findViewById(R.id.info_text);
        infoText.setText(getResources().getString(R.string.info));
    }
}
