package com.dyn.webview.mainprocess

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.dyn.webview.IBinderPool
import com.orhanobut.logger.Logger
import java.util.concurrent.CountDownLatch

class RemoteWebBinderPool private constructor(private val mContext: Context) {
    /**
     * Android 使用CountDownLatch 实现多线程同步
     * 自我理解  确保只绑定一次服务
     * */
    private var mConnectBinderPoolCountDownLatch: CountDownLatch? = null

    /**
     * 子进程拿到主进程提供的 查找binder 的 IBinderPool 的服务
     * */
    private var mBinderPool: IBinderPool? = null
    private var mBinderPoolConnection: ServiceConnection
    private var mBinderPoolDeathRecipient: IBinder.DeathRecipient //Binder 死亡的监听

    companion object {
        const val BINDER_WEB_AIDL = 1
        private var instance: RemoteWebBinderPool? = null
        fun newInstance(context: Context) = lazy {
            if (instance == null) {
                instance = RemoteWebBinderPool(context)
            }
            instance!!
        }.value
    }

    init {
        /**
         * 当Binder死亡的时候系统会回调binderDied方法
         * */
        mBinderPoolDeathRecipient = object : IBinder.DeathRecipient {
            // 6
            override fun binderDied() {
                mBinderPool?.asBinder()?.unlinkToDeath(this, 0)
                mBinderPool = null
                Logger.i("服务binderDied------------------")
                connectToMainProcessService()
            }
        }
        /**
         * 子进程绑定主进程后的 Connection
         * */
        mBinderPoolConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                try {
                    Logger.i("服务连接成功------------------")
                    mBinderPool = IBinderPool.Stub.asInterface(service)
                    mBinderPool?.asBinder()?.linkToDeath(mBinderPoolDeathRecipient, 0)//监听进程消失时候的回调
                    mConnectBinderPoolCountDownLatch?.countDown()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onBindingDied(name: ComponentName?) {
                super.onBindingDied(name)
                Logger.i("服务onBindingDied------------------")
            }

        }
        connectToMainProcessService()
    }

    /**
     * 通过code查找一个IBinder服务
     * */
    fun queryBinder(binderCode: Int): IBinder? {
        var binder: IBinder? = null
        try {
            Logger.i("queryBinder------------$binderCode------")
            binder = mBinderPool?.queryBinder(binderCode)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        return binder
    }


    /**
     * 绑定 启动主进程的服务
     * */
    @Synchronized
    private fun connectToMainProcessService() {
        mConnectBinderPoolCountDownLatch = CountDownLatch(1)
        val intent = Intent(mContext, MainProHandleRemoteService::class.java)
        mContext?.bindService(intent, mBinderPoolConnection, Context.BIND_AUTO_CREATE)
        try {
            mConnectBinderPoolCountDownLatch?.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 主进程提供给子进程的代理
     * */
    class BinderPoolImpl(private val mContext: Context) : IBinderPool.Stub() {
        override fun queryBinder(binderCode: Int): IBinder? {
            Logger.i("queryBinder------------$binderCode------")
            when (binderCode) {
                BINDER_WEB_AIDL -> {
                    return MainProcessServiceManager.getInstance(mContext)
                }
            }
            return null
        }

    }
}