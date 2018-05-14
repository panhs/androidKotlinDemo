package unioncast.cn.alllibsdemo.di.component.room

import com.github.jokar.zhihudaily.room.AppDatabaseHelper
import dagger.Component
import unioncast.cn.alllibsdemo.di.module.room.AppDatabaseModule

/**
 * Created by JokAr on 2017/6/30.
 */
@Component(modules = arrayOf(AppDatabaseModule::class))
interface AppDatabaseComponent {
    fun appDataBase(): AppDatabaseHelper
}