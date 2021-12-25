package jp.sabiz.flow_er.timer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

typealias CountDownTimerListener = (remainingTimeMin: ULong, remainingTimeSec: ULong, remainingAllMilliSec: ULong) -> Unit

class CountDownTimer (private var initialTimeSec: ULong, private val viewModel: ViewModel) {

    private var isStarted = false
    private var currentTimeMilliSec: Long = 0L
    private var currentJob: Job? = null

    companion object {

        private const val TIMER_INTERVAL_MS = 150L

        @JvmStatic
        fun toTime(timeMin: ULong, timeSec: ULong) = (timeMin * 60UL) + timeSec
    }

    var time: ULong
        get() = initialTimeSec
        set(value) {
            reset()
            initialTimeSec = value
        }

    var timeMilliSec: ULong
        get() = time * 1000UL
        private set(value) {}


    @Synchronized
    fun start(listener: CountDownTimerListener) {
        if (!isStarted) {
            reset()
            isStarted = true
            currentTimeMilliSec = initialTimeSec.toLong() * 1000L
            call(listener)
        }

        currentJob = viewModel.viewModelScope.launch(Dispatchers.IO) {
            delay(TIMER_INTERVAL_MS)
            currentTimeMilliSec-=TIMER_INTERVAL_MS
            if(currentTimeMilliSec < 0L) {
                currentTimeMilliSec = 0L
            }
            withContext(Dispatchers.Main) {
                call(listener)
            }
            if (currentTimeMilliSec > 0L) {
                start(listener)
            }
        }
    }

    @Synchronized
    fun stop() {
        currentJob?.cancel()
        currentJob = null
    }

    @Synchronized
    fun reset() {
        stop()
        isStarted = false
    }

    private fun call(listener: CountDownTimerListener) {
        val tempCurrentTimeMin = (currentTimeMilliSec / 1000L / 60L).toULong()
        val tempCurrentTimeSec = (currentTimeMilliSec / 1000L % 60L).toULong()
        listener.invoke(tempCurrentTimeMin, tempCurrentTimeSec, currentTimeMilliSec.toULong())
    }
}