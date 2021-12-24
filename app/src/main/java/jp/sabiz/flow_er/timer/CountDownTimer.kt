package jp.sabiz.flow_er.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

typealias CountDownTimerListener = (currentTimeMin: ULong, currentTimeSec: ULong) -> Unit

class CountDownTimer (private var initialTimeSec: ULong, private val viewModel: ViewModel) {

    private var isStarted = false
    private var currentTimeSec: ULong = 0UL
    private var currentJob: Job? = null

    companion object {

        @JvmStatic
        fun toTime(timeMin: ULong, timeSec: ULong) = (timeMin * 60UL) + timeSec
    }

    var time: ULong
        get() = initialTimeSec
        set(value) {
            reset()
            initialTimeSec = value
        }


    fun start(listener: CountDownTimerListener) {
        if (!isStarted) {
            reset()
            isStarted = true
            currentTimeSec = initialTimeSec
            call(listener)
        }

        currentJob = viewModel.viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            currentTimeSec--
            withContext(Dispatchers.Main) {
                call(listener)
            }
            if (currentTimeSec > 0UL) {
                start(listener)
            } else {
                reset()
            }
        }
    }

    fun stop() {
        currentJob?.cancel()
        currentJob = null
    }

    fun reset() {
        stop()
        isStarted = false
    }

    private fun call(listener: CountDownTimerListener) {
        val tempCurrentTimeMin = currentTimeSec / 60UL
        val tempCurrentTimeSec = currentTimeSec % 60UL
        listener.invoke(tempCurrentTimeMin, tempCurrentTimeSec)
    }
}