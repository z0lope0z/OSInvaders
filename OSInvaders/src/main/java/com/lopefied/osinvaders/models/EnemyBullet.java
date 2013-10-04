package com.lopefied.osinvaders.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by lemano on 10/1/13.
 */
public class EnemyBullet extends GameObject {
    private int circleRadius;
    private Paint circlePaint;
    private int height = 20;
    private int width = 20;
    private Alien alien;

    public EnemyBullet(Alien alien) {
        this.alien = alien;
        this.xPos = alien.getXPos();
        this.yPos = alien.getYTail();
        setSpeed(10);
        setBoundRect(this.xPos - width / 2, this.yPos - this.height / 2, this.xPos + width / 2, this.yPos + this.height / 2);
    }

    @Override
    public void setPos(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public void draw(Canvas area) {
        circleRadius = width / 2;
        circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
//        Paint boundPaint = new Paint();
//        boundPaint.setColor(Color.RED);
//        area.drawRect(getBoundRect(), boundPaint);
        area.drawCircle(xPos, yPos, circleRadius, circlePaint);
    }

    @Override
    public void update(long timeDelta) {
        this.yPos = this.yPos + getSpeed();
        setBoundRect(this.xPos - width / 2, this.yPos - this.height / 2, this.xPos + width / 2, this.yPos + this.height / 2);
    }

    @Override
    public void handleWall() {
        // no need
    }
}
