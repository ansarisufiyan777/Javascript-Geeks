package info.infiniteloops.jgeeks.Utils

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.widget.ImageView

import java.util.Calendar
import java.util.TimeZone

import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import info.infiniteloops.jgeeks.R

/**
 * Created by Asna on 3/17/2019.
 */
object Utils {
    var RC_SIGN_IN = 1001
    val dateMillis: Long
        get() {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            return cal.timeInMillis
        }

    fun getUriFromStr(url: String): Uri {
        return Uri.parse(url)
    }

    fun openChromeTab(activity: Activity, url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
        builder.setStartAnimations(activity, R.anim.slide_in_right, R.anim.slide_out_left)
        // vice versa
        //builder.setExitAnimations(activity, R.anim.slide_in_left, R.anim.slide_out_right);
        customTabsIntent.launchUrl(activity, Uri.parse(url))

    }

    fun setLikeImage(view:ImageView,activity: Activity,category: String){
        val tabNames = activity.resources.getStringArray(R.array.tab_names)
        if(tabNames[0] == category){
            view.setImageDrawable(activity.getDrawable(R.drawable.likes_like))
        }else if(tabNames[1] == category){
            view.setImageDrawable(activity.getDrawable(R.drawable.js_like))

        }else if(tabNames[2] == category){
            view.setImageDrawable(activity.getDrawable(R.drawable.angular_like))

        }else if(tabNames[3] == category){
            view.setImageDrawable(activity.getDrawable(R.drawable.react_like))

        }else if(tabNames[4] == category){
            view.setImageDrawable(activity.getDrawable(R.drawable.vue_like))

        }else if(tabNames[5] == category){
            view.setImageDrawable(activity.getDrawable(R.drawable.css_like))

        }

    }
}
