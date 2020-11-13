## ForegroundService

### 簡介
使用前台服務須創造一通知，讓用戶主動意識到您的應用正在前台執行任務，並且正在消耗系統資源。除非服務已停止或從前台刪除，否則無法取消該通知。

前台服務的應用示例包括：
在前台服務中播放音樂的音樂播放器應用。該通知可能會顯示當前正在播放的歌曲。
健身應用程序，在收到用戶的許可後，將其記錄在前台服務中。該通知可能會顯示用戶在當前健身會話期間旅行的距離。

### 說明
#### 權限
Android 9（API級別28）或更高版本，使用必須添加權限
``` java
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```
#### 開啟foreground service
``` java
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("下拉列表中的Title")
                .setContentText("要显示的内容 要显示的内容 要显示的内容 要显示的内容") //過多的字以...呈現
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("要显示的内容 要显示的内容 要显示的内容 要显示的内容")) //顯示完整內容
                .setSmallIcon(R.drawable.ic_cake)// 設置狀態攔下小圖標，建議大小為32x32
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.smile)) // 設置狀態攔下大圖標，建議用PNG，大小為64x64
                .setWhen(System.currentTimeMillis())// 設置通知發生時間
                .addAction(R.drawable.ic_launcher_foreground, "Stop", pStopSelf)
                .addAction(R.drawable.ic_launcher_foreground, "NewActivity", newActivity)
                .setDefaults(Notification.DEFAULT_ALL) // 加上提醒效果:震動(DEFAULT_VIBRATE)，音效(DEFAULT_VIBRATE)，燈光(DEFAULT_LIGHT)
                //若有用到震動則需在AndroidManifest.xml加入權限<uses-permission android:name="android.permission.VIBRATE" />
                .build();

        startForeground(1, notification);
```
#### 提醒
使用Service需在AndroidManifest.xml加入Service 類別
``` xml
<service
        android:name=".ForegroundService"
        android:enabled="true"
        android:exported="true" />
```
### 範例
#### MainActivity

``` java
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
```

#### activity_main

``` xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">
    <Button
        android:id="@+id/buttonStartService"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Start Service"
        android:onClick="startService"
        />
    <Button
        android:id="@+id/buttonStopService"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Stop Service"
        android:onClick="stopService"
        />
</LinearLayout>
```
#### ForegroundService

``` java
package com.example.androidforegroundserviceexample;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
  
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("ForegroundService","onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //通知跳轉業面
        PendingIntent newActivity = PendingIntent.getActivity(this, 0, new Intent(this, NewActivity.class), 0);

        //通知關閉Service
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Stop Foreground Service in Android");
        serviceIntent.setAction("STOPFOREGROUND_ACTION");
        PendingIntent pStopSelf = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
      
        /************************/
        String input = intent.getStringExtra("inputExtra");
        Log.v("ForegroundService" , input);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("下拉列表中的Title")
                .setContentText("要显示的内容 要显示的内容 要显示的内容 要显示的内容") //過多的字以...呈現
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("要显示的内容 要显示的内容 要显示的内容 要显示的内容")) //顯示完整內容
                .setSmallIcon(R.drawable.ic_cake)// 設置狀態攔下小圖標，建議大小為32x32
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.smile)) // 設置狀態攔下大圖標，建議用PNG，大小為64x64
                .setWhen(System.currentTimeMillis())// 設置通知發生時間
                .addAction(R.drawable.ic_launcher_foreground, "Stop", pStopSelf)
                .addAction(R.drawable.ic_launcher_foreground, "NewActivity", newActivity)
                .setDefaults(Notification.DEFAULT_ALL) // 加上提醒效果:震動(DEFAULT_VIBRATE)，音效(DEFAULT_VIBRATE)，燈光(DEFAULT_LIGHT)
                //若有用到震動則需在AndroidManifest.xml加入權限<uses-permission android:name="android.permission.VIBRATE" />
                .build();

        //方法一
        if (intent.getAction().equals( "STARTFOREGROUND_ACTION")) {
            Log.i("ForegroundService", "Received Start Foreground Intent ");
            startForeground(1, notification);
        }
        else if (intent.getAction().equals( "STOPFOREGROUND_ACTION")) {
            Log.i("ForegroundService", "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelfResult(startId);
            //stopSelf();//停止Service
        }

        //方法二
        // startForeground(1, notification);
    
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

}
```

onStartCommand() 的回傳值決定 Service 被殺掉時要怎麼做對應的處理:
START_NOT_STICKY：如果系統在 onStartCommand() 後終止 Service，除非有待決的 intent 要傳送，否則請「不要」建立 Service。
START_STICKY：如果系統在 onStartCommand() 後終止 Service，請重新建立 Service 並呼叫 onStartCommand()，但「不要」重新傳送最後的 intent。 相反地，除非有待決的 intent 要啟動 service，否則系統會使用 null intent 呼叫，適用於媒體播放程式 。
START_REDELIVER_INTENT：如果系統在 onStartCommand() 後終止 Service，請重新建立 Service 並使用傳送至 Service的最後 intent 呼叫onStartCommand()。適用的 Service 為主動執行如下載檔案等應該立即繼續的工作。
         

PendingIntent有以下flag：
FLAG_CANCEL_CURRENT:如果當前系統中已經存在一個相同的PendingIntent對象，那麼就將先將已有的PendingIntent取消，然後重新生成一個PendingIntent對象。
FLAG_NO_CREATE:如果當前系統中不存在相同的PendingIntent對象，系統將不會創建該PendingIntent對象而是直接返回null。
FLAG_ONE_SHOT:該PendingIntent只作用一次。
FLAG_UPDATE_CURRENT:如果系統中已存在該PendingIntent對象，那麼系統將保留該PendingIntent對象，但是會使用新的Intent來更新之前PendingIntent中的Intent對像數據，例如更新Intent中的Extras。
創建PendingIntent方式：
PendingIntent.getActivity (context, requestCode, broadIntent, flags)
PendingIntent.getBroadcast(context,requestCode, broadIntent, flags)
PendingIntent.getService (context, requestCode, broadIntent, flags)



### 參考資料
官方文檔
https://developer.android.com/guide/components/foreground-services
Foreground Service
https://androidwave.com/foreground-service-android-example/
PendingIntent
https://blog.csdn.net/yangwen123/article/details/8019739