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

    private int damageTime = 10;
    private Boolean isDamaged = Boolean.FALSE;
    private int elapsedDamageTime = 0;
    private Bitmap animation;


    private long frameTimer;
    private int fps = 10;
    private Rect sRectangle;
    private int frames = 2;
    private int currentFrame = 0;

    private Boolean isGoingLeft = Boolean.FALSE;

    private EnemyListener enemyListener;
    private int bulletInterval = 10;

    private int life = 2;
    private Boolean isExploding = Boolean.FALSE;
    private int explodeWidth = 128;
    private int explodeHeight = 128;
    private Bitmap explodeAnimation;
    private Rect explodeSRectangle;
    private int explodeFrames = 33;
    private int explodeCurrentFrame = 0;

    public Alien(EnemyListener enemyListener) {
        this.enemyListener = enemyListener;
        this.xPos = 10;
        this.yPos = 200;
        setBoundRect(xPos - width / 2, yPos + height / 2, xPos + width / 2, yPos - height / 2);
        animation = Bitmaps.getBitmap(Bitmaps.ALIEN_GREEN_SHEET);
        explodeAnimation = Bitmaps.getBitmap(Bitmaps.EXPLODE_ALIEN);
        sRectangle = new Rect(0, 0, this.width, this.height);
        explodeSRectangle = new Rect(0, 0, this.explodeWidth, this.explodeHeight);
    }

    public void explode() {
        this.isExploding = Boolean.TRUE;
    }

    public void damage() {
        this.isDamaged = Boolean.TRUE;
        life--;
    }

    public boolean isKilled() {
        return life <= 0;
    }

    public void reset() {
        this.isDamaged = Boolean.FALSE;
        this.elapsedDamageTime = 0;
    }

    @Override
    public void setPos(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public void draw(Canvas area) {
        if (isExploding){
            Rect rect = new Rect(getXLeftWing(), getYHead(), getXLeftWing() + width, getYHead() + height);
            area.drawBitmap(explodeAnimation, explodeSRectangle, rect, null);
            return;
        }
        circleRadius = 50;
        if (isDamaged) {
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
        if (isExploding){
            // end game
            if (explodeCurrentFrame > explodeFrames){
                enemyListener.explosionComplete(this);
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

        this.xPos = this.xPos + getSpeed();
//        setBoundRect(xPos - width / 2, yPos + height / 2, xPos + width / 2, yPos - height / 2);
        setBoundRect(getXLeftWing(), getYHead(), getXLeftWing() + width, getYHead() + height);
        //setBoundRect(xPos - (this.width / 2), yPos - (this.height / 2), xPos + width / 2, yPos - this.height / 2);
        elapsedDamageTime++;
        if (elapsedDamageTime > 10)
            reset();
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

        if (frameTimer == 0) {
            enemyListener.launchBullet(this);
        } else if ((frameTimer % bulletInterval == 0) && (frameTimer >= bulletInterval)) {
            enemyListener.launchBullet(this);
        }
        frameTimer++;
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
