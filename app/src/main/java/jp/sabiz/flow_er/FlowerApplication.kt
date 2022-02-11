package jp.sabiz.flow_er

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class FlowerApplication: Application() {

    val scopeBackground = CoroutineScope(Dispatchers.IO)
}