package com.topen.overlaywidget;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissions = {Manifest.permission.SYSTEM_ALERT_WINDOW};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1001);
        }
        checkPermissionOverlay();

        Intent svc = new Intent(this, OverlayShowingService.class);
        startService(svc);
//        finish();
    }

    public void checkPermissionOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(MainActivity.this, "Need permission to overlay", Toast.LENGTH_LONG).show();
                Intent intentSettings = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(intentSettings, 1001);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TextView textView = findViewById(R.id.logtxt);
        textView.setText(permissions[0] + grantResults[0]);
    }
}