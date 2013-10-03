package com.lopefied.osinvaders.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.lopefied.osinvaders.models.Alien;
import com.lopefied.osinvaders.models.Bullet;
import com.lopefied.osinvaders.models.Hero;

import java.util.Iterator;

/**
 * Created by lemano on 9/25/13.
 */
public class MovementView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

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
        hero = new Hero(height);
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
    private Hero hero = new Hero(height);
    private int maxBullets = 5;
    private int bulletCounter = 0;
    private Bullet[] bullets = new Bullet[maxBullets];

    @Override
    protected void onDraw(Canvas canvas) {
        // you can always experiment by removing this line if you wish!
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(xPos, yPos, circleRadius, circlePaint);
        alien.draw(canvas);
        hero.draw(canvas);
        for (Bullet bullet : bullets){
            if (bullet != null)
                bullet.draw(canvas);
        }
    }

    private void launchBullet(){
        if (bulletCounter < maxBullets) {
            Bullet bullet = new Bullet(this.hero.getXPos(), height);
            bullets[bulletCounter] = bullet;
            bulletCounter++;
        }
    }

    public void updatePhysics() {
        synchronized (this) {
            alien.update(1);
            Rect alienRect = alien.getBoundRect();
            if (alien.getXPos() + alienRect.width() / 2 > width)
                alien.handleWall();
            else if (alien.getXPos() - alienRect.width() / 2 < 0-(alienRect.width() / 2))
                alien.handleWall();

            int index = 0;
            for (Bullet bullet : bullets){
                if (bullet != null){
                    bullet.update(1);
                    Boolean isCollided = alien.isCollided(bullet, true);
                    if (isCollided)
                        alien.explode();
                    if ((bullet.getYPos() < -(bullet.getBoundRect().height()/2)) || isCollided){
                        bullets[index] = null;
                        bulletCounter--;
                    }
                }
                index++;
            }
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                hero.setPos(Math.round(motionEvent.getX()), height);
                launchBullet();
            } break;
            case MotionEvent.ACTION_MOVE: {
                hero.setPos(Math.round(motionEvent.getX()), height);
            } break;
        }
        return true;
    }

//    @Override
//    public boolean onTouch(View arg0, MotionEvent arg1) {
//
//        switch (arg1.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                if (gameOver != NOT_YET) {
//                    reset();
//                } else if (!hero.isKilled()){
//
//                    int x = Math.round(arg1.getX());
//                    int y = Math.round(arg1.getY());
//
//                    boolean intersects = false;
//
//                    Rect longRect = new Rect(hero.getBoundRect());
//
//                    int width = longRect.width();
//
//                    if (x > longRect.right) {
//                        longRect.right = x + width/2;
//                    } else if (x < longRect.left){
//                        longRect.left = x - width/2;
//                    }
//
//                    //Let's calculate a collision
//                    for (int i = 0; i < maxEnemyBullets; ++i) {
//                        if (enemyBullets[i].isVisible()) {
//                            if (Rect.intersects(longRect, enemyBullets[i].getBoundRect()) ) {
//                                enemyBullets[i].setVisible(false);
//                                hero.setPos(enemyBullets[i].getBoundRect().left, enemyBullets[i].getBoundRect().top);
//                                hero.addDamage(enemyBullets[i].getPower());
//
//                                intersects = true;
//                                break;
//                            }
//                        }
//                    }
//
//                    if (!intersects) {
//                        hero.setPos(x,y);
//                        fireBullet();
//                    }
//                }
//            }break;
//
//            case MotionEvent.ACTION_MOVE: {
//                if (!hero.isKilled()){
//                    hero.setPos(Math.round(arg1.getX()), Math.round(arg1.getY()));
//                }
//            }break;
//
//            case MotionEvent.ACTION_UP: {
//            }
//            case MotionEvent.ACTION_CANCEL: {
//            }break;
//
//            default:
//                return false;
//        }
//
//        return true;
//    }
}