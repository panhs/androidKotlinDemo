package unioncast.cn.alllibsdemo.modal.domain

import com.google.gson.annotations.SerializedName
import java.util.*

data class NewsInfoResponse(@SerializedName("body") var body: String,
                       @SerializedName("image_source") var image_source: String,
                       @SerializedName("title") var title: String,
                       @SerializedName("image") var image: String,
                       @SerializedName("share_url") var share_url: String,
                       @SerializedName("js") var js: Array<String>,
                       @SerializedName("images") var images: Array<String>,
                       @SerializedName("id") var id: Int,
                       @SerializedName("css") var css: Array<String>) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewsInfoResponse

        if (body != other.body) return false
        if (image_source != other.image_source) return false
        if (title != other.title) return false
        if (image != other.image) return false
        if (share_url != other.share_url) return false
        if (!Arrays.equals(js, other.js)) return false
        if (!Arrays.equals(images, other.images)) return false
        if (id != other.id) return false
        if (!Arrays.equals(css, other.css)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = body.hashCode()
        result = 31 * result + image_source.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + share_url.hashCode()
        result = 31 * result + Arrays.hashCode(js)
        result = 31 * result + Arrays.hashCode(images)
        result = 31 * result + id
        result = 31 * result + Arrays.hashCode(css)
        return result
    }
}
