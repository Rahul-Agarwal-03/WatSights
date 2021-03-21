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

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
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
        if(!from.equalsIgnoreCase("WhatsApp")&&!from.equalsIgnoreCase("WhatsApp Web"))
            new AddNotification(context).execute(message, from);
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
    }

    class AddNotification extends AsyncTask<String,Void,Void>{
        DbHelper dbHelper;
        Context context;

        public AddNotification(Context context) {
            this.context = context;
            dbHelper = new DbHelper(context);
        }

        @Override
        protected Void doInBackground(String... strings) {
            dbHelper = new DbHelper(context);
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
                    if (dbHelper.getGroupLastMessage(groupId) != null){
                        if(!dbHelper.getGroupLastMessage(groupId).equalsIgnoreCase(strings[0])) {
                            long id = dbHelper.addMessage(groupId, personId, strings[0], new Date().toString());
                            if(isImportant(strings[0])){
                                dbHelper.addImportantMessage(id);
                            }
                        }
                    } else {
                        Log.i(TAG, "doInBackground: same message notification");
                    }
                }
                else{
                    Log.d(TAG, "doInBackground: Group Messages not stored");
                }
            }
            else{
                long personId = dbHelper.getPersonId(strings[1]);
                if (dbHelper.getPersonLastMessage(personId) != null){
                    if(!dbHelper.getGroupLastMessage(personId).equalsIgnoreCase(strings[0])) {
                        long id = dbHelper.addMessage(0, personId, strings[0], new Date().toString());
                        if (isImportant(strings[0])) {
                            dbHelper.addImportantMessage(id);
                        }
                    }
                }

            }
            dbHelper.close();
            return null;
        }
        boolean isImportant(String message){
            FilterImportant filterImportant = new FilterImportant(message);
            return filterImportant.isImportant();
        }
    }
}