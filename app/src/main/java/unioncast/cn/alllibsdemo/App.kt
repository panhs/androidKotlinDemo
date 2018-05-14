package unioncast.cn.alllibsdemo

import android.util.Log
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import unioncast.cn.alllibsdemo.di.DaggerAppComp
import unioncast.cn.alllibsdemo.di.module.network.IRetrofit

class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComp.builder().application(this).build();
    }

    override fun onCreate() {
        super.onCreate()
    }
}
