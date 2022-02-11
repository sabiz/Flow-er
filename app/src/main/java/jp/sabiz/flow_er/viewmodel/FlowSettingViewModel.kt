package jp.sabiz.flow_er.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.sabiz.flow_er.FlowerApplication
import jp.sabiz.flow_er.flowitem.FlowItem
import jp.sabiz.flow_er.flowitem.db.FlowDatabase
import jp.sabiz.flow_er.flowitem.db.FlowItemDao
import jp.sabiz.flow_er.timer.MinSec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlowSettingViewModel(application: Application): AndroidViewModel(application) {

    private val flowerApplication = application as FlowerApplication
    private val flowItemDao: FlowItemDao = FlowDatabase.get(application).flowItemDao()

    var flowList = MutableLiveData<List<FlowItem>>(emptyList())


    init {
        load()
    }

    fun load() {
        flowerApplication.scopeBackground.launch {
            val value = flowItemDao.loadAll()
            withContext(Dispatchers.Main) {
                flowList.value = value
            }
        }
    }

    fun addNewFlowItem(onDone: ()->Unit) {
        flowerApplication.scopeBackground.launch {
            val pos = flowItemDao.count() + 1
            val newItem = FlowItem.newItem(pos)
            flowItemDao.add(newItem)
            load()
            withContext(Dispatchers.Main) {
                onDone.invoke()
            }
        }
    }

    fun deleteItem(item: FlowItem) {
        flowerApplication.scopeBackground.launch {
            flowItemDao.delete(item)
            load()
        }

    }
}