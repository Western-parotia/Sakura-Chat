package com.sakura.chat.v2.business.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sakura.chat.v2.base.net.AllNetLoadingHandler
import com.sakura.chat.v2.base.net.BaseViewModel
import java.io.File

class OpenAIViewModel : BaseViewModel() {
    private val _voiceRes = MutableLiveData<String>()
    val voiceRes: LiveData<String> = _voiceRes


    fun sendMessage(file: File) {

    }

    fun sendMessage(desc: String) {

    }

    fun sendMessage(file: File?, question: String) {

        netLaunch("sendMessage") {
            file?.let {
                if (it.length() > 0) {
                    withBusiness {
                        apiService.translationsVoice()
                    }
                }
            }


            apiService.translationsVoice()


        }.start(AllNetLoadingHandler())


    }


}