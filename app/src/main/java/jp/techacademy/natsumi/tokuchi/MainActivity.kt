package jp.techacademy.natsumi.tokuchi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.os.Handler
import java.util.*

class MainActivity : AppCompatActivity() {
    // 許可のための変数
    private val  PERMISSIONS_REQUEST_CODE = 100

    // タイマーのための変数
    private var mTimer: Timer? = null

    private var mTimerSec = 0.0

    private  var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 外部ストレージへの許可を確認する
        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }

        // タイマーを設定する
        play_pause_button.setOnClickListener {
            if (mTimer == null) {
                mTimer = Timer()
                mTimer!!.schedule(object: TimerTask() {
                    override fun run() {
                        mTimerSec += 2.0
                        mHandler.post {

                        }
                    }
                }, 2000, 2000)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                } else {
                    Toast.makeText(applicationContext, "アプリを利用するには許可が必要です", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun getContentsInfo() {
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        if (cursor!!.moveToFirst()) {
            do {
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                Log.d("ASA", "URI : $imageUri")

            } while (cursor.moveToNext())
        }
        cursor.close()
    }
}
