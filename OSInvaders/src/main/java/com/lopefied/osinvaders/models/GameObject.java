package com.lopefied.osinvaders.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lemano on 10/1/13.
 */
public abstract class GameObject {
    private int speed = 5;
    private float floatSpeed;
    private Boolean isKilled = Boolean.FALSE;

    public float getFloatSpeed() {
        return floatSpeed;
    }

    public void setFloatSpeed(float floatSpeed) {
        this.floatSpeed = floatSpeed;
    }

    private boolean visible;
    private Rect aabb;

    public abstract void draw(Canvas area);

    public abstract void update(long timeDelta);

    public abstract void handleWall();

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    protected Rect spaceRect = new Rect();

    public void setSpaceArea(int left, int top, int right, int bottom) {
        spaceRect.left = left;
        spaceRect.top = top;
        spaceRect.right = right;
        spaceRect.bottom = bottom;
    }

    public Rect getBoundRect() {
        return aabb;
    }

    public void setBoundRect(int left, int top, int right, int bottom) {
        if (aabb == null) {
            aabb = new Rect(left, top, right, bottom);
        } else {
            aabb.left = left;
            aabb.right = right;
            aabb.top = top;
            aabb.bottom = bottom;
        }
    }

    public void setBoundRect(Rect aabb) {
        if (this.aabb == null) {
            this.aabb = new Rect(aabb);
        } else {
            this.aabb.left = aabb.left;
            this.aabb.right = aabb.right;
            this.aabb.top = aabb.top;
            this.aabb.bottom = aabb.bottom;
        }
    }

    public void drawBounds(Canvas area){
        Paint boundPaint = new Paint();
        boundPaint.setColor(Color.RED);
        area.drawRect(getBoundRect(), boundPaint);
    }

    public void setPos(int x, int y) {
        Rect aabb = getBoundRect();
        int width = aabb.width();
        aabb.left = x - width / 2;
        aabb.right = x + width / 2;
        setBoundRect(aabb);
    }

    protected int xPos;
    protected int yPos;

    public int getXPos(){
        return this.xPos;
    }

    public int getYPos(){
        return this.yPos;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isCollided(GameObject collider, boolean isUpwardsDirection){
        if (isUpwardsDirection){
            return (((collider.getYHead() < this.getYTail()) && ((collider.getXLeftWing() < this.getXRightWing() && collider.getXRightWing() > this.getXRightWing()))));
        } else {
            return ((collider.getYHead() > this.getYTail() && (collider.getXLeftWing() < this.getXRightWing() && collider.getXRightWing() > this.getXRightWing())));
        }
    }

    public int getXLeftWing(){
        return this.getXPos() - (this.getBoundRect().width() / 2);
    }

    public int getXRightWing(){
        return this.getXPos() + (this.getBoundRect().width() / 2);
    }

    public int getYTail(){
        return this.getYPos() + (this.getBoundRect().height() / 2);
    }

    public int getYHead(){
        return this.getYPos() - (this.getBoundRect().height() / 2);
    }

    public boolean isKilled(){
        return isKilled;
    }
}
