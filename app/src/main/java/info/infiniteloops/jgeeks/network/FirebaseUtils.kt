package info.infiniteloops.jgeeks.network

import android.app.Activity
import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

import org.jsoup.helper.StringUtil

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

import androidx.recyclerview.widget.RecyclerView
import info.infiniteloops.jgeeks.Utils.MetaExtractor
import info.infiniteloops.jgeeks.Utils.Utils
import info.infiniteloops.jgeeks.adapter.RecycleAdapter
import info.infiniteloops.jgeeks.models.Post
import info.infiniteloops.jgeeks.models.PostEditor

/**
 * Created by dell on 7/23/2017.
 */

class FirebaseUtils {

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
        val ub = Post.UserBean()
        ub.email = "a@a.com"
        ub.username = "Anonymous"
        ub.userid = "6121992"
        post.user = ub
        if (!StringUtil.isBlank(post.postLink)) {
            val metaExtractor = MetaExtractor()
            metaExtractor.extractMetaInfo(post, activity)
        } else {
            setPostData(post, activity)
        }
    }


    fun setPostData(post: Post, activity: Activity) {
        getFireBaseDataBase(POST + "/" + post.category)
                .child(post.id)
                .setValue(post).addOnSuccessListener { aVoid ->
                    Log.e(activity.packageName, "Post added successfully.")
                    Toast.makeText(activity, "Post added successfully.", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { e ->
                    Log.e(activity.packageName, e.message)
                    Toast.makeText(activity, "Failure: " + e.message, Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
    }

    fun generateRVData(activity: Activity, category: String, rc: RecyclerView, arrayList: ArrayList<Post>) {

        val loadingDialog = ProgressDialog(activity)
        getFireBaseQuery("$POST/$category").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue<Post>(Post::class.java)!!
                    arrayList.add(post)
                    setRecyclerView(arrayList, activity, rc)
                }
                loadingDialog.dismiss()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                loadingDialog.dismiss()
                Toast.makeText(activity, "Error Loading Firebase Data", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setRecyclerView(arrayList: ArrayList<Post>, activity: Activity, rc: RecyclerView) {
        rc.adapter = null
        val adapter = RecycleAdapter(activity, arrayList)
        rc.adapter = adapter
    }

    companion object {
        val POST = "post"
        fun getFireBaseQuery(category: String): Query {
            return getFireBaseDataBase(category).orderByChild("datemillis")
        }

        fun getFireBaseDataBase(category: String): DatabaseReference {
            val mFirebaseDatabase: DatabaseReference
            val mFirebaseInstance: FirebaseDatabase = FirebaseDatabase.getInstance()
            mFirebaseDatabase = mFirebaseInstance.getReference(category)
            return mFirebaseDatabase
        }
    }
}
