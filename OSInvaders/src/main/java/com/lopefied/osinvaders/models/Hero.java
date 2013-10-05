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

    private int life = 2;
    private int hitTime = 20;
    private int hitCounter = 0;
    private Boolean isHit = Boolean.FALSE;

    private HeroListener heroListener;
    private Boolean isExploding = Boolean.FALSE;
    private int explodeWidth = 256;
    private int explodeHeight = 256;
    private Bitmap explodeAnimation;
    private Rect explodeSRectangle;
    private int explodeFrames = 47;
    private int explodeCurrentFrame = 0;

    public Hero(int height, HeroListener heroListener) {
        this.heroListener = heroListener;
        this.xPos = 10;
        this.yPos = height - 20;
        setBoundRect(xPos - width / 2, yPos - this.height, xPos + width / 2, yPos + this.height / 2);
        sRectangle = new Rect(0, 0, width, this.height);
        this.animation = Bitmaps.getBitmap(Bitmaps.HERO);
        this.explodeAnimation = Bitmaps.getBitmap(Bitmaps.EXPLODE_SHIP);
        explodeSRectangle = new Rect(0, 0, this.explodeWidth, this.explodeHeight);
    }

    public void explode() {
        this.isExploding = Boolean.TRUE;
    }

    public void damage() {
        life--;
        isHit = Boolean.TRUE;
    }

    public boolean isKilled() {
        return life <= 0;
    }

    private void resetHit() {
        isHit = Boolean.FALSE;
        hitCounter = 0;
    }

    @Override
    public void setPos(int x, int y) {
        this.xPos = x;
        setBoundRect(xPos - this.width / 2, yPos - this.height, xPos + this.width / 2, yPos + this.height / 2);
    }

    @Override
    public void draw(Canvas area) {
        if (isExploding) {
            Rect rect = new Rect(getXPos() - explodeWidth / 2, getYPos() - explodeHeight / 2, getXPos() + explodeWidth / 2 , getYPos() + explodeHeight/2);
            area.drawBitmap(explodeAnimation, explodeSRectangle, rect, null);
            return;
        }
        circleRadius = 10;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        Paint boundPaint = new Paint();
        boundPaint.setColor(Color.RED);
        if (isHit) {
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
        if (isExploding) {
            // end game
            if (explodeCurrentFrame > explodeFrames) {
                heroListener.explosionComplete(this);
            } else {
                if ((frameTimer % 4) == 0) {
                    explodeSRectangle.left = explodeCurrentFrame * explodeWidth;
                    explodeSRectangle.top = 0;
                    explodeSRectangle.right = explodeSRectangle.left + explodeWidth;
                    explodeSRectangle.bottom = explodeHeight;
                    explodeCurrentFrame++;
                }
            }
            frameTimer++;
            return;
        }

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
            if (hitCounter < hitTime) {
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
