package com.sakura.chat.v2.base.component

import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import com.foundation.app.arc.fragment.BaseViewBinding2Fragment
import com.foundation.widget.loading.IPageLoading
import com.foundation.widget.loading.PageLoadingAdapter
import com.foundation.widget.utils.other.MjPage
import com.sakura.chat.v2.base.loading.LoadingEventHelper
import com.sakura.chat.v2.base.loading.LoadingProgress
import com.sakura.chat.v2.base.loading.NormalLoadingAdapter
import com.sakura.chat.v2.base.net.BaseViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 *
 *create by zhusw on 5/18/21 18:38
 */
abstract class BaseFragmentV2(@LayoutRes id: Int) : BaseViewBinding2Fragment(id) {
    override fun initViewModel() {

    }

    /**
     * 注意：如果涉及嵌套fragment，父类必须仍是[BaseFragmentV2]，不能混用BaseProgressFragmentWrap的
     */
    override fun onVisible(isFirst: Boolean) {

    }

    override fun onHidden() {

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // 从找工人完善的新版api
    /////////////////////////////////////////////////////////////////////////////////////////////////

    fun bindInitLoadingEvent(
        vm: BaseViewModel,
        loadingView: IPageLoading,
        adapter: PageLoadingAdapter = NormalLoadingAdapter(),
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
        initLiveData: LiveData<LoadingProgress>,
        loadingView: IPageLoading,
        adapter: PageLoadingAdapter = NormalLoadingAdapter(),
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
        vararg liveDatas: LiveData<LoadingProgress>,
        loadingView: IPageLoading,
        adapter: PageLoadingAdapter = NormalLoadingAdapter(),
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
        vm: BaseViewModel,
        refreshLayout: SmartRefreshLayout
    ) {
        bindRefreshLoadingEvent(vm.refreshLoadingLiveData, refreshLayout)
    }

    fun bindRefreshLoadingEvent(
        refreshLiveData: LiveData<LoadingProgress>,
        refreshLayout: SmartRefreshLayout
    ) {
        LoadingEventHelper.bindRefreshLoadingEvent(this, refreshLiveData, refreshLayout)
    }

    fun bindInitAndRefreshLoadingEvent(
        vm: BaseViewModel,
        loadingView: IPageLoading,
        refreshLayout: SmartRefreshLayout,
        adapter: PageLoadingAdapter = NormalLoadingAdapter(),
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
        initLiveData: LiveData<LoadingProgress>,
        refreshLiveData: LiveData<LoadingProgress>,
        loadingView: IPageLoading,
        refreshLayout: SmartRefreshLayout,
        adapter: PageLoadingAdapter = NormalLoadingAdapter(),
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
        vm: BaseViewModel,
        refreshLayout: SmartRefreshLayout
    ) {
        bindLoadMoreLoadingEvent(vm.loadMoreLoadingLiveData, refreshLayout)
    }

    fun bindLoadMoreLoadingEvent(
        loadMoreLiveData: LiveData<LoadingProgress>,
        refreshLayout: SmartRefreshLayout
    ) {
        LoadingEventHelper.bindLoadMoreLoadingEvent(this, loadMoreLiveData, refreshLayout)
    }

    fun bindPageEvent(vm: BaseViewModel, page: MjPage) {
        bindPageEvent(vm.initLoadingLiveData, vm.loadMoreLoadingLiveData, page)
    }

    /**
     * 绑定分页逻辑，可以回退和重新init
     */
    fun bindPageEvent(
        initLiveData: LiveData<LoadingProgress>,
        loadMoreLiveData: LiveData<LoadingProgress>,
        page: MjPage
    ) {
        LoadingEventHelper.bindPageEvent(this, initLiveData, loadMoreLiveData, page)
    }
}