package jp.sabiz.flow_er.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.constraintlayout.widget.ConstraintSet

import androidx.constraintlayout.widget.ConstraintLayout

import androidx.constraintlayout.widget.Guideline

import androidx.databinding.BindingAdapter
import jp.sabiz.flow_er.fragment.TimerFragment
import jp.sabiz.flow_er.timer.MinSec


class TimerViewModel : ViewModel() {
    var state = MutableLiveData(TimerFragment.State.STAND_BY)
    var progress = MutableLiveData(0F)
    var currentMin = MutableLiveData(0UL)
    var currentSec = MutableLiveData(0UL)
    var timerSequence = MutableLiveData(listOf(
                                            MinSec.from(80UL),
                                            MinSec.from(25UL),
                                            MinSec.from(40UL),
                                            MinSec.from(25UL),
                                            MinSec.from(40UL)))
}