package com.lopefied.osinvaders.models;

/**
 * Created by lemano on 10/4/13.
 */
public interface EnemyListener {
    public void launchBullet(Alien alien);
    public void explosionComplete(Alien alienBoss);
}
