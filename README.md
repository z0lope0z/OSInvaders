OSInvaders
==========

Synchronization is in 

```
OSInvaders/src/main/java/com/lopefied/osinvaders/views/MovementView.java
```

```java
// snippet
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
}
```
