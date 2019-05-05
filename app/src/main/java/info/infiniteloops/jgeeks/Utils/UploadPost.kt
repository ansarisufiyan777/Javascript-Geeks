package info.infiniteloops.jgeeks.Utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Toast

import org.jsoup.helper.StringUtil

import info.infiniteloops.jgeeks.R
import info.infiniteloops.jgeeks.models.PostEditor
import info.infiniteloops.jgeeks.network.FirestoreUtils
import kotlinx.android.synthetic.main.upload_loayout.view.*

/**
 * Created by Asna on 3/17/2019.
 */
object UploadPost {
    fun uploadAlert(activity: Activity, pos: Int) {
        val colors = activity.resources.getStringArray(R.array.default_preview)
        val tabNames = activity.resources.getStringArray(R.array.tab_names)
        val urls = activity.resources.getStringArray(R.array.urls)

        val dialog = Dialog(activity, android.R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        val inflater = activity.layoutInflater
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.upload_loayout, null)
        dialog.setContentView(view)
        view.submitPost!!.setOnClickListener { view1 ->
            val postEditorModel = PostEditor()
            postEditorModel.caption = view.caption!!.text.toString()
            postEditorModel.postLink = view.postLink!!.text.toString()
            if (StringUtil.isBlank(view.postLink!!.text.toString())) {
                postEditorModel.imageUrl = urls[pos]
            } else {
                postEditorModel.imageUrl = view.imageUrl!!.text.toString()
            }
            postEditorModel.postLink = view.postLink!!.text.toString()
            val fu = FirestoreUtils()
            fu.insertPost(postEditorModel, tabNames[pos], activity)
            Toast.makeText(activity, "Publishing new post", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }
}
