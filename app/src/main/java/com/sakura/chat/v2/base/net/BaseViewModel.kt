package com.sakura.chat.v2.base.net

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foundation.service.net.NetManager
import com.foundation.service.net.NetViewModel
import com.foundation.widget.utils.other.MjPage
import com.sakura.chat.v2.base.loading.LoadingAction
import com.sakura.chat.v2.base.loading.LoadingControl
import com.sakura.chat.v2.base.loading.LoadingProgress
import retrofit2.Response

/**
 * create by zhusw on 6/9/21 14:11
 */
open class BaseViewModel : NetViewModel() {
    protected val apiService = NetManager.getApiService(ApiService::class.java)

    protected val _initLoadingLiveData = MutableLiveData<LoadingProgress>()
    val initLoadingLiveData: LiveData<LoadingProgress> = _initLoadingLiveData

    protected val _refreshLoadingLiveData = MutableLiveData<LoadingProgress>()
    val refreshLoadingLiveData: LiveData<LoadingProgress> = _refreshLoadingLiveData

    protected val _loadMoreLoadingLiveData = MutableLiveData<LoadingProgress>()
    val loadMoreLoadingLiveData: LiveData<LoadingProgress> = _loadMoreLoadingLiveData

    protected fun factoryLoadingControl(loadingAction: LoadingAction): LoadingControl {
        val liveData = when (loadingAction) {
            LoadingAction.INIT -> {
                _initLoadingLiveData
            }
            LoadingAction.REFRESH -> {
                _refreshLoadingLiveData
            }
            LoadingAction.LOAD_MORE -> {
                _loadMoreLoadingLiveData
            }
        }
        return LoadingControl(liveData)
    }

    protected fun factoryLoadingControl(liveData: MutableLiveData<LoadingProgress>?): LoadingControl {
        return LoadingControl(liveData)
    }

    /**
     * 根据page自动判断几个状态
     */
    protected fun factoryLoadingPageControl(page: MjPage): LoadingControl {
        return when {
            page.isInit -> {
                factoryLoadingControl(LoadingAction.INIT)
            }
            page.isFirst() -> {
                factoryLoadingControl(LoadingAction.REFRESH)
            }
            else -> {
                factoryLoadingControl(LoadingAction.LOAD_MORE)
            }
        }
    }

    /**
     * 根据page自动判断几个状态
     */
    protected fun factoryLoadingPageHandler(
        page: MjPage,
        onCoverInterceptor: ((e: ResException, loadingControl: LoadingControl?) -> Boolean)? = null
    ) = factoryLoadingHandler(factoryLoadingPageControl(page), onCoverInterceptor)

    protected fun factoryLoadingHandler(
        action: LoadingAction = LoadingAction.INIT,
        onCoverInterceptor: ((e: ResException, loadingControl: LoadingControl?) -> Boolean)? = null
    ) = factoryLoadingHandler(factoryLoadingControl(action), onCoverInterceptor)

    /**
     * @param onCoverInterceptor 异常拦截回调，和[AllNetLoadingHandler.coverException]一样，true则拦截此异常不走默认处理
     *                           注意：true表示完全处理，[loadingControl]也不会发送事件，需要手动调用loadingControl.doXxx
     */
    protected fun factoryLoadingHandler(
        control: LoadingControl?,
        onCoverInterceptor: ((e: ResException, loadingControl: LoadingControl?) -> Boolean)? = null
    ) = AllNetLoadingHandler(control, onCoverInterceptor = onCoverInterceptor)

    /**
     * 业务层处理
     * 无根数据结构
     * 需要自己判断具体业务 是否为空
     * */
    protected suspend fun <E> withBusiness(block: suspend () -> Response<E>): E {
        return NetDataFilter.withBusiness(block)
    }

}