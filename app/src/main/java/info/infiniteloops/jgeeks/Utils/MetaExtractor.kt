package info.infiniteloops.jgeeks.Utils

import android.app.Activity
import android.app.Dialog
import android.os.AsyncTask

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.io.IOException

import info.infiniteloops.jgeeks.models.MetaData
import info.infiniteloops.jgeeks.models.Post
import info.infiniteloops.jgeeks.network.FirestoreUtils

/**
 * Created by Asna on 3/17/2019.
 */
class MetaExtractor {

    fun extractMetaInfo(post: Post, activity: Activity) {
        MyTask(post, activity).execute()
    }

    private inner class MyTask(private val post: Post, private val activity: Activity) : AsyncTask<Void, Void, MetaData>() {

        override fun doInBackground(vararg params: Void): MetaData? {
            val doc: Document
            try {
                doc = Jsoup.connect(post.postLink).get()

                val metaTags = doc.getElementsByTag("meta")
                val ex = MetaData()

                for (metaTag in metaTags) {
                    val content = metaTag.attr("content")
                    val name = metaTag.attr("property")

                    if (MetaData.ogTitle == name) {
                        ex.title = content
                    } else if (MetaData.ogDescription == name) {
                        ex.description = content
                    } else if (MetaData.ogImage == name) {
                        ex.image = content
                    } else if (MetaData.ogSiteName == name) {
                        ex.siteName = content
                    } else if (MetaData.ogUrl == name) {
                        ex.url = content
                    } else if (MetaData.ogType == name) {
                        ex.type = content
                    }
                }
                return ex

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }


        override fun onPostExecute(result: MetaData?) {
            //if you had a ui element, you could display the title
            if (result != null) {
                post.metaData = result
                FirestoreUtils().setPostData(post, activity)
            }
        }
    }
}
