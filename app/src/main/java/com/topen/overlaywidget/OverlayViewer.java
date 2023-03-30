package com.topen.overlaywidget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatImageView;

public class OverlayViewer implements View.OnTouchListener{
    private Context appContext;
    private WindowManager wm;

    private View topLeftView;
    private View overlayedView;

    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;

    private static int screen_width;
    private static int screen_height;

    public OverlayViewer(Context context) {
        appContext = context;
        screen_width = context.getResources().getDisplayMetrics().widthPixels;
        screen_height = context.getResources().getDisplayMetrics().heightPixels / 2;

        wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        overlayedView = inflater.inflate(R.layout.layout_test, null);

        overlayedView.setOnTouchListener(this);

        AppCompatImageView btnCaption = overlayedView.findViewById(R.id.btn_caption);
        View btnExtends = overlayedView.findViewById(R.id.btn_extends);
        AppCompatImageView btnRecord = overlayedView.findViewById(R.id.btn_record);
        AppCompatImageView btnSetting = overlayedView.findViewById(R.id.btn_setting);
        AppCompatImageView btnClose = overlayedView.findViewById(R.id.btn_close);


        btnCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCaption.setVisibility(View.GONE);
                btnExtends.setVisibility(View.VISIBLE);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCaption.setVisibility(View.VISIBLE);
                btnExtends.setVisibility(View.GONE);
            }
        });

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = screen_width;
        params.y = screen_height;
        overlayedView.setBackgroundResource(R.drawable.fab_back_right_rounded);
        wm.addView(overlayedView, params);

        topLeftView = new View(context);
        WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);
    }

    public void destroy() {
        if (overlayedView != null) {
            wm.removeView(overlayedView);
            wm.removeView(topLeftView);
            overlayedView = null;
            topLeftView = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getRawX();
            float y = event.getRawY();

            moving = false;

            int[] location = new int[2];
            overlayedView.getLocationOnScreen(location);

            originalXPos = location[0];
            originalYPos = location[1];

            offsetX = originalXPos - x;
            offsetY = originalYPos - y;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int[] topLeftLocationOnScreen = new int[2];
            topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

            System.out.println("topLeftY=" + topLeftLocationOnScreen[1]);
            System.out.println("originalY=" + originalYPos);

            float x = event.getRawX();
            float y = event.getRawY();

            WindowManager.LayoutParams params = (WindowManager.LayoutParams) overlayedView.getLayoutParams();

            int newX = (int) (offsetX + x);
            int newY = (int) (offsetY + y);

            if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                return false;
            }

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);
            if ((screen_width - (params.x + 50)) < 50) {
                overlayedView.setBackgroundResource(R.drawable.fab_back_right_rounded);
                params.x = screen_width;
            } else if (params.x < 50) {
                overlayedView.setBackgroundResource(R.drawable.fab_back_left_rounded);
                params.x = 0;
            } else {
                overlayedView.setBackgroundResource(R.drawable.fab_back);
            }
            wm.updateViewLayout(overlayedView, params);
            moving = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (moving) {
                return true;
            }
        }

        return false;
    }
}
