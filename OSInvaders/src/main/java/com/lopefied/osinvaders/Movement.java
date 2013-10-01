package com.lopefied.osinvaders;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.lopefied.osinvaders.views.MovementView;

public class Movement extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MovementView(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.movement, menu);
        return true;
    }
    
}
