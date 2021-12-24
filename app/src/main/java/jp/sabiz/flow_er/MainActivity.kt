package jp.sabiz.flow_er

import android.os.Build
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import android.R
import android.util.Log
import android.view.WindowManager
import jp.sabiz.flow_er.fragment.TimerFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.addToBackStack("root")
            transaction.replace(R.id.content, TimerFragment.newInstance())
            transaction.commit()
        }
    }
}