package com.lopefied.osinvaders.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by lemano on 10/1/13.
 */
public class Bullet extends GameObject {
    private int circleRadius;
    private Paint circlePaint;
    private int height = 20;
    private int width = 20;

    public Bullet(int xPos, int height) {
        this.xPos = xPos;
        this.yPos = height - 20;
        setSpeed(20);
        setBoundRect(xPos - width / 2, yPos + this.height / 2, xPos + width / 2, yPos - this.height / 2);
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
        circlePaint.setColor(Color.BLUE);
        Paint boundPaint = new Paint();
        boundPaint.setColor(Color.RED);
        area.drawRect(getBoundRect(), boundPaint);
        area.drawCircle(xPos, yPos, circleRadius, circlePaint);
    }

    @Override
    public void update(long timeDelta) {
        this.yPos = this.yPos - getSpeed();
        setBoundRect(xPos - width / 2, yPos + this.height / 2, xPos + width / 2, yPos - this.height / 2);
    }

    @Override
    public void handleWall() {
        // no need
    }
}
