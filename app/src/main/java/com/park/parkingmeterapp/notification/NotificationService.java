package com.park.parkingmeterapp.notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Jackk on 8/25/2016.
 */
public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = NotificationService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage != null){
            sendNotification(remoteMessage);
            remoteMessage.getNotification().getClickAction();
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String message = "", title = "";
        if(remoteMessage.getNotification().getBody() != null){
            message = remoteMessage.getNotification().getBody();
        }

        if(remoteMessage.getNotification().getTitle() != null){
            title = remoteMessage.getNotification().getTitle();
        }

        Log.e(TAG, "sendNotification: message " + message + " title " + title);
//        Log.e(TAG, "sendNotification: message " + message);
//        Intent intent = new Intent(this,MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setAction(Constant.APP_PNAME + "_" + System.currentTimeMillis());
//        if(remoteMessage.getData().size() > 0){
//            Log.e(TAG, "sendNotification: data " + remoteMessage.getData().toString());
//            Iterator it = remoteMessage.getData().entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry pair = (Map.Entry)it.next();
//                System.out.println(pair.getKey() + " = " + pair.getValue());
//                intent.putExtra(pair.getKey().toString(),pair.getValue().toString());
//                // avoids a ConcurrentModificationException
//                it.remove();
//            }
//        }

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setLargeIcon(bitmap)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setPriority(Notification.PRIORITY_HIGH)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notificationBuilder.build());
    }
}
