package info.infiniteloops.jgeeks.models

/**
 * Created by Asna on 3/17/2019.
 */
class MetaData {

    var description: String? = null
    var image: String? = null
    var type: String? = null
    var title: String? = null
    var url: String? = null
    var siteName: String? = null

    companion object {
        var ogType = "og:type"
        var ogSiteName = "og:site_name"
        var ogTitle = "og:title"
        var ogUrl = "og:url"
        var ogImage = "og:image"
        var ogDescription = "og:description"
    }
}
