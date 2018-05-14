package unioncast.cn.alllibsdemo.modal.domain

import unioncast.cn.alllibsdemo.NewsItemRecyclerViewAdapter

data class TitleStory constructor(val date: String) : IStory {
    override fun getGroupTitle(): String = date

    override fun getType(): NewsItemRecyclerViewAdapter.ViewType {
        return NewsItemRecyclerViewAdapter.ViewType.TITLE
    }
}