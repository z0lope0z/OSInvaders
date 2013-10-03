package com.lopefied.osinvaders.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by lemano on 10/1/13.
 */
public class Alien extends GameObject {
    private int circleRadius;
    private Paint circlePaint;
    private int height = 20;
    private int width = 20;

    private int explodeTime = 10;
    private Boolean isExploding = Boolean.FALSE;
    private int elapsedExplodeTime = 0;

    public Alien() {
        this.xPos = 10;
        this.yPos = 200;
        setBoundRect(xPos - width / 2, yPos + height / 2, xPos + width / 2, yPos - height / 2);
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
        circleRadius = 10;
        if (isExploding)
            circleRadius = 100;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        area.drawCircle(xPos, yPos, circleRadius, circlePaint);
        Paint boundPaint = new Paint();
        boundPaint.setColor(Color.RED);
        area.drawRect(getBoundRect(), boundPaint);
        area.drawCircle(xPos, yPos, circleRadius, circlePaint);
    }

    @Override
    public void update(long timeDelta) {
        this.xPos = this.xPos + getSpeed();
        setBoundRect(xPos - width / 2, yPos + this.height / 2, xPos + width / 2, yPos - this.height / 2);
        elapsedExplodeTime++;
        if (elapsedExplodeTime > 10)
            reset();
    }

    @Override
    public void handleWall() {
        setSpeed(getSpeed() * -1);
    }
}
