package unioncast.cn.alllibsdemo.di.subComponent

import dagger.Module
import dagger.android.ContributesAndroidInjector
import unioncast.cn.alllibsdemo.MainActivity
import unioncast.cn.alllibsdemo.NewsDetailActivity

@Module()
abstract class ActivityBindModule {

    @ContributesAndroidInjector(modules = [(HomeFragmentModule::class), ThemeNewsFragmentModule::class])
    abstract fun mainActivity(): MainActivity


    @ContributesAndroidInjector
    abstract fun newsDetailActivity(): NewsDetailActivity


}


