package com.lopefied.osinvaders;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lopefied.osinvaders.views.MovementView;

public class Movement extends Activity implements MovementView.GameFinishedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        MovementView movementView = new MovementView(this, this);
        setContentView(movementView);
        movementView.setOnTouchListener(movementView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.movement, menu);
        return true;
    }

    String title;
    String message;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void gameFinished(boolean isWinner) {
        title = "Congratulations! You win! :D";
        if (!isWinner) {
            title = "Sorry! You lose! :(";
        }
        message = " restarting game..";
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), title + message, Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(Toast.LENGTH_SHORT);
                    recreate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        );
    }
}
