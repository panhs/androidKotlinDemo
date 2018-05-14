package unioncast.cn.alllibsdemo.di.subComponent

import dagger.Module
import dagger.android.ContributesAndroidInjector
import unioncast.cn.alllibsdemo.HomeFragment

@Module
abstract class HomeFragmentModule {
    @ContributesAndroidInjector
    abstract fun homeFragment(): HomeFragment

}
