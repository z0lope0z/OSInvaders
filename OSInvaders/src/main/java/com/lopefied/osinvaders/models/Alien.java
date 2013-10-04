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
public class Alien extends GameObject {
    private int circleRadius;
    private Paint circlePaint;
    private int height = 180;
    private int width = 180;

    private int explodeTime = 10;
    private Boolean isExploding = Boolean.FALSE;
    private int elapsedExplodeTime = 0;
    private Bitmap animation;


    private long frameTimer;
    private int fps = 10;
    private Rect sRectangle;
    private int frames = 2;
    private int currentFrame = 0;

    private Boolean isGoingLeft = Boolean.FALSE;

    public Alien() {
        this.xPos = 10;
        this.yPos = 200;
        setBoundRect(xPos - width / 2, yPos + height / 2, xPos + width / 2, yPos - height / 2);
        animation = Bitmaps.getBitmap(Bitmaps.ALIEN_GREEN_SHEET);
        sRectangle = new Rect(0, 0, this.width, this.height);
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
        area.drawBitmap(animation, sRectangle, rect, null);
        //sample
//        Rect sRect2 = new Rect(0, 0, 30, 40);
//        Rect rect2 = new Rect(100, 0, 30, 40);
//        area.drawBitmap(animation, sRect2, rect2, null);
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
        frameTimer++;
        if ((frameTimer % fps) == 0) {
            if (currentFrame == frames) {
                currentFrame = 0;
            } else {
                currentFrame++;
            }
            if (!isGoingLeft) {
                // if going right
                sRectangle.top = 0;
                sRectangle.bottom = height;
                sRectangle.left = width * currentFrame;
                sRectangle.right = sRectangle.left + width;
            } else {
                // if going left
                sRectangle.top = height;
                sRectangle.bottom = 2 * height;
                sRectangle.left = width * currentFrame;
                sRectangle.right = sRectangle.left + width;
            }
        }
    }

    @Override
    public void handleWall() {
        setSpeed(getSpeed() * -1);
        if (getSpeed() < 0) {
            isGoingLeft = Boolean.TRUE;
        } else {
            isGoingLeft = Boolean.FALSE;
        }
    }
}
