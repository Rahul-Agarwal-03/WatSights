package com.absolutezero.watsights;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Date;

public class NotificationService extends NotificationListenerService {
    private static final String TAG = "NotificationService";
    private static final String WHATSAPP_PACKAGE = "com.whatsapp";
    Context context ;
    DbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        dbHelper = new DbHelper(context);
    }
    @Override
    public void onListenerConnected() {
        Log.i(TAG, "Notification Listener connected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log. i ( TAG , "********** onNotificationPosted" ) ;
        Log. i ( TAG , "ID :" + sbn.getId() + " \t " + sbn.getNotification().tickerText + " \t " + sbn.getPackageName()) ;
//        notificationListener.setValue("Post: " + sbn.getPackageName());
        if (!sbn.getPackageName().equals(WHATSAPP_PACKAGE)) return;
        Notification notification = sbn.getNotification();
        Bundle bundle = notification.extras;

        String from = bundle.getString(NotificationCompat.EXTRA_TITLE);
        String message = bundle.getString(NotificationCompat.EXTRA_TEXT);

        Log.i(TAG, "From: " + from);
        Log.i(TAG, "Message: " + message);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log. i ( TAG , "********** onNotificationRemoved" ) ;
        if (!sbn.getPackageName().equals(WHATSAPP_PACKAGE)) return;
        Notification notification = sbn.getNotification();
        Bundle bundle = notification.extras;
        String from = bundle.getString(NotificationCompat.EXTRA_TITLE);
        String message = bundle.getString(NotificationCompat.EXTRA_TEXT);
        new AddNotification().execute(message,from);
    }

    class AddNotification extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            Log.d(TAG, "doInBackground() returned: message => " + strings[0]);
            Log.d(TAG, "doInBackground() returned: from =>" + strings[1]);
            if (strings[1].contains(":")) {
                String temp = strings[1];
                String name;
                if(temp.contains("("))
                    name = strings[1].substring(0,strings[1].lastIndexOf('(')).trim();
                else
                    name = strings[1].substring(0, strings[1].lastIndexOf(':')).trim();
                long groupId = dbHelper.getGroupId(name);
                String from = strings[1].substring(strings[1].lastIndexOf(':')+1).trim();
                long personId = dbHelper.getPersonId(from);
                dbHelper.addMember(groupId, personId);
                if (dbHelper.storeGroupMessages(groupId)) {
                    dbHelper.addMessage(groupId, personId, strings[0], new Date().toString());    
                }
                else{
                    Log.d(TAG, "doInBackground: Group Messages not stored");
                }
            }
            else{
                long personId = dbHelper.getPersonId(strings[1]);
                dbHelper.addMessage(0, personId, strings[0], new Date().toString());
            }
            return null;
        }
    }
}