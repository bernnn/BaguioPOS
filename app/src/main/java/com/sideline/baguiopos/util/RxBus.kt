package com.sideline.baguiopos.util

import android.os.Handler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

object RxBus {

    val subject = PublishSubject.create<Any>()
    val disposables = mutableMapOf<Any, CompositeDisposable>()

    inline fun <reified T : Any> subscribe(subscriber: Any, noinline observer: (T) -> Unit) {
        val disposable = disposables[subscriber]
                ?: CompositeDisposable().apply { disposables[subscriber] = this }
        disposable.add(subject.ofType(T::class.java).subscribe(observer))
    }

    fun unsubscribe(subscriber: Any) {
        disposables[subscriber]?.clear()
        disposables.remove(subscriber)
    }

    fun unsubscribe(subscriber: Any, callback: () -> Unit) {
        disposables[subscriber]?.clear()
        disposables.remove(subscriber)
        callback()
    }

    fun unsubscribe(subscriber: Any, callback: () -> Unit, delay: Long) {
        disposables[subscriber]?.clear()
        disposables.remove(subscriber)
        Handler().postDelayed(callback, delay)
    }

    fun post(item: Any) = subject.onNext(item)


}