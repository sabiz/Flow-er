package jp.sabiz.flow_er.timer

import jp.sabiz.flow_er.flowitem.FlowItem

data class MinSec(val minutes: Long, val seconds: Long) {

    companion object {
        @JvmStatic
        fun from(time: Long) = MinSec(time / 60L, time % 60L)

        @JvmStatic
        fun from(flowItem: FlowItem): MinSec {
            return MinSec.from(flowItem.count)
        }
    }

    val totalSeconds: Long
        get() = minutes * 60L + seconds

    override fun toString(): String {
        var ret = ""
        if (minutes > 0L) {
            ret = "$minutes:"
        }
        ret += if (seconds < 10L) {
            "0$seconds"
        } else {
            "$seconds"
        }
        return ret
    }
}
