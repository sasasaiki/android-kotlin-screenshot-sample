package net.rennsyuu.screenshotkotlinsample

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.content.BroadcastReceiver
import android.content.Context
import net.rennsyuu.screenshotkotlinsample.common.ImageCache
import android.content.IntentFilter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //ボタンを押したらスクリーンショットを撮る
        fab.setOnClickListener {
            saveScreenShot()
        }

        //サービスで処理させるデモ。なくてもよろしい
        val intent = Intent(this, MyService::class.java)
        startService(intent)

        // receiverをセットする
        val receiver = CaptureEndReceiver()
        val filter = IntentFilter()
        filter.addAction(CaptureActivity.END_CAPTURE_ACTION_NAME)
        registerReceiver(receiver, filter)
    }

    private fun saveScreenShot(){
        val intent = Intent(this, CaptureActivity::class.java)
        startActivityForResult(intent,CaptureActivity.REQUEST_CAPTURE)
    }

    fun setImage(image:Bitmap){
        image_view.setImageBitmap(image)
    }

    inner class CaptureEndReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bitMap = ImageCache[ImageCache.Key.TmpScreenShot.str]
            bitMap?.let { setImage(it) }
        }
    }

}

