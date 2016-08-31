package com.example.shera.thebookalert;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class splashscreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(splashscreen.this,MainActivity.class));
                finish();
            }
        };

        Timer t = new Timer();
        t.schedule(timerTask,5000);
    }
}
