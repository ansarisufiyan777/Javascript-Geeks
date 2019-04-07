package info.infiniteloops.jgeeks.adapter

import android.app.Activity

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.jsoup.helper.StringUtil

import java.util.ArrayList

import info.infiniteloops.jgeeks.R
import info.infiniteloops.jgeeks.Utils.Utils
import info.infiniteloops.jgeeks.models.MetaData
import info.infiniteloops.jgeeks.models.Post
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.meta_info.view.*

class RecycleAdapter(private val baseContext: Activity, private val arrayList: ArrayList<Post>) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(baseContext).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = arrayList[position]
        val linkMetaData = arrayList[position].metaData
        holder.bind(post, linkMetaData)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post, linkMetaData: MetaData?) {
            itemView.txtVpItemList!!.text = post.caption
            if (linkMetaData != null) {
                if (!StringUtil.isBlank(linkMetaData.image)) {
                    itemView.metaImageUrl!!.setImageURI(Utils.getUriFromStr(linkMetaData.image!!))
                    itemView.metaImageUrl!!.setOnClickListener { view -> Utils.openChromeTab(baseContext, post.postLink!!) }
                }
                if (!StringUtil.isBlank(linkMetaData.siteName)) {
                    itemView.metaSiteName!!.text = linkMetaData.siteName
                }
                if (!StringUtil.isBlank(linkMetaData.description)) {
                    itemView.metaDesc!!.text = linkMetaData.description
                }
                if (!StringUtil.isBlank(linkMetaData.title)) {
                    itemView.metaTitle!!.text = linkMetaData.title
                }
                itemView.metaDataContainer!!.setOnClickListener { view -> Utils.openChromeTab(baseContext, post.postLink!!) }
            }

            if (StringUtil.isBlank(post.imageurl)) {
                if (!StringUtil.isBlank(linkMetaData?.image)) {
                    itemView.imageUrl!!.setImageURI(Utils.getUriFromStr(linkMetaData?.image!!))
                }
            } else {
                itemView.imageUrl!!.setImageURI(Utils.getUriFromStr(post.imageurl!!))
            }
        }
    }

}