package com.openpay.pruebatecnica.firebase
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.openpay.pruebatecnica.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseMessaginge"
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "    MENSAJE RECIBIDO    ")
        Log.d(TAG, "From: " + remoteMessage.from)
        Log.d(TAG, "Message Notification Title: " + remoteMessage.notification?.title)
        Log.d(TAG, "Message Notification Body: " + remoteMessage.notification?.body)
        // Método llamado cuando se recibe una notificación push
        // Extraer información del mensaje
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        // Mostrar la notificación
        showNotification(title, body)
    }

    private fun showNotification(title: String?, body: String?) {
        Log.d(TAG, "showNotification")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Configurar el canal de notificación (necesario para versiones de Android >= Oreo)
        val channelId = "my_channel_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Channel"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "My Channel Description"
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            notificationManager.createNotificationChannel(channel)
        }

        // Construir la notificación
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.location_on_24)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        Log.d(TAG, "before notificationManager")
        // Mostrar la notificación
        notificationManager.notify(1110, notificationBuilder.build())
    }
}