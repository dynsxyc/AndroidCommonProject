package com.dyn.base.mvvm.model

import com.dyn.base.ui.base.AutoDisposeLifecycleScopeProvider
import com.dyn.base.ui.base.BaseApplication
import com.dyn.base.utils.GsonFactory
import com.dyn.base.utils.PreferenceExt
import org.json.JSONObject
import java.lang.reflect.ParameterizedType

/**
 * 数据模型model  每个请求对应一个model
 * @param NET_DATA 网络返回数据模型
 * @param RESULT_DATA 页面加载需要的数据模型
 * */
abstract class BaseModel<NET_DATA, RESULT_DATA> : INetDataObserver<NET_DATA> {
    /**
     * rx AutoDispose 使用
     * */
    protected open val autoDisposeLifecycleScopeProvider = AutoDisposeLifecycleScopeProvider()
    private val netDataClazz =  (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<NET_DATA>

    /**
     * 给ViewModel 的回调
     * */
//    private var mNetCallbackListener: WeakReference<IBaseModelListener<RESULT_DATA>>? = null
    private var mToViewModelCallbackListener: IBaseModelListener<RESULT_DATA>? = null
    private var mPage: Int = 0 //分页时当前页码
    private var mIsLoading: Boolean = false //是否正在加载
    private var mIsPaging: Boolean //是否分页 默认false
    private val INIT_PAGE_NUMSER: Int

    //缓存数据
    private var mCacheData by PreferenceExt(
        BaseApplication.AppContext, getCachePreferenceKey()
            ?: "empty_cache_key", ""
    )

    init {
        mIsPaging = isPaging()
        getInitPageNumber().also {
            mPage = it
            INIT_PAGE_NUMSER = it
        }
    }

    /**
     * 当前数据模型是否是分页的 如果是分页返回true
     * */
    open fun isPaging(): Boolean {
        return false
    }

    /**
     * 第一页初始位置，默认从0开始
     * */
    protected open fun getInitPageNumber(): Int {
        return 1
    }

    /**
     * 注册ViewModel监听回调
     * */
    fun registerListener(callbackListener: IBaseModelListener<RESULT_DATA>) {
        mToViewModelCallbackListener = callbackListener
    }

    /**
     * 刷新，如果是分页数据，将page置为初始位置 重新加载
     * */
    fun refresh() {
        if (mIsLoading.not()) {
            mIsLoading = true
            if (mIsPaging) {
                mPage = INIT_PAGE_NUMSER
            }
            load()
        }
    }

    /**
     * 加载下一页
     * */
    fun loadNextPage() {
        if (mIsLoading.not()) {
            mIsLoading = true
            load()
        }
    }

    /**
     * 获取缓存，并load
     * */
    fun getCachedDataAndLoad() {
        if (mIsLoading.not()) {
            mIsLoading = true
            try {
                if (mIsPaging) {
                    mPage = INIT_PAGE_NUMSER
                }
                if (getCachePreferenceKey().isNullOrBlank().not()) {
                    //有缓存的key
                    var cacheData = mCacheData
                    if (cacheData.isNullOrBlank().not()) {
                        //缓存不为空,解析缓存
                        val jsonObject = JSONObject(cacheData)
                        val netObject =
                            GsonFactory.gson.fromJson(jsonObject.optString("data"), netDataClazz)
                        val cacheTime = jsonObject.optLong("upDateTime")
                        if (netObject != null){
                            onSuccess(netObject, true)
                        }
                        if (isNeedToUpdate(cacheTime) || netObject == null) {
                            load()
                        }
                        return
                    }
                    if (getApkPredefineData().isNullOrBlank().not()) {
                        //有预存数据
                        val netObject =
                            GsonFactory.gson.fromJson(getApkPredefineData(), netDataClazz)
                        onSuccess(netObject, true)
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            load()
        }
    }

    /**
     * 获取APK预存数据
     *
     * */
    protected open fun getApkPredefineData(): String? {
        return null
    }

    /**
     * 判断缓存是否在有效期内
     * @return true 通过cacheTime 判断缓存是否过期 需不需要重新load  默认需要
     * */
    protected open fun isNeedToUpdate(cacheTime: Long): Boolean {
        return true
    }

    /**
     * 真正的加载操作，给具体的model去实现
     * */
    abstract fun load()

    /**
     * 数据返回成功  回调通知到viewModel 层
     * */
    protected open fun notifySuccessToListener(
        netData: NET_DATA,
        resultData: RESULT_DATA?,
        isFromCache: Boolean
    ) {
        if (mToViewModelCallbackListener == null) {
            throw IllegalArgumentException("请先注册回调监听")
        }
        mToViewModelCallbackListener?.let {
            //notify
            if (mIsPaging) {
                val isEmpty = if (resultData is List<*>) {
                    resultData.isNullOrEmpty()
                } else {
                    true
                }
                val hasMore = hasMoreData(netData, resultData)
                //pageNumber
                it.onSuccess(
                    this,
                    resultData,
                    ResultPageInfo(mPage == INIT_PAGE_NUMSER, isEmpty, hasMore && isFromCache.not())
                )
                //saveDataToPreference
                if (getCachePreferenceKey().isNullOrBlank().not() && mPage == INIT_PAGE_NUMSER && isFromCache.not()
                ) {
                    saveDataToPreference(netData)
                }
                if (hasMore && isFromCache.not()) {//有更多  并且不是缓存
                    mPage++
                }
            } else {
                it.onSuccess(this, resultData)
                //saveDataToPreference
                if (getCachePreferenceKey().isNullOrBlank().not() && isFromCache.not()) {
                    saveDataToPreference(netData)
                }
            }
        }
        mIsLoading = false
    }

    protected open fun hasMoreData(netData: NET_DATA, resultData: RESULT_DATA?): Boolean {
        return if (resultData is List<*>) {
            resultData.isNotEmpty()
        } else {
            false
        }
    }

    /**
     * 加载失败 统一回调处理 通知到viewModel 层
     * */
    protected open fun notifyFailToListener(message: Throwable) {
        if (mToViewModelCallbackListener == null) {
            throw IllegalArgumentException("请先注册回调监听")
        }
        mToViewModelCallbackListener?.let {
            if (mIsPaging) {
                it.onFail(this, message, ResultPageInfo(mPage == INIT_PAGE_NUMSER, true))
            } else {
                it.onFail(this, message)
            }
        }
        mIsLoading = false
    }

    /**
     * 保存数据到缓存，默认使用的是SharedPreferences 保存json 数据
     * */
    private fun saveDataToPreference(netData: NET_DATA) {
        val baseCachedData = BaseCachedData(System.currentTimeMillis(), netData)
        mCacheData = GsonFactory.gson.toJson(baseCachedData)
    }

    /**
     * 缓存 sp 对应的key
     * */
    protected open fun getCachePreferenceKey(): String? {
        return null
    }

    /**
     * rx java autoDispose 使用的provider clear操作
     * */
    fun onClear() {
        autoDisposeLifecycleScopeProvider.onCleared()
    }

    fun getCurrentPage(): Int {
        return mPage
    }

}