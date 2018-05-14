package unioncast.cn.alllibsdemo.util.kt

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.forNet(): Observable<T> {
    return observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}