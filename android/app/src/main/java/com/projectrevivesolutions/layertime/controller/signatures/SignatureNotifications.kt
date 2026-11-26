package com.projectrevivesolutions.layertime.controller.signatures
import android.app.*
import android.content.*
import com.projectrevivesolutions.layertime.controller.R
import com.projectrevivesolutions.layertime.controller.ui.MainActivity

/** Normal background delivery; failure cannot stop evidence collection or erase a result. */
class SignatureNotifications(private val context:Context) {
    fun post(key:String,text:String,quiet:Boolean):Boolean=runCatching {
        val manager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(!manager.areNotificationsEnabled())return false
        val prefs=context.getSharedPreferences("signature-notifications",Context.MODE_PRIVATE)
        if(prefs.getBoolean(key,false))return true
        val channel=if(quiet)"signature-review-quiet" else "signature-review"
        manager.createNotificationChannel(NotificationChannel(channel,"Evidence sequence reviews",if(quiet)NotificationManager.IMPORTANCE_LOW else NotificationManager.IMPORTANCE_DEFAULT))
        val open=PendingIntent.getActivity(context,0,Intent(context,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val note=Notification.Builder(context,channel).setSmallIcon(R.drawable.ic_vixen_notification)
            .setContentTitle("Evidence sequence reviewed").setContentText(text).setStyle(Notification.BigTextStyle().bigText(text))
            .setContentIntent(open).setAutoCancel(true).setOnlyAlertOnce(true).build()
        manager.notify("signature-$key",402,note)
        prefs.edit().putBoolean(key,true).apply()
        true
    }.getOrDefault(false)
}
