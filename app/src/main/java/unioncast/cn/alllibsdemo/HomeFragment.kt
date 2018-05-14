package unioncast.cn.alllibsdemo

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import unioncast.cn.alllibsdemo.di.module.network.IRetrofit
import unioncast.cn.alllibsdemo.modal.domain.HeadStories
import unioncast.cn.alllibsdemo.modal.domain.Story
import unioncast.cn.alllibsdemo.modal.domain.TitleStory
import unioncast.cn.alllibsdemo.util.kt.forNet
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import unioncast.cn.alllibsdemo.NewsItemRecyclerViewAdapter.ViewType.TITLE as TYPE_TITLE

/**
 * A fragment representing a list of Items.
 */
class HomeFragment @Inject
constructor() : DaggerFragment(), AnkoLogger {
    @Inject
    lateinit var retrofit: IRetrofit


    private lateinit var listener: OnListFragmentInteractionListener

    private lateinit var adapter: NewsItemRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            view.adapter = adapter
            view.addOnScrollListener(scrollListener)
            with(view) {
                layoutManager = LinearLayoutManager(context)
                loadNews(0)

            }
        }
        return view
    }

    /**
     * 参数优化为用毫秒数
     */
    private fun loadNews(time: Long) {
        val paramDate = formater.format(Date(time)).toLong()
        retrofit.run {
            val observable = if (time <= 0) getLatestNews() else getBeforeNews(paramDate)
            observable.forNet().subscribe({
                val showDate = showFormat.format(Date(time - 1000 * 60 * 60 * 24))
                val dateStr = if (time <= 0) "今日热闻" else showDate
                if (time > 0) daysBefore += 1 else adapter.add(HeadStories(it.top_stories))
                adapter.add(TitleStory(dateStr))
                with(it.stories) {
                    if (size > 0) {
                        this[it.stories.size - 1].date = dateStr
                        adapter.addAll(this)
                    }
                }
            }, { warn { "error is ${it.message}" } })

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
            adapter = NewsItemRecyclerViewAdapter(listener)
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    var daysBefore: Long = 0
    private val formater: SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
    private val showFormat: SimpleDateFormat = SimpleDateFormat("MM月dd日")
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val lastVisiblePos = layoutManager.findLastVisibleItemPosition()
            val itemCount = layoutManager.itemCount
            warn("onScrollStateChanged lastVisiblePos $lastVisiblePos newState $newState ")
            if (lastVisiblePos == itemCount - 1 && newState == SCROLL_STATE_IDLE) {
                val time = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * daysBefore
                warn("time time is $time")
                loadNews(time)

            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val lm: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisiblePos = lm.findFirstVisibleItemPosition()
            val nextPos = firstVisiblePos + 1
            val viewType = adapter.getItemViewType(firstVisiblePos)
            val nextViewType = adapter.getItemViewType(nextPos)

            if (viewType == TYPE_TITLE.ordinal || nextViewType == TYPE_TITLE.ordinal) {
                val item = adapter.getItem(firstVisiblePos)
                listener.visiableGroupTitleChanged(item?.getGroupTitle())
            }

            super.onScrolled(recyclerView, dx, dy)
        }
    }

}
