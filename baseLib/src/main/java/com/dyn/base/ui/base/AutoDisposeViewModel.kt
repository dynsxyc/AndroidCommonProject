package com.dyn.base.ui.base

import androidx.lifecycle.ViewModel
import autodispose2.lifecycle.CorrespondingEventsFunction
import autodispose2.lifecycle.LifecycleEndedException
import autodispose2.lifecycle.LifecycleScopeProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
/**
 * rx 根据viewModel clear 自动化取消的 autoDispose
 * */
abstract class AutoDisposeViewModel :ViewModel(),LifecycleScopeProvider<AutoDisposeViewModel.ViewModelEvent> {
    // Subject backing the auto disposing of subscriptions.
    private val lifecycleEvents = BehaviorSubject.createDefault(ViewModelEvent.CREATED)

    enum class ViewModelEvent{
        CLEARED, CREATED
    }

    override fun lifecycle(): Observable<ViewModelEvent> {
        return lifecycleEvents.hide()
    }

    override fun correspondingEvents(): CorrespondingEventsFunction<ViewModelEvent> {
        return CORRESPONDING_EVENTS
    }

    override fun peekLifecycle(): ViewModelEvent {
        return lifecycleEvents.value
    }

    override fun onCleared() {
        lifecycleEvents.onNext(ViewModelEvent.CLEARED)
        super.onCleared()
    }
    companion object {
        /**
         * Function of current event -> target disposal event. ViewModel has a very simple lifecycle.
         * It is created and then later on cleared. So we only have two events and all subscriptions
         * will only be disposed at [ViewModelEvent.CLEARED].
         */
        private val CORRESPONDING_EVENTS = CorrespondingEventsFunction<ViewModelEvent> { event ->
            when (event) {
                ViewModelEvent.CREATED -> ViewModelEvent.CLEARED
                else -> throw LifecycleEndedException(
                        "Cannot bind to ViewModel lifecycle after onCleared.")
            }
        }
    }

}