package unioncast.cn.alllibsdemo.modal.domain

import unioncast.cn.alllibsdemo.NewsItemRecyclerViewAdapter

interface IStory {
    fun getType(): NewsItemRecyclerViewAdapter.ViewType
    fun getGroupTitle(): String
}