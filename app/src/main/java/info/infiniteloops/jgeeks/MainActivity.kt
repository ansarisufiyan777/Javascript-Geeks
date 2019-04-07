package info.infiniteloops.jgeeks

import android.app.Activity
import android.graphics.Color
import android.os.Bundle

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import java.util.ArrayList

import info.infiniteloops.jgeeks.Utils.UploadPost
import info.infiniteloops.jgeeks.Utils.Utils
import info.infiniteloops.jgeeks.models.Post
import info.infiniteloops.jgeeks.network.FirebaseUtils
import info.infiniteloops.library.ntb.NavigationTabBar
import info.infiniteloops.jgeeks.adapter.RecycleAdapter
import kotlinx.android.synthetic.main.activity_horizontal_coordinator_ntb.*

/**
 * Created by Asna on 28.03.2019.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_coordinator_ntb)
        initUI()
    }

    private fun initUI() {
        val colors = resources.getStringArray(R.array.default_preview)
        val tabNames = resources.getStringArray(R.array.tab_names)
        val urls = resources.getStringArray(R.array.urls)

        vp_horizontal_ntb!!.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return 6
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun destroyItem(container: View, position: Int, `object`: Any) {
                (container as ViewPager).removeView(`object` as View)
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = LayoutInflater.from(
                        baseContext).inflate(R.layout.item_vp_list, null, false)

                val recyclerView = view.findViewById<RecyclerView>(R.id.rv)
                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager = LinearLayoutManager(
                        baseContext, RecyclerView.VERTICAL, false
                )
                val fu = FirebaseUtils()
                val arrayList = ArrayList<Post>()
                fu.generateRVData(this@MainActivity, tabNames[position], recyclerView, arrayList)
                container.addView(view)
                return view
            }
        }


        val models = ArrayList<NavigationTabBar.Model>()
        models.add(
                NavigationTabBar.Model.Builder(
                        resources.getDrawable(R.drawable.like),
                        Color.parseColor(colors[0]))
                        .title(tabNames[0])
                        .build()
        )
        models.add(
                NavigationTabBar.Model.Builder(
                        resources.getDrawable(R.drawable.js),
                        Color.parseColor(colors[1]))
                        .title(tabNames[1])
                        .build()
        )
        models.add(
                NavigationTabBar.Model.Builder(
                        resources.getDrawable(R.drawable.angular),
                        Color.parseColor(colors[2]))
                        .title(tabNames[2])
                        .build()
        )
        models.add(
                NavigationTabBar.Model.Builder(
                        resources.getDrawable(R.drawable.react),
                        Color.parseColor(colors[3]))
                        .title(tabNames[3])
                        .build()
        )
        models.add(
                NavigationTabBar.Model.Builder(
                        resources.getDrawable(R.drawable.vuew),
                        Color.parseColor(colors[4]))
                        .title(tabNames[4])
                        .build()
        )
        models.add(
                NavigationTabBar.Model.Builder(
                        resources.getDrawable(R.drawable.css),
                        Color.parseColor(colors[5]))
                        .title(tabNames[5])
                        .build()
        )

        ntb_horizontal!!.models = models
        ntb_horizontal!!.setViewPager(vp_horizontal_ntb, 2)
        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
        ntb_horizontal!!.isBehaviorEnabled = true

        ntb_horizontal!!.onTabBarSelectedIndexListener = object : NavigationTabBar.OnTabBarSelectedIndexListener {
            override fun onStartTabSelected(model: NavigationTabBar.Model, index: Int) {}

            override fun onEndTabSelected(model: NavigationTabBar.Model, index: Int) {
                model.hideBadge()
            }
        }
        ntb_horizontal!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        fab!!.setOnClickListener { UploadPost.uploadAlert(this@MainActivity, vp_horizontal_ntb!!.currentItem) }
    }
}
