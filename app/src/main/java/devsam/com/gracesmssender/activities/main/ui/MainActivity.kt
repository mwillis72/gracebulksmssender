package devsam.com.gracesmssender.activities.main.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import devsam.com.gracesmssender.R
import devsam.com.gracesmssender.activities.main.fragments.SplashScreenFragment

import devsam.com.gracesmssender.activities.main.fragments.mainScreen.ui.MainScreenFragment
import devsam.com.gracesmssender.util.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private companion object {
        private const val FRAGMENT_CONTAINER = R.id.mainActivityRootFragContainer
        private const val SPLASH_DELAY_VALUE = 5000L
    }

    private val timer = Timer()
    private val mainScreenFragment by lazy {
        MainScreenFragment.getInstance()
    }

    private var firstFragmentFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenWindow()
        setContentView(R.layout.activity_main)
        addFragment(SplashScreenFragment.getInstance(), FRAGMENT_CONTAINER)
    }

    override fun onResume() {
        super.onResume()
        if (firstFragmentFlag)
            startTimerSchedule()
    }

    private fun startTimerSchedule() {
        timer.schedule(SPLASH_DELAY_VALUE) {
            runOnUiThread { clearWindowsFlag() }
            replaceFragment(mainScreenFragment, FRAGMENT_CONTAINER)
            firstFragmentFlag = false
        }
    }

    override fun onStop() {
        super.onStop()
        if (firstFragmentFlag) timer.cancel()
    }

    override fun onBackPressed() {}

    override fun onKeyDown(keyCode: Int, event: KeyEvent?) = if (keyCode == KeyEvent.KEYCODE_BACK) {
        moveTaskToBack(true)
        true
    } else false

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancelAll()
    }
}
