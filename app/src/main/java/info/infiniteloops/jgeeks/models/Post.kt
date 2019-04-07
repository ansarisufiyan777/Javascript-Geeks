package info.infiniteloops.jgeeks.models

/**
 * Created by Asna on 3/17/2019.
 */
class Post {

    /**
     * caption : Busted!
     * category : 9GAG-DarkHumor-NoGif
     * created : 2017.08.06.07.15.07
     * datemillis : -1502134390003
     * dislike : 0
     * id : https://9gag.com/gag/a0554mO
     * imageurl : https://img-9gag-fun.9cache.com/photo/a0554mO_460s.jpg
     * isalive : true
     * like : 20
     * unratedflagcount : 0
     * user : {"email":"anonymous@havefun.com","userid":"6121992","username":"Anonymous"}
     */

    var caption: String? = null
    var category: String? = null
    var created: String? = null
    var datemillis: Long = 0
    var dislike: Int = 0
    var id: String = ""
    var imageurl: String? = null
    var postLink: String? = null
    var isIsalive: Boolean = false
    var like: Int = 0
    var unratedflagcount: Int = 0
    var user: UserBean? = null
    var metaData: MetaData? = null

    class UserBean {
        /**
         * email : anonymous@havefun.com
         * userid : 6121992
         * username : Anonymous
         */

        var email: String? = null
        var userid: String? = null
        var username: String? = null
    }
}
