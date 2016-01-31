package org.example.lockscreen;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DigitalClock;



/**
 * Created by rustie on 1/30/16.
 */
public class Clock extends Activity
{
    @Override

    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DigitalClock dc = (DigitalClock) findViewById(R.id.digitalClock);
    }
}
