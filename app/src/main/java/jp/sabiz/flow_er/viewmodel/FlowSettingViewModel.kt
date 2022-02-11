package jp.sabiz.flow_er.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import jp.sabiz.flow_er.FlowerApplication
import jp.sabiz.flow_er.extension.moveAt
import jp.sabiz.flow_er.flowitem.FlowItem
import jp.sabiz.flow_er.flowitem.db.FlowDatabase
import jp.sabiz.flow_er.flowitem.db.FlowItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlowSettingViewModel(application: Application): AndroidViewModel(application) {

    private val flowerApplication = application as FlowerApplication
    private val flowItemDao: FlowItemDao = FlowDatabase.get(application).flowItemDao()

    var flowList = MutableLiveData<MutableList<FlowItem>>(mutableListOf())


    init {
        load()
    }

    private fun load() {
        flowerApplication.scopeBackground.launch {
            val value = flowItemDao.loadAll()
            withContext(Dispatchers.Main) {
                flowList.value = value.toMutableList()
            }
        }
    }

    fun addNewFlowItem(onDone: ()->Unit) {
        flowerApplication.scopeBackground.launch {
            val pos = flowItemDao.count() + 1
            val newItem = FlowItem.newItem(pos)
            flowItemDao.add(newItem)
            flowList.value?.add(newItem)
            withContext(Dispatchers.Main) {
                onDone.invoke()
            }
        }
    }

    fun deleteItem(item: FlowItem, onDone: ()->Unit) {
        flowerApplication.scopeBackground.launch {
            flowItemDao.delete(item)
            withContext(Dispatchers.Main) {
                flowList.value?.remove(item)
                val newList = flowList.value?.mapIndexed { index, flowItem ->
                    flowItem.clone(position =  index + 1)
                }?: emptyList()
                flowList.value?.removeAll { true }
                flowList.value?.addAll(newList)
                onDone.invoke()
            }
        }
    }

    fun updateItem(targetItem: FlowItem, count: Long = 0, onDone: ()->Unit) {
        flowerApplication.scopeBackground.launch {
            val pos = flowList.value?.indexOf(targetItem) ?: return@launch
            val newItem = targetItem.clone(count = count)
            flowItemDao.update(newItem)

            withContext(Dispatchers.Main) {
                flowList.value?.removeAt(pos)
                flowList.value?.add(pos, newItem)
                onDone.invoke()
            }
        }
    }

    fun moveItem(from: Int, to: Int, onDone: ()->Unit) {
        flowerApplication.scopeBackground.launch {
            flowList.value?.moveAt(from, to)
            val newList = flowList.value?.mapIndexed { index, flowItem ->
                flowItem.clone(position =  index + 1)
            }?: emptyList()
            flowItemDao.update(*(newList.toTypedArray()))

            withContext(Dispatchers.Main) {
                onDone.invoke()
            }
        }


    }
}