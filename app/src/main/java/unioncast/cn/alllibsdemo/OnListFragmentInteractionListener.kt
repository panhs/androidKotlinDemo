package unioncast.cn.alllibsdemo

import unioncast.cn.alllibsdemo.modal.domain.Story

interface OnListFragmentInteractionListener {
    fun onListFragmentInteraction(item: Story)

    fun visiableGroupTitleChanged(newTitle: String?)
}
