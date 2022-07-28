package com.dyn.net.api

import com.blankj.utilcode.util.NetworkUtils
import com.dyn.net.api.errorhandler.ApiDataHandler
import com.dyn.net.api.errorhandler.HttpErrorHandler
import com.dyn.net.api.interceptor.RequestInterceptor
import com.dyn.net.api.utils.GsonFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

abstract class ApiBase<S> {
    private val apiClazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<S>
    private val mRetrofit by lazy {
        Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(getBaseUrl())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonFactory.gson))
            .build()
    }

    val mService: S by lazy {
        mRetrofit.create(apiClazz)
    }

    companion object {
        private const val CONNECT_TIME_OUT_SECOND = 10L
        private const val READ_TIME_OUT_SECOND = 10L
        private const val WRITE_TIME_OUT_SECOND = 10L
        private val mErrorTransformer = ErrorTransformer<Any?>()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(getConnectTimeOutBySecond(), TimeUnit.SECONDS)
            .readTimeout(getReadTimeOutBySecond(), TimeUnit.SECONDS)
            .writeTimeout(getWriteTimeOutBySecond(), TimeUnit.SECONDS)
        getRequestInterceptor()?.let {
            clientBuilder.addInterceptor(it)
        }

        getNetInterceptor()?.let {
            clientBuilder.addNetworkInterceptor(it)
        }

        /**  socketFactory  start*/
        var trustManagerFactory: TrustManagerFactory? = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory!!.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + Arrays.toString(
                trustManagers
            )
        }
        val trustManager = trustManagers[0] as X509TrustManager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustManager), null)
        val sslSocketFactory: SSLSocketFactory = sslContext.getSocketFactory()
        clientBuilder.sslSocketFactory(sslSocketFactory,trustManager)
        /**  socketFactory  end*/
        setLoggingLevel(clientBuilder)
        val okHttpClient = clientBuilder.build()
        okHttpClient.dispatcher.maxRequestsPerHost = 20
        return okHttpClient
    }

    private fun setLoggingLevel(builder: OkHttpClient.Builder) {
        val logInterceptor = HttpLoggingInterceptor()
        var isDebug = false
        getRequestInterceptor()?.let {
            isDebug = it.mRequestInfo.isDebug()
        }
        logInterceptor.level =
            if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        builder.addInterceptor(logInterceptor)
    }

    protected open fun getConnectTimeOutBySecond(): Long {
        return CONNECT_TIME_OUT_SECOND
    }

    protected open fun getReadTimeOutBySecond(): Long {
        return READ_TIME_OUT_SECOND
    }

    protected open fun getWriteTimeOutBySecond(): Long {
        return WRITE_TIME_OUT_SECOND
    }

    fun getInterceptors(): MutableList<Interceptor> {
        return mutableListOf()
    }

    protected open fun getRequestInterceptor(): RequestInterceptor? {
        return null
    }

    protected open fun getNetInterceptor(): Interceptor? {
        return null
    }

    abstract fun getBaseUrl(): String

    fun <T> executeSubscribe(observable: Observable<T?>, observer: Observer<T?>) {
        observable.doOnSubscribe(checkNetConsumer)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .compose(mErrorTransformer)
            .map {
                it as T
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

    /**
     * 对请求的 Observable 封装一下 再返回出去
     * */
    fun <T> wrapBaseObservable(observable: Observable<T>): Observable<T> {
        return observable.doOnSubscribe(checkNetConsumer)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .compose(mErrorTransformer)
            .map {
                it as T
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    class ErrorTransformer<T> : ObservableTransformer<T, T> {
        override fun apply(upstream: Observable<T>): ObservableSource<T> {
            return upstream.map(ApiDataHandler()).onErrorResumeNext(HttpErrorHandler<T>())
        }
    }

    private val checkNetConsumer = Consumer<Disposable> { t ->
        if (NetworkUtils.isAvailable().not()) {
            throw ServerException(-1, "请检查网络连接")
            t?.dispose()
        }
    }

//
//    open fun <T> applySchedulers(observer: Observer<T>?): ObservableTransformer<T, T> {
//        return ObservableTransformer<T, T> { upstream ->
//            val observable: Observable<T> = upstream
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .map(getAppErrorHandler())
//                    .onErrorResumeNext(HttpErrorHandler<T>()) as Observable<T>
//            observable.subscribe(observer)
//            observable
//        }
//    }
//
//    protected abstract fun <T> getAppErrorHandler(): Function<T, T>
}

