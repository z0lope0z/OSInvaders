package com.lopefied.osinvaders.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lopefied.osinvaders.R;

/**
 * Created by lemano on 10/3/13.
 */
public class Bitmaps {
    public static final int ALIEN1 = 0;
    public static final int ALIEN2 = 1;
    public static final int HERO = 2;
    public static final int BULLET = 3;
    public static final int ALIEN_SHEET = 4;
    public static final int ALIEN_GREEN_SHEET = 5;

    private Bitmaps() {
    }

    private Context context;
    private static Bitmaps instance = null;

    public static void createInstance(Context context) {
        instance = new Bitmaps();
        instance.context = context;
    }

    private static Bitmaps getInstance() {
        return instance;
    }

    public static Bitmap getBitmap(int id) {

        Bitmap bmp;
        int rId = 0;
        switch (id) {
            case ALIEN1: {
                rId = R.drawable.bigbrain1;
            }
            break;
            case ALIEN2: {
                rId = R.drawable.ic_launcher;
            }
            break;
            case HERO: {
                rId = R.drawable.titan;
            }
            break;
            case BULLET: {
                rId = R.drawable.titan;
            }
            case ALIEN_SHEET: {
                rId = R.drawable.alien_sheet_40x30;
            }
            break;
            case ALIEN_GREEN_SHEET: {
                rId = R.drawable.greenalien_sheet_180x180;
            }
            break;
        }
        bmp = BitmapFactory.decodeResource(getInstance().context.getResources(), rId);
        return bmp;
    }
}
