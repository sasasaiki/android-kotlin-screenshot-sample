package net.rennsyuu.screenshotkotlinsample

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

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

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val intent = Intent(this, CaptureService::class.java)
                .setAction(CaptureService.Action.StartCapture.str)
            startService(intent)
        }

        // receiver
        val receiver = CaptureEndReceiver()
        val filter = IntentFilter()
        filter.addAction(CaptureService.CaptureEndActionName)
        registerReceiver(receiver, filter)
    }

    fun setImage(image:Bitmap){
        image_view.setImageBitmap(image)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    inner class CaptureEndReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bitMap = ImageCache[ImageCache.Key.TmpScreenShot.str]
            bitMap?.let { setImage(it) }
        }
    }

}
