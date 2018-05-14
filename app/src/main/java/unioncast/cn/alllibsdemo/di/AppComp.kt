package unioncast.cn.alllibsdemo.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import unioncast.cn.alllibsdemo.App
import unioncast.cn.alllibsdemo.di.module.network.IRetrofit
import unioncast.cn.alllibsdemo.di.subComponent.ActivityBindModule
import dagger.android.support.AndroidSupportInjectionModule as AM

@Component(modules = [(AM::class),
    (ActivityBindModule::class),
    IRetrofit.RetrofitModule::class])
interface AppComp : AndroidInjector<App> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComp
    }
}