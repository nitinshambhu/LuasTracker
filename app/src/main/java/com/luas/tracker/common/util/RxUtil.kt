package com.luas.tracker.common.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers


fun <T> Single<T>.applySingleSchedulers(): Single<T> {
    return this.compose { single ->
        single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}