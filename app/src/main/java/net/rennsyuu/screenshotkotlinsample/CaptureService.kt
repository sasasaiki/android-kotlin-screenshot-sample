package net.rennsyuu.screenshotkotlinsample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import net.rennsyuu.screenshotkotlinsample.common.ImageCache

class CaptureService : Service() {
    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val CaptureEndActionName = "ON_CAPTURE_END"
    }

    enum class Action(val str: String) {
        DoCapture("enable_capture")
    }

    private val capture = Capture(this)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                Action.DoCapture.str -> doCapture()
            }
        }
        return Service.START_STICKY
    }

    private fun doCapture() {
        CaptureActivity.projection?.let {
            capture.run(it) {bitMap ->
                // save bitmap
                ImageCache.put(ImageCache.Key.TmpScreenShot.str,bitmap = bitMap)
                sendMessage()
                stopSelf()
            }
        }
    }

    private fun disableCapture() {
        capture.stop()
        CaptureActivity.projection = null
    }

    private fun sendMessage() {
        val broadcast = Intent()
        broadcast.action = CaptureEndActionName
        baseContext.sendBroadcast(broadcast)
    }

    override fun onDestroy() {
        super.onDestroy()
        disableCapture()
    }
}
