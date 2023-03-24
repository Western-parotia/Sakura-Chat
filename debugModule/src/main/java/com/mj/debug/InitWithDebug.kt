package com.mj.debug

import android.app.Application
import com.foundation.debug.exception.DebugExceptionHandler
import com.xpx.arch.ArchConfig

internal object InitWithDebug {
    fun onInit(app: Application) {
        if (!ArchConfig.debug) {
            throw RuntimeException("该模块只能使用debug依赖")
        }

        DebugExceptionHandler.init(app, ArchConfig.debug, ArchConfig.buildTime)
        //由于debugUtils的功能有耦合逻辑，所以暂时还是写在app里
//        DebugUtils.init(app, ArchConfig.debug, ZGRDebugTaskLoader)

        MultiModuleInit.initMultiCode(app)
    }
}