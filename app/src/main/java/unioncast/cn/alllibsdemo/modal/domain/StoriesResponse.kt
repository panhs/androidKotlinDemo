package unioncast.cn.alllibsdemo.modal.domain

data class StoriesResponse constructor(
        val date: String,
        val stories: MutableList<Story>,
        val top_stories: List<TopStory>)