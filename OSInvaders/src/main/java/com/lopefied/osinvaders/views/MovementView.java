package com.lopefied.osinvaders.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lopefied.osinvaders.models.Alien;

/**
 * Created by lemano on 9/25/13.
 */
public class MovementView extends SurfaceView implements SurfaceHolder.Callback {

    private int xPos;
    private int yPos;
    private float xVel;
    private int yVel;
    private int width;
    private int height;
    private int circleRadius;
    private Paint circlePaint;
    UpdateThread updateThread;

    public MovementView(Context context) {
        super(context);
        getHolder().addCallback(this);
        circleRadius = 10;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        xVel = 2;
        yVel = 2;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
        width = surfaceFrame.width();
        height = surfaceFrame.height();
        xPos = width / 2;
        yPos = height / 2;
        updateThread = new UpdateThread(this);
        updateThread.setRunning(true);
        updateThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        updateThread.setRunning(false);
        while (retry) {
            try {
                updateThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    private Alien alien = new Alien();

    @Override
    protected void onDraw(Canvas canvas) {
        // you can always experiment by removing this line if you wish!
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(xPos, yPos, circleRadius, circlePaint);
        alien.draw(canvas);
    }

    public void updatePhysics() {
        alien.update(1);
        Rect alienRect = alien.getBoundRect();
        if (alien.getXPos() + alienRect.width() / 2 > width)
            alien.handleWall();
        else if (alien.getXPos() - alienRect.width() / 2 < 0-(alienRect.width() / 2))
            alien.handleWall();
        xPos += xVel;
        yPos += yVel;
        if (yPos - circleRadius < 0 || yPos + circleRadius > height) {
            // the ball has hit the top or the bottom of the canvas
            if (yPos - circleRadius < 0) {
                // the ball has hit the top of the canvas
                yPos = circleRadius;
            } else {
                // the ball has hit the bottom of the canvas
                yPos = height - circleRadius;
            }
            // reverse the y direction of the ball
            yVel *= -1;
        }
        if (xPos - circleRadius < 0 || xPos + circleRadius > width) {
            // the ball has hit the sides of the canvas
            if (xPos - circleRadius < 0) {
                // the ball has hit the left of the canvas
                xPos = circleRadius;
            } else {
                // the ball has hit the right of the canvas
                xPos = width - circleRadius;
            }
            // reverse the x direction of the ball
            xVel *= -1;
            xVel *= 1.6;
            yPos += yVel;
        }
    }
}