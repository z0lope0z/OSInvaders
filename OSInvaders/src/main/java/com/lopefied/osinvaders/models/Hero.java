package com.lopefied.osinvaders.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.lopefied.osinvaders.views.Bitmaps;

/**
 * Created by lemano on 10/1/13.
 */
public class Hero extends GameObject {
    private int circleRadius;
    private Paint circlePaint;
    private int height = 80;
    private int width = 40;

    private Bitmap animation;
    public Hero(int height) {
        this.xPos = 10;
        this.yPos = height - 20;
        setBoundRect(xPos - width / 2, yPos - this.height - 10, xPos + width / 2, yPos + this.height / 2);
        this.animation = Bitmaps.getBitmap(Bitmaps.HERO);
    }

    @Override
    public void setPos(int x, int y) {
        this.xPos = x;
        //this.yPos = y;
        setBoundRect(xPos - this.width / 2, yPos - this.height  - 10, xPos + this.width / 2, yPos + this.height/2);
    }

    @Override
    public void draw(Canvas area) {
        circleRadius = 10;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        //area.drawBitmap(this.animation, null, getBoundRect(), null);
        //area.drawCircle(xPos, yPos, circleRadius, circlePaint);
        Paint boundPaint = new Paint();
        boundPaint.setColor(Color.RED);
        Rect rect = new Rect(getXPos(), getYHead(), getXPos() + width, getYHead() + height);
        area.drawBitmap(animation, null, rect, null);
        //area.drawRect(getBoundRect(), boundPaint);
        //area.drawCircle(xPos, yPos, circleRadius, circlePaint);
    }

    public int getGunOffset() {
        return height + 5;
    }

    @Override
    public void update(long timeDelta) {
    }

    @Override
    public void handleWall() {
        // no need
    }
}
