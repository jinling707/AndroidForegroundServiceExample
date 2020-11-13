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
    MediaPlayer mediaPlayer;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("ForegroundService","onCreate");
//        //音樂播放測試
//        mediaPlayer = MediaPlayer.create(this, R.raw.music);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
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
        /*
        PendingIntent有以下flag：
        FLAG_CANCEL_CURRENT:如果当前系统中已经存在一个相同的PendingIntent对象，那么就将先将已有的PendingIntent取消，然后重新生成一个PendingIntent对象。
        FLAG_NO_CREATE:如果当前系统中不存在相同的PendingIntent对象，系统将不会创建该PendingIntent对象而是直接返回null。
        FLAG_ONE_SHOT:该PendingIntent只作用一次。
        FLAG_UPDATE_CURRENT:如果系统中已存在该PendingIntent对象，那么系统将保留该PendingIntent对象，但是会使用新的Intent来更新之前PendingIntent中的Intent对象数据，例如更新Intent中的Extras。
        创建PendingIntent方式：
        PendingIntent.getActivity (context, requestCode, broadIntent, flags)
        PendingIntent.getBroadcast(context,requestCode, broadIntent, flags)
        PendingIntent.getService (context, requestCode, broadIntent, flags)
        */


        String input = intent.getStringExtra("inputExtra");
        Log.v("ForegroundService" , input);

        createNotificationChannel();

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
            //your end servce code
            stopForeground(true);
            stopSelfResult(startId);
//            //音樂播放測試
//            mediaPlayer.stop();
//            mediaPlayer.release();

            //stopSelf();//停止Service
        }

        //方法二
        // startForeground(1, notification);

        return START_NOT_STICKY;
        /*
        onStartCommand() 的回傳值決定 Service 被殺掉時要怎麼做對應的處理:
        START_NOT_STICKY：如果系統在 onStartCommand() 後終止 Service，除非有待決的 intent 要傳送，否則請「不要」建立 Service。
        START_STICKY：如果系統在 onStartCommand() 後終止 Service，請重新建立 Service 並呼叫 onStartCommand()，但「不要」重新傳送最後的 intent。 相反地，除非有待決的 intent 要啟動 service，否則系統會使用 null intent 呼叫，適用於媒體播放程式 。
        START_REDELIVER_INTENT：如果系統在 onStartCommand() 後終止 Service，請重新建立 Service 並使用傳送至 Service的最後 intent 呼叫onStartCommand()。適用的 Service 為主動執行如下載檔案等應該立即繼續的工作。
         */
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

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}