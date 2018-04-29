package net.rennsyuu.screenshotkotlinsample

import android.app.Service
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import net.rennsyuu.screenshotkotlinsample.common.ImageCache

class CaptureActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CAPTURE = 1
        const val EndCaptureActionName = "ON_END_CAPTURE"
    }
    var projection: MediaProjection? = null

    private lateinit var mediaProjectionManager: MediaProjectionManager
    private val capture = Capture(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaProjectionManager = getSystemService(Service.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //ScreenCaptureService(Intent)を実行した直後ここに入ってくる
        if (requestCode == REQUEST_CAPTURE) {
            if (resultCode == RESULT_OK) {
                projection = mediaProjectionManager.getMediaProjection(resultCode, data)

                //初回の許可確認ダイアログが出た場合に閉じきる前に取られてしまうので少しだけ待つ
                Thread.sleep(100)

                doCapture()
            } else {
                projection = null
                Toast.makeText(this, "キャプチャに失敗しました", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

    private fun doCapture() {
        projection?.let {
            capture.run(it) {bitMap ->
                //キャプチャを止めないと呼ばれ続ける
                disableCapture()
                // save bitmap
                ImageCache.put(ImageCache.Key.TmpScreenShot.str,bitmap = bitMap)
                sendMessage()
                finish()
            }
        }
    }

    private fun sendMessage() {
        val broadcast = Intent()
        broadcast.action = EndCaptureActionName
        baseContext.sendBroadcast(broadcast)
    }

    private fun disableCapture() {
        capture.stop()
        projection = null
    }

    override fun onDestroy() {
        super.onDestroy()
        disableCapture()
    }
}
