package com.dyn.base.common.sheduler

import io.reactivex.rxjava3.core.Scheduler

/**
 * Created by QingMei on 2017/11/13.
 * desc:
 */

interface SchedulerProvider {

    fun ui(): Scheduler

    fun io(): Scheduler

}
