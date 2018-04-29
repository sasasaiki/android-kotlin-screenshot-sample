package net.rennsyuu.screenshotkotlinsample.common

import android.graphics.Bitmap
import android.support.v4.util.LruCache

object ImageCache {

    enum class Key(val str:String){
        TmpScreenShot("TmpScreenShot")
    }

    //キャッシュサイズをbyteで制限する場合
//    private const val CACHE_SIZE_BASE = 5
//    private const val CACHE_SIZE = CACHE_SIZE_BASE * 1024 * 1024

    private val sLruCache: LruCache<String, Bitmap>

    init {
        //キャッシュサイズをbyteで制限する場合、CACHE_SIZEを渡しsizeOfをoverrideする
        sLruCache = object : LruCache<String, Bitmap>(1) {
//            override fun sizeOf(key: String, value: Bitmap): Int {
//                return value.rowBytes * value.height
//            }
        }
    }


    operator fun get(key: String): Bitmap? {
        return sLruCache.get(key)
    }

    fun put(key: String, bitmap: Bitmap) {
        sLruCache.put(key, bitmap)
    }

    fun remove(key: String) {
        sLruCache.remove(key)
    }
}