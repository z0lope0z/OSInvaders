package com.lopefied.osinvaders.views;

import android.content.Context;
import android.graphics.Bitmap;
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
    private Bitmap background;
    private Bitmap parallax;
    private int parallaxHeight;
    private Rect rectParallax;
    private Rect rectCanvas;

    public MovementView(Context context) {
        super(context);
        Bitmaps.createInstance(context);
        getHolder().addCallback(this);
        circleRadius = 10;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        xVel = 2;
        yVel = 2;
        alien = new Alien();
        background = Bitmaps.getBitmap(Bitmaps.FARBACK);
        parallax = Bitmaps.getBitmap(Bitmaps.STARFIELD);
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
        rectParallax = new Rect(0, parallax.getHeight() - height, width, parallax.getHeight());
        rectCanvas = new Rect(0, 0, width, height);
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

    private Alien alien;
    private Hero hero;
    private int maxBullets = 5;
    private int bulletCounter = 0;
    private Bullet[] bullets = new Bullet[maxBullets];

    @Override
    protected void onDraw(Canvas canvas) {
        // you can always experiment by removing this line if you wish!
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(xPos, yPos, circleRadius, circlePaint);
        canvas.drawBitmap(background, 0, 0, null);
        if ((parallax != null) && (rectCanvas != null))
            canvas.drawBitmap(parallax, rectParallax, rectCanvas, null);
        alien.draw(canvas);
        if (hero == null) {
            hero = new Hero(canvas.getHeight());
        }
        hero.draw(canvas);
        for (Bullet bullet : bullets) {
            if (bullet != null)
                bullet.draw(canvas);
        }
    }

    private void launchBullet() {
        if (bulletCounter < maxBullets) {
            Bullet bullet = new Bullet(this.hero.getXPos(), height, hero.getGunOffset());
            bullets[bulletCounter] = bullet;
            bulletCounter++;
        }
    }

    private int parallaxCounter = 0;

    public void updatePhysics() {
        synchronized (this) {
            int index = 0;
            for (Bullet bullet : bullets) {
                if (bullet != null) {
                    bullet.update(1);
                    Boolean isCollided = alien.isCollided(bullet, true);
                    if (isCollided)
                        alien.explode();
                    if ((bullet.getYPos() < -(bullet.getBoundRect().height() / 2)) || isCollided) {
                        bullets[index] = null;
                        bulletCounter--;
                    }
                }
                index++;
            }
            hero.update(1);
            alien.update(1);
            Rect alienRect = alien.getBoundRect();
            if (alien.getXPos() + alienRect.width() / 2 > width)
                alien.handleWall();
            else if (alien.getXPos() - alienRect.width() / 2 < 0 - (alienRect.width() / 2))
                alien.handleWall();

            if (parallaxCounter % 15 == 0) {
                if ((rectParallax != null) && (rectCanvas != null)) {
                    if (rectParallax.top > 0) {
                        rectParallax.top -= 1;
                        rectParallax.bottom -= 1;
                    } else {
                        rectParallax.bottom = parallax.getHeight();
                        rectParallax.top = parallax.getHeight() - rectCanvas.height();
                    }
                }
            }
            parallaxCounter++;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                hero.setPos(Math.round(motionEvent.getX()), height);
                launchBullet();
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                hero.setPos(Math.round(motionEvent.getX()), height);
            }
            break;
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