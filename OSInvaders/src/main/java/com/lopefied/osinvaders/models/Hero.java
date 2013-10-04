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
    private int height = 64;
    private int width = 29;

    private Bitmap animation;

    private long frameTimer;
    private int fps = 10;
    private Rect sRectangle;
    private int maxFrames = 3;
    private int currentFrame = 0;

    private int life = 3;
    private int hitTime = 20;
    private int hitCounter = 0;
    private Boolean isHit = Boolean.FALSE;

    public Hero(int height) {
        this.xPos = 10;
        this.yPos = height - 20;
        setBoundRect(xPos - width / 2, yPos - this.height, xPos + width / 2, yPos + this.height / 2);
        sRectangle = new Rect(0, 0, width, this.height);
        this.animation = Bitmaps.getBitmap(Bitmaps.HERO);
    }

    public void damage() {
        life--;
        isHit = Boolean.TRUE;
    }

    public boolean isKilled(){
        return life <= 0;
    }

    private void resetHit(){
        isHit = Boolean.FALSE;
        hitCounter=0;
    }

    @Override
    public void setPos(int x, int y) {
        this.xPos = x;
        setBoundRect(xPos - this.width / 2, yPos - this.height, xPos + this.width / 2, yPos + this.height / 2);
    }

    @Override
    public void draw(Canvas area) {
        circleRadius = 10;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        Paint boundPaint = new Paint();
        boundPaint.setColor(Color.RED);
        if (isHit){
            area.drawRect(getBoundRect(), boundPaint);
        }
        Rect rect = new Rect(getXLeftWing(), getYHead(), getXLeftWing() + width, getYHead() + height);
        area.drawBitmap(animation, sRectangle, rect, null);
    }

    public int getGunOffset() {
        return height + 5;
    }

    @Override
    public void update(long timeDelta) {
        if (frameTimer % fps == 0) {
            sRectangle.left = currentFrame * width;
            sRectangle.right = sRectangle.left + width;
            currentFrame++;
        }
        if (currentFrame > maxFrames) {
            currentFrame = 0;
        }
        frameTimer++;
        //hit
        if (isHit) {
            if (hitCounter < hitTime){
                hitCounter++;
            } else {
                resetHit();
            }
        }
    }

    @Override
    public void handleWall() {
        // no need
    }
}
