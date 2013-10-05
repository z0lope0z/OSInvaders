package com.lopefied.osinvaders.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.lopefied.osinvaders.models.Alien;
import com.lopefied.osinvaders.models.Bullet;
import com.lopefied.osinvaders.models.EnemyBullet;
import com.lopefied.osinvaders.models.EnemyListener;
import com.lopefied.osinvaders.models.Hero;
import com.lopefied.osinvaders.models.HeroListener;

/**
 * Created by lemano on 9/25/13.
 */
public class MovementView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private GameFinishedListener gameFinishedListener;
    private Context mContext;
    private int xPos;
    private int yPos;
    private float xVel;
    private int yVel;
    private int width;
    private int height;
    private int circleRadius;
    private Paint circlePaint;
    private UpdateThread updateThread;
    private Bitmap background;
    private Bitmap parallax;
    private int parallaxHeight;
    private Rect rectParallax;
    private Rect rectCanvas;

    public MovementView(Context context, GameFinishedListener gameFinishedListener) {
        super(context);
        Bitmaps.createInstance(context);
        getHolder().addCallback(this);
        circleRadius = 10;
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        xVel = 2;
        yVel = 2;
        alien = new Alien(enemyListener);
        background = Bitmaps.getBitmap(Bitmaps.FARBACK);
        parallax = Bitmaps.getBitmap(Bitmaps.STARFIELD);
        this.gameFinishedListener = gameFinishedListener;
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
        hero = new Hero(height, heroListener);
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
    private int maxEnemyBullets = 5;
    private Bullet[] bullets = new Bullet[maxBullets];
    private int bulletCounter = 0;
    private EnemyBullet[] enemyBullets = new EnemyBullet[maxEnemyBullets];
    private int enemyBulletCounter = 0;
    private EnemyListener enemyListener = new EnemyListener() {
        @Override
        public void launchBullet(Alien alien) {
            if (enemyBulletCounter < maxBullets) {
                EnemyBullet enemyBullet = new EnemyBullet(alien);
                enemyBullets[enemyBulletCounter] = enemyBullet;
                enemyBulletCounter++;
            }
        }

        @Override
        public void explosionComplete(Alien alienBoss) {
            endGame(Boolean.TRUE);
        }
    };
    private HeroListener heroListener = new HeroListener() {
        @Override
        public void explosionComplete(Hero hero) {
            endGame(Boolean.FALSE);
        }
    };
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
            hero = new Hero(canvas.getHeight(), heroListener);
        }
        hero.draw(canvas);
        for (Bullet bullet : bullets) {
            if (bullet != null)
                bullet.draw(canvas);
        }
        for (EnemyBullet enemyBullet : enemyBullets) {
            if (enemyBullet != null)
                enemyBullet.draw(canvas);
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

    private void endGame(Boolean isWinner){
        gameFinishedListener.gameFinished(isWinner);
    }

    public void updatePhysics() {
        synchronized (this) {
            int index = 0;
            for (Bullet bullet : bullets) {
                bulletCounter = maxBullets;
                if (bullet != null) {
                    bullet.update(1);
                    Boolean isCollided = alien.isCollided(bullet, true);
                    if (isCollided) {
                        alien.damage();
                        if (alien.isKilled()){
                            alien.explode();
                        }
                    }
                    if ((bullet.getYPos() < -(bullet.getBoundRect().height() / 2)) || isCollided) {
                        bullets[index] = null;
                    }
                } else {
                    bulletCounter--;
                }
                index++;
            }
            index = 0;
            for (EnemyBullet enemyBullet : enemyBullets) {
                if (enemyBullet != null) {
                    enemyBullet.update(1);
                    Boolean isCollided = hero.isCollided(enemyBullet, false);
                    if (isCollided) {
                        hero.damage();
                        if (hero.isKilled()){
                            hero.explode();
                        }
                    }
                    if (enemyBullet.getYPos() > rectCanvas.height() || isCollided) {
                        enemyBullets[index] = null;
                        enemyBulletCounter--;
                    }
                }
                index++;
            }
            if (hero != null)
                hero.update(1);
            alien.update(1);
            Rect alienRect = alien.getBoundRect();
            if (alien.getXPos() + alienRect.width() / 2 > width)
                alien.handleWall();
            else if (alien.getXPos() - alienRect.width() / 2 < 0 - (alienRect.width() / 2))
                alien.handleWall();

            if (parallaxCounter % 7 == 0) {
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

    public static interface GameFinishedListener {
        public void gameFinished(boolean isWinner);
    }
}
