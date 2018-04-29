package net.rennsyuu.screenshotkotlinsample

import android.app.Service
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class CaptureActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CAPTURE = 1

        var projection: MediaProjection? = null
    }

    private lateinit var mediaProjectionManager: MediaProjectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty)
        mediaProjectionManager = getSystemService(Service.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //ScreenCaptureService(Intent)を実行した直後ここに入ってくる
        if (requestCode == REQUEST_CAPTURE) {
            if (resultCode == RESULT_OK) {
                projection = mediaProjectionManager.getMediaProjection(resultCode, data)
                val intent = Intent(this, CaptureService::class.java)
                       .setAction(CaptureService.Action.DoCapture.str)

                //初回の許可確認ダイアログが出た場合に閉じきる前に取られてしまうので少しだけ待つ
                Thread.sleep(100)

                startService(intent)
            } else {
                projection = null
                Toast.makeText(this, "キャプチャに失敗しました", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
}
