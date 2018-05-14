package unioncast.cn.alllibsdemo.modal.domain

import com.google.gson.annotations.SerializedName

data class ThemeEntities(@SerializedName("limit") var limit: Int,
                         @SerializedName("others") var themes: List<MainMenu>)
