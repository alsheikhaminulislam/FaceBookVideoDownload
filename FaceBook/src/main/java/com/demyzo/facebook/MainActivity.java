package com.demyzo.facebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    private MainActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity =this;
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                String CopyKey = key;
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    LicenceSuccess(activity.getIntent().getExtras().getString(CopyKey));
                }
            }
        }

    }

    private void LicenceSuccess(String copyKey) {
        FaceBookInitializing.getInstance(MainActivity.this).addEventListener(copyKey, new EventListener() {
            @Override
            public void onStart(String meg) {
                Toast.makeText(MainActivity.this, meg, Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}