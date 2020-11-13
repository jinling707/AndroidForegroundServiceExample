package com.example.androidforegroundserviceexample;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startService(View view) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Start Foreground Service in Android");
        //方法一
        serviceIntent.setAction("STARTFOREGROUND_ACTION");
        startService(serviceIntent);
        //方法二
        // startService(serviceIntent);
    }

    public void stopService(View view) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Stop Foreground Service in Android");
        //方法一
        serviceIntent.setAction("STOPFOREGROUND_ACTION");
        startService(serviceIntent);
        //方法二
        //stopService(serviceIntent);
    }
}