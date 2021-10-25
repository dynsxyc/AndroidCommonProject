package com.dyn.base.common.sheduler

import com.dyn.base.common.sheduler.SchedulerProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers


class AppSchedulerProvider : SchedulerProvider {
    private constructor() {
    }

    companion object {
        val instance by lazy {
            AppSchedulerProvider()
        }
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }
}

class AppRxSchedulerProvider {
    private constructor() {
    }

    companion object {
        val instance by lazy {
            AppRxSchedulerProvider()
        }
    }

    fun ui(): io.reactivex.Scheduler {
        return io.reactivex.android.schedulers.AndroidSchedulers.mainThread()
    }

    fun io(): io.reactivex.Scheduler {
        return io.reactivex.schedulers.Schedulers.io()
    }
}
