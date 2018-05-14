package unioncast.cn.alllibsdemo.di.module.network

import android.util.Log
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import unioncast.cn.alllibsdemo.modal.domain.NewsInfoResponse
import unioncast.cn.alllibsdemo.modal.domain.StoriesResponse
import unioncast.cn.alllibsdemo.modal.domain.ThemeEntities
import java.lang.reflect.Type
import javax.inject.Singleton

val BASE_URL = "http://news-at.zhihu.com/"

public interface IRetrofit {

    @GET("api/4/news/latest")
    fun getLatestNews(): Observable<StoriesResponse>

    @GET("api/4/news/before/{date}")
    fun getBeforeNews(@Path("date") date: Long): Observable<StoriesResponse>

    @GET("api/4/theme/{id}")
    fun getThemeNews(@Path("id") id: Long): Observable<StoriesResponse>

    @GET("api/4/news/{id}")
    fun getNewsDetail(@Path("id") id: Long): Observable<NewsInfoResponse>


    @GET("api/4/themes")
    fun getTheme(): Observable<ThemeEntities>


    @Singleton
    @Module
    class RetrofitModule {
        @Provides
        fun providesIRetrofit(): IRetrofit {
            val builder = OkHttpClient.Builder().addInterceptor { chain ->
                //Log.w(TAG, "chain.request().body() $chain")
                val newBuilder = chain.request().newBuilder()
                newBuilder.addHeader("Content-Type", "application/json;charset=UTF-8")
                val mRequest = newBuilder.build()
                chain.proceed(mRequest)
            }
            val retrofit = Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(BASE_URL)
                    //.addCallAdapterFactory(ObserveOnMainCallAdapterFactory(AndroidSchedulers.mainThread()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val iRetrofit = retrofit.create(IRetrofit::class.java)
            return iRetrofit
        }
    }

}