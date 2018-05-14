package unioncast.cn.alllibsdemo


import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_item.view.*
import kotlinx.android.synthetic.main.layout_head.view.*
import kotlinx.android.synthetic.main.layout_text.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.startActivity
import unioncast.cn.alllibsdemo.modal.domain.*

/**
 */
class NewsItemRecyclerViewAdapter(
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AnkoLogger {

    private val mValues: MutableList<IStory> = ArrayList()

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Story
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        debug("viewType $viewType")
        return when (viewType) {
            ViewType.COMMON.ordinal -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_item, parent, false))
            ViewType.HEAD.ordinal -> HeadViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_head, parent, false))
            else -> TitleViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_text, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        debug("holder $holder")
        when (holder) {
            is HeadViewHolder -> {
                val item = mValues[position] as HeadStories
                holder.viewPager.adapter = HeadViewPageAdapter(item.top_stories)
            }
            is TitleViewHolder -> {
                val item = mValues[position] as TitleStory
                holder.groupTitle.text = item.date.toString()

            }
            is ViewHolder -> {
                val item = mValues[position] as Story
                holder.mIdView.text = item.title
                if (item.images != null && item.images.size > 0) {
                    holder.mContentView.visibility = View.VISIBLE
                    Glide.with(holder.mContentView.context)
                            .load(item.images[0])
                            .into(holder.mContentView)
                } else holder.mContentView.visibility = View.GONE


                with(holder.mView) {
                    tag = item
                    setOnClickListener(mOnClickListener)
                }
            }
        }
    }

    enum class ViewType() {
        HEAD, TITLE, COMMON
    }

    fun getItem(position: Int): IStory? {
        return try {
            mValues[position]
        } catch (e: Exception) {
            null
        }
    }

    override fun getItemCount(): Int = mValues.size

    override fun getItemViewType(position: Int): Int {
        return mValues[position].getType().ordinal
    }

    inner class HeadViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val viewPager: ViewPager = mView.viewpager_head

    }

    inner class TitleViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val groupTitle: TextView = mView.textview

    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_title
        val mContentView: ImageView = mView.content

    }

    fun addAll(list: MutableList<out IStory>) {
        mValues.addAll(list)
        notifyDataSetChanged()
    }

    fun add(story: IStory) {
        mValues.add(story)
        notifyDataSetChanged()
    }

    class HeadViewPageAdapter constructor(private val images: List<TopStory>) : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, pos: Int): Any {
            val imageView = ImageView(container.context)
            imageView.setOnClickListener {
                container.context.startActivity<NewsDetailActivity>(
                        "id" to images[pos].id, "showTitle" to false)

            }
            imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            Glide.with(container.context).load(images[pos].image).into(imageView)
            // imageView.setImageResource(R.mipmap.ic_launcher)
            container.addView(imageView)
            return imageView
        }

        override fun isViewFromObject(view: View, arg2: Any): Boolean = view == arg2

        override fun getCount(): Int = images.size

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            //super.destroyItem(container, position, `object`)
            val child = container.getChildAt(position)
            if (child != null)
                container.removeView(child)
        }
    }

    fun getDateTitle(pos: Int): String =""

}
