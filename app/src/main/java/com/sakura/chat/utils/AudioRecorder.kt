package com.sakura.chat.utils

import android.media.MediaRecorder
import com.sakura.chat.core.App
import java.io.File

class AudioRecorder {
    private var recorder = MediaRecorder()

    /**
     * -1停止，0暂停，1录音中，2录音完成
     */
    var state = -1
        private set

    val fileDir = File(App.get().cacheDir.absolutePath + "/audio").let {
        if (!it.exists()) {
            it.mkdirs()
        }
        "${it.absolutePath}/${System.currentTimeMillis()}.mp4"
    }

    init {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        recorder.setOutputFile(fileDir)
        recorder.prepare()
    }

    fun start() {
        when (state) {
            -1 -> {
                recorder.start()
                state = 1
            }
            0 -> {
                resume()
            }
        }
    }

    fun pause() {
        if (state == 1) {
            recorder.pause()
            state = 0
        }
    }

    fun resume() {
        if (state == 0) {
            recorder.resume()
            state = 1
        }
    }

    fun stop() {
        when (state) {
            0, 1 -> {
                state = 2
                recorder.stop()
                recorder.release()
            }
        }
    }
}
