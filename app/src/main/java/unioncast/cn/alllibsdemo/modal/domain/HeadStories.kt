package unioncast.cn.alllibsdemo.modal.domain

import unioncast.cn.alllibsdemo.NewsItemRecyclerViewAdapter

data class HeadStories(val top_stories: List<TopStory>) : IStory {
    override fun getGroupTitle(): String = "首页"

    override fun getType(): NewsItemRecyclerViewAdapter.ViewType =
            NewsItemRecyclerViewAdapter.ViewType.HEAD
}