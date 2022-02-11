package jp.sabiz.flow_er.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.constraintlayout.widget.ConstraintSet

import androidx.constraintlayout.widget.ConstraintLayout

import androidx.constraintlayout.widget.Guideline

import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import jp.sabiz.flow_er.FlowerApplication
import jp.sabiz.flow_er.flowitem.db.FlowDatabase
import jp.sabiz.flow_er.flowitem.db.FlowItemDao
import jp.sabiz.flow_er.fragment.TimerFragment
import jp.sabiz.flow_er.timer.MinSec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TimerViewModel(application: Application): AndroidViewModel(application) {

    private val flowerApplication = application as FlowerApplication
    private val flowItemDao: FlowItemDao = FlowDatabase.get(application).flowItemDao()

    var state = MutableLiveData(TimerFragment.State.STAND_BY)
    var progress = MutableLiveData(0F)
    var currentMin = MutableLiveData(0L)
    var currentSec = MutableLiveData(0L)
    var timerSequence:MutableLiveData<MutableList<MinSec>> = MutableLiveData(mutableListOf())

    init {
        loadSequence()
    }

    fun loadSequence() {
        flowerApplication.scopeBackground.launch {
            val value = flowItemDao.loadAll().map { MinSec.from(it) }.toMutableList()
            withContext(Dispatchers.Main) {
                timerSequence.value = value
            }
        }
    }
}