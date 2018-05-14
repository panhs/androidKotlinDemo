package unioncast.cn.alllibsdemo.di.module.room

import android.content.Context
import com.github.jokar.zhihudaily.room.AppDatabaseHelper
import dagger.Module
import dagger.Provides

@Module
class AppDatabaseModule(var context: Context) {

    @Provides
    fun dataBaseProvider(): AppDatabaseHelper {
        return AppDatabaseHelper.getInstance(context)
    }
}