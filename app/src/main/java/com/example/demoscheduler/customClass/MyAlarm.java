package com.example.demoscheduler.customClass;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.demoscheduler.MainActivity;
import com.example.demoscheduler.R;

public class MyAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notiID=intent.getIntExtra("notiId",0);
        String subject=intent.getStringExtra("Subject");
        String desc=intent.getStringExtra("Desc");

        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,notiID,mainIntent,0);

        long[] pattern = {500,500,500,500,500,500,500,500,500,500,500};
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getPackageName() + "/" + R.raw.alarm_sound);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        String CHANNEL_ID = "Schedule";
        Notification notification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentIntent(pendingIntent)
                .setContentTitle(subject)
                .setContentText(desc)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setWhen(System.currentTimeMillis())
                .build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,subject,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setVibrationPattern(pattern);
            channel.setSound(soundUri,audioAttributes);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notiID,notification);
    }

}
