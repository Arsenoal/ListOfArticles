package com.arsen.listofarticles.util.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public final class ScreenHelper {
    private ScreenHelper(){}

    public static final DisplayMetrics METRICS = Resources.getSystem().getDisplayMetrics();

    public static void displayMessage(Context context, String message){
        Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.FILL_HORIZONTAL| Gravity.TOP, 0, 0);
        t.show();
    }

    public static void hideKeyboard(AppCompatActivity appCompatActivity){
        View view = appCompatActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static int convertPixelsToDp(float px){
        float dp = px / (METRICS.densityDpi / 160f);
        return Math.round(dp);
    }

    public static int convertDpToPixel(float dp){
        float px = dp * (METRICS.densityDpi / 160f);
        return Math.round(px);
    }

    public static Point getScreen(AppCompatActivity appCompatActivity){
        Display display = appCompatActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        return size;
    }

    public static Pair<Integer, Integer> getRealScreen(AppCompatActivity appCompatActivity){
        Display display = appCompatActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics realMetrics = new DisplayMetrics();
        display.getRealMetrics(realMetrics);
        int realWidth = realMetrics.widthPixels;
        int realHeight = realMetrics.heightPixels;

        return new Pair<>(realHeight, realHeight);
    }

    public static int getColor(int color, int alpha){
        return (color & 0x00ffffff) | (alpha << 24);
    }
}
