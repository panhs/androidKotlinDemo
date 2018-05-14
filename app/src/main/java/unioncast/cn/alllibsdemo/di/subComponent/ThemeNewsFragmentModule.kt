package unioncast.cn.alllibsdemo.di.subComponent

import dagger.Module
import dagger.android.ContributesAndroidInjector
import unioncast.cn.alllibsdemo.ThemeNewsFragment

@Module
abstract class ThemeNewsFragmentModule {

    @ContributesAndroidInjector
    abstract fun themeNewsFragment(): ThemeNewsFragment
}