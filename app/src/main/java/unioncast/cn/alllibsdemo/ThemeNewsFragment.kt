package unioncast.cn.alllibsdemo


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import unioncast.cn.alllibsdemo.di.module.network.IRetrofit
import unioncast.cn.alllibsdemo.util.kt.forNet
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 *
 */
class ThemeNewsFragment @Inject constructor() : DaggerFragment(), AnkoLogger {

    private var listener: OnListFragmentInteractionListener? = null
    @Inject
    lateinit var retrofit: IRetrofit
    private lateinit var adapter: NewsItemRecyclerViewAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        // Set the adapter
        if (view is RecyclerView) {
            view.adapter = adapter
            with(view) {
                layoutManager = LinearLayoutManager(context)
                getListAndShow()
            }
        }
        return view
    }

    fun getListAndShow(id: Long = 4) {
        retrofit.getThemeNews(id)
                .forNet()
                .subscribe({
                    adapter.addAll(it.stories)
                }, { warn("getThemeNews error ${it.message}") })
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
        listener = null
    }
}
