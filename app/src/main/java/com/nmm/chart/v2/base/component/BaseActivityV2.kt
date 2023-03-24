package com.nmm.chart.v2.base.component

import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.LiveData
import com.foundation.app.arc.activity.BaseFragmentManagerActivity
import com.foundation.widget.loading.IPageLoading
import com.foundation.widget.loading.PageLoadingAdapter
import com.foundation.widget.utils.other.MjPage
import com.nmm.chart.v2.base.loading.LoadingEventHelper
import com.nmm.chart.v2.base.loading.XpxLoadingProgress
import com.nmm.chart.v2.base.loading.XpxNormalLoadingAdapter
import com.nmm.chart.v2.base.net.XpxBaseViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * create by zhusw on 6/7/21 15:24
 */
abstract class BaseActivityV2 : BaseFragmentManagerActivity() {
    override fun initViewModel() {
    }

    override fun beforeSuperOnCreate(savedInstanceState: Bundle?) {

    }

    override fun afterSuperOnCreate(savedInstanceState: Bundle?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (closeReBuild()) {
            super.onCreate(null)
        } else {
            super.onCreate(savedInstanceState)
        }

        if (requestedOrientation < 0) { //必须配置一个屏幕属性
            throw RuntimeException("必须在xml中配置android:screenOrientation=\"...\"")
        }
        if (window.attributes.softInputMode <= 0) { //没有就默认隐藏键盘,有就跳过
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (!closeReBuild()) {
            super.onRestoreInstanceState(savedInstanceState)
        }
    }

    /**
     * 是否关闭重建，默认为true： 关闭
     * @return
     */
    protected fun closeReBuild(): Boolean = true

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // 从找工人完善的新版api
    /////////////////////////////////////////////////////////////////////////////////////////////////

    fun bindInitLoadingEvent(
        vm: XpxBaseViewModel,
        loadingView: IPageLoading,
        adapter: PageLoadingAdapter = XpxNormalLoadingAdapter(),
        onClickRetry: () -> Unit
    ) {
        bindInitLoadingEvent(
            vm.initLoadingLiveData,
            loadingView,
            adapter,
            onClickRetry
        )
    }


    /**
     * @param onClickRetry 点击重试
     */
    fun bindInitLoadingEvent(
        initLiveData: LiveData<XpxLoadingProgress>,
        loadingView: IPageLoading,
        adapter: PageLoadingAdapter = XpxNormalLoadingAdapter(),
        onClickRetry: (() -> Unit)? = null
    ) {
        LoadingEventHelper.bindLoadingEvent(
            owner = this,
            liveData = initLiveData,
            adapter = adapter,
            loadingView = loadingView,
            onClickRetry = onClickRetry
        )
    }

    /**
     * 多liveData绑定loading（一般为全局loading）
     * 由于是多接口，所有只有loading、success、failure三种情况，empty归并到成功里
     * @param loadingView 单独的view，不要共用其他loading
     */
    fun bindMultiLoadingEvent(
        vararg liveDatas: LiveData<XpxLoadingProgress>,
        loadingView: IPageLoading,
        adapter: PageLoadingAdapter = XpxNormalLoadingAdapter(),
        onClickRetry: (() -> Unit)? = null
    ) {
        LoadingEventHelper.bindMultiLoadingEvent(
            owner = this,
            liveDatas = liveDatas,
            adapter = adapter,
            loadingView = loadingView,
            onClickRetry = onClickRetry
        )
    }

    /**
     * 默认使用vm的
     */
    fun bindRefreshLoadingEvent(
        vm: XpxBaseViewModel,
        refreshLayout: SmartRefreshLayout
    ) {
        bindRefreshLoadingEvent(vm.refreshLoadingLiveData, refreshLayout)
    }

    fun bindRefreshLoadingEvent(
        refreshLiveData: LiveData<XpxLoadingProgress>,
        refreshLayout: SmartRefreshLayout
    ) {
        LoadingEventHelper.bindRefreshLoadingEvent(this, refreshLiveData, refreshLayout)
    }

    fun bindInitAndRefreshLoadingEvent(
        vm: XpxBaseViewModel,
        loadingView: IPageLoading,
        refreshLayout: SmartRefreshLayout,
        adapter: PageLoadingAdapter = XpxNormalLoadingAdapter(),
        onClickRetry: (() -> Unit)? = null
    ) {
        bindInitAndRefreshLoadingEvent(
            vm.initLoadingLiveData,
            vm.refreshLoadingLiveData,
            loadingView,
            refreshLayout,
            adapter,
            onClickRetry
        )
    }

    fun bindInitAndRefreshLoadingEvent(
        initLiveData: LiveData<XpxLoadingProgress>,
        refreshLiveData: LiveData<XpxLoadingProgress>,
        loadingView: IPageLoading,
        refreshLayout: SmartRefreshLayout,
        adapter: PageLoadingAdapter = XpxNormalLoadingAdapter(),
        onClickRetry: (() -> Unit)? = null
    ) {
        LoadingEventHelper.bindInitAndRefreshLoadingEvent(
            this,
            initLiveData,
            refreshLiveData,
            loadingView,
            refreshLayout,
            adapter,
            onClickRetry
        )
    }

    /**
     * 默认使用vm的
     */
    fun bindLoadMoreLoadingEvent(
        vm: XpxBaseViewModel,
        refreshLayout: SmartRefreshLayout
    ) {
        bindLoadMoreLoadingEvent(vm.loadMoreLoadingLiveData, refreshLayout)
    }

    fun bindLoadMoreLoadingEvent(
        loadMoreLiveData: LiveData<XpxLoadingProgress>,
        refreshLayout: SmartRefreshLayout
    ) {
        LoadingEventHelper.bindLoadMoreLoadingEvent(this, loadMoreLiveData, refreshLayout)
    }

    fun bindPageEvent(vm: XpxBaseViewModel, page: MjPage) {
        bindPageEvent(vm.initLoadingLiveData, vm.loadMoreLoadingLiveData, page)
    }

    /**
     * 绑定分页逻辑，可以回退和重新init
     */
    fun bindPageEvent(
        initLiveData: LiveData<XpxLoadingProgress>,
        loadMoreLiveData: LiveData<XpxLoadingProgress>,
        page: MjPage
    ) {
        LoadingEventHelper.bindPageEvent(this, initLiveData, loadMoreLiveData, page)
    }
}