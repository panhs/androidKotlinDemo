package unioncast.cn.alllibsdemo

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_news_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.UI
import org.jetbrains.anko.warn
import retrofit2.Call
import retrofit2.Response
import unioncast.cn.alllibsdemo.di.module.network.IRetrofit
import unioncast.cn.alllibsdemo.modal.domain.NewsInfoResponse
import unioncast.cn.alllibsdemo.util.HtmlUtil
import unioncast.cn.alllibsdemo.util.kt.forNet
import unioncast.cn.alllibsdemo.util.kt.setToolBar
import javax.inject.Inject

class NewsDetailActivity : DaggerAppCompatActivity(), AnkoLogger {
    @Inject
    lateinit var retrofit: IRetrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        setToolBar(toolbar, 0, "详情页面", true)
        val showTitle = intent.getBooleanExtra("showTitle", false)
        warn("showTitle $showTitle")
        val longExtra = intent.getLongExtra("id", 9682447)
        retrofit.getNewsDetail(longExtra)
                .forNet()
                .subscribe({
                    toolbar.title = it.title
                    val body = HtmlUtil.createHtmlData(showTitle, it.css, it.js, it.body)
                    webview?.loadDataWithBaseURL("", body, "text/html", "utf-8", null)
                }, { warn("getNewsDetail error$it.message ") })

    }
}
