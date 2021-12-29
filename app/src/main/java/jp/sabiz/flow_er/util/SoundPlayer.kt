package jp.sabiz.flow_er.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool


class SoundPlayer {

    private val audioAttributes by lazy {
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .build()

    }
    private val soundPool by lazy {
        SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(2)
            .build()
    }
    private val loadId = mutableListOf<Int>()

    fun load(context: Context ,soundIdArray: IntArray) {
        soundIdArray.forEach {
            loadId.add(soundPool.load(context, it, 1))
        }
    }

    fun play(idx:Int) {
        loadId.getOrNull(idx)?.let {
            soundPool.play(it, 1.5F, 1.5F, 1, 0, 1F)
        }
    }
}