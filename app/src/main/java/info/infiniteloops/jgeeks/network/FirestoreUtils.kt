package info.infiniteloops.jgeeks.network

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import info.infiniteloops.jgeeks.Utils.MetaExtractor
import info.infiniteloops.jgeeks.Utils.Utils
import info.infiniteloops.jgeeks.adapter.RecycleAdapter
import info.infiniteloops.jgeeks.models.Post
import info.infiniteloops.jgeeks.models.PostEditor
import org.jsoup.helper.StringUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Asna on 4/28/2019.
 */
class FirestoreUtils {
    fun insertPost(postEditor: PostEditor, category: String, activity: Activity) {
        val id = Utils.dateMillis
        val post = Post()
        post.id = id.toString()
        post.caption = postEditor.caption
        post.imageurl = postEditor.imageUrl
        post.postLink = postEditor.postLink
        post.category = category
        post.datemillis = id
        post.created = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date())
        post.category = category
        //TODO: FIXME After the implementation of AuthLogin
        val ub: Post.UserBean
        val session = SessionManager(activity);
            ub = Post.UserBean()
        if (session.isLoggedIn) {
            ub.email = session.email
            ub.username = session.name
            ub.userid = session.uid
        }else{
            ub.email = "sufiyan2you@gmail.com"
            ub.username = "Sufiyan"
            ub.userid = "6121992"
        }
        post.user = ub
        if (!StringUtil.isBlank(post.postLink)) {
            val metaExtractor = MetaExtractor()
            metaExtractor.extractMetaInfo(post, activity)
        } else {
            setPostData(post, activity)
        }
    }

    fun setPostData(post: Post, activity: Activity) {
        val instance = getInstance(post.category!!).document(post.datemillis.toString())
        instance.set(post).addOnSuccessListener {
            Log.e(activity.packageName, "Post added successfully.")
            Toast.makeText(activity, "Post added successfully.", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Log.e(activity.packageName, it.message)
            Toast.makeText(activity, "Failure: " + it.message, Toast.LENGTH_LONG).show()
            it.printStackTrace()
        }
    }

    fun generateRVData(activity: Activity, category: String, adapter: RecycleAdapter, arrayList: ArrayList<Post>, startTimeMillis: String) {
        val instance = getInstance(category)
        val loadingDialog = ProgressDialog(activity)

        instance.orderBy("datemillis", Query.Direction.DESCENDING)
                .startAfter(startTimeMillis.toLong())
                .limit(5)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (res in task.result!!) {
                            val post = res.toObject(Post::class.java)
                            arrayList.add(post)
                        }
                        adapter.notifyDataSetChanged()
                        loadingDialog.dismiss()
                    }
                }
    }

    fun getInstance(collection: String): CollectionReference {
        return FirebaseFirestore.getInstance().collection(collection)
    }

}