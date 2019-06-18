package com.elbndarmarket;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;


/**
 * Created by Ma7MouD on 4/30/2018.
 */

public class MyFCMService extends FirebaseMessagingService {

    int count = 0;
    int notification_id = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        try {
//            Badges.setBadge(this, ++count);
//        } catch (BadgesNotSupportedException badgesNotSupportedException) {
//            Log.d(TAG, badgesNotSupportedException.getMessage());
//        }

        if (remoteMessage.getData() != null) {
            String Ntitle = remoteMessage.getData().get("title");
            String Nmessage = remoteMessage.getData().get("body");

//            // Create new message and assign value to it
//            ChatMessageResponseModel.Message message = new ChatMessageResponseModel.Message();
//            message.setMessage(msg);
//            message.setSenderId(sender_id);
//            message.setMsgDate(msg_date);
//            message.setReceiverId(receiver_id);

            // check if the sender of message is current user or not//
//                // check if app in background or not
            if (isAppIsInBackground(this)) {
                // app is in background show notification to user
//                        sendNotification(message);
                NotificationHelper.getInstance(this).createNotification(Ntitle, Nmessage);
            } else {
                // app is forground and user see it now send broadcast to update chat
                // you can send broadcast to do anything if you want !
//                    Intent intent = new Intent("UpdateChateActivity");
//                    intent.putExtra("msg", message);
//                    sendBroadcast(intent);
            }
        }
    }

    /**
     * Method check if app is in background or in foreground
     *
     * @param context this contentx
     * @return true if app is in background or false if app in foreground
     */
    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}