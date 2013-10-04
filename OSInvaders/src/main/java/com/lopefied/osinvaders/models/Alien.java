package com.lopefied.osinvaders.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.lopefied.osinvaders.views.Bitmaps;

/**
 * Created by lemano on 10/1/13.
 */
public class Alien extends GameObject {
    private int circleRadius;
    private Paint circlePaint;
    private int height = 150;
    private int width = 150;

    private int explodeTime = 10;
    private Boolean isExploding = Boolean.FALSE;
    private int elapsedExplodeTime = 0;
    private Bitmap animation;
    public Alien() {
        this.xPos = 10;
        this.yPos = 200;
        setBoundRect(xPos - width / 2, yPos + height / 2, xPos + width / 2, yPos - height / 2);
        animation = Bitmaps.getBitmap(Bitmaps.ALIEN1);
    }

    public void explode() {
        this.isExploding = Boolean.TRUE;
    }

    public void reset() {
        this.isExploding = Boolean.FALSE;
        this.elapsedExplodeTime = 0;
    }

    @Override
    public void setPos(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public void draw(Canvas area) {
        circleRadius = 50;
        if (isExploding) {
            circleRadius = 100;
            circlePaint = new Paint();
            circlePaint.setColor(Color.BLUE);
            area.drawCircle(xPos, yPos, circleRadius, circlePaint);
        }
        //area.drawRect(getBoundRect(), boundPaint);

        //Log.d("stuff", String.valueOf(animation));
//        Paint boundPaint = new Paint();
//        boundPaint.setColor(Color.RED);
//        area.drawRect(getBoundRect(), boundPaint);
        //area.drawBitmap(animation, getXLeftWing(), getYHead(), null);
        //Rect rect = new Rect(10, 10, 20, 20);
        Rect rect = new Rect(getXLeftWing(), getYHead(), getXLeftWing() + width, getYHead() + height);
        area.drawBitmap(animation, null, rect, null);
    }

    @Override
    public void update(long timeDelta) {
        this.xPos = this.xPos + getSpeed();
//        setBoundRect(xPos - width / 2, yPos + height / 2, xPos + width / 2, yPos - height / 2);
        setBoundRect(getXLeftWing(), getYHead(), getXLeftWing() + width, getYHead() + height);
        //setBoundRect(xPos - (this.width / 2), yPos - (this.height / 2), xPos + width / 2, yPos - this.height / 2);
        elapsedExplodeTime++;
        if (elapsedExplodeTime > 10)
            reset();
    }

    @Override
    public void handleWall() {
        setSpeed(getSpeed() * -1);
    }
}
