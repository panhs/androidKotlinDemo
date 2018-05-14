package unioncast.cn.alllibsdemo.modal.domain

import unioncast.cn.alllibsdemo.NewsItemRecyclerViewAdapter

data class Story constructor(
        val images: ArrayList<String>,
        val type: Long,
        val id: Long,
        val ga_prefix: String,
        val title: String, var date: String?) : IStory {

    override fun getGroupTitle(): String = date?.toString() ?: ""

    override fun getType(): NewsItemRecyclerViewAdapter.ViewType {
        return NewsItemRecyclerViewAdapter.ViewType.COMMON
    }

}

