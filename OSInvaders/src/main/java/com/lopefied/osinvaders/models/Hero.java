package com.lopefied.osinvaders.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by lemano on 10/1/13.
 */
public class Hero extends GameObject {
    private int circleRadius;
    private Paint circlePaint;
    private int height = 10;
    private int width = 10;

    public Hero(int height) {
        this.xPos = 10;
        this.yPos = height - 20;
        setBoundRect(xPos - width / 2, yPos + height / 2, xPos + width / 2, yPos - height / 2);
    }

    @Override
    public void setPos(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public void draw(Canvas area) {
        circleRadius = 10;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        area.drawCircle(xPos, yPos, circleRadius, circlePaint);
    }

    @Override
    public void update(long timeDelta) {
        // no need
    }

    @Override
    public void handleWall() {
        // no need
    }
}
