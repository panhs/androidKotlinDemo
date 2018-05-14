package unioncast.cn.alllibsdemo.modal.domain

import com.google.gson.annotations.SerializedName

data class MainMenu(@SerializedName("description") var description: String?,
                    @SerializedName("name") var name: String,
                    @SerializedName("id") var id: Int?,
                    @SerializedName("thumbnail") var thumbnail: String?)