package com.topen.overlaywidget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class OverlayShowingService extends Service {
    private OverlayViewer overlayViewer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        overlayViewer = new OverlayViewer(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        overlayViewer.destroy();
    }
}
