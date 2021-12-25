package jp.sabiz.flow_er.timer

data class MinSec(val minutes: ULong, val seconds: ULong) {

    companion object {
        @JvmStatic
        fun from(time: ULong) = MinSec(time / 60UL, time % 60UL)
    }

    val totalSeconds: ULong
        get() = minutes * 60UL + seconds

    override fun toString(): String {
        var ret = ""
        if (minutes > 0UL) {
            ret = "$minutes:"
        }
        ret += if (seconds < 10UL) {
            "0$seconds"
        } else {
            "$seconds"
        }
        return ret
    }
}
