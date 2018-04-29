package net.rennsyuu.screenshotkotlinsample

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import net.rennsyuu.screenshotkotlinsample.common.ImageCache

class MyService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // receiver
        val receiver = CaptureEndReceiver()
        val filter = IntentFilter()
        filter.addAction(CaptureActivity.EndCaptureActionName)
        registerReceiver(receiver, filter)
        return super.onStartCommand(intent, flags, startId)
    }

    inner class CaptureEndReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bitMap = ImageCache[ImageCache.Key.TmpScreenShot.str]
            bitMap?.let { Log.d("debug","サービスで処理をさせることもできる") }
        }
    }
}
