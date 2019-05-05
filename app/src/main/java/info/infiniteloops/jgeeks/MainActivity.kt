package info.infiniteloops.jgeeks

import android.content.Context
import android.graphics.Color
import android.os.Bundle

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import info.infiniteloops.jgeeks.Utils.EndlessRecyclerViewScrollListener

import java.util.ArrayList

import info.infiniteloops.jgeeks.Utils.UploadPost
import info.infiniteloops.jgeeks.adapter.RecycleAdapter
import info.infiniteloops.jgeeks.models.Post
import info.infiniteloops.library.ntb.NavigationTabBar
import info.infiniteloops.jgeeks.network.FirestoreUtils
import kotlinx.android.synthetic.main.activity_horizontal_coordinator_ntb.*
import android.content.Intent
import info.infiniteloops.jgeeks.Utils.Utils
import info.infiniteloops.jgeeks.network.GoogleLogin
import com.google.firebase.auth.FirebaseAuth
import info.infiniteloops.jgeeks.network.SessionManager
import kotlinx.android.synthetic.main.login_loayout.view.*
import org.jsoup.helper.StringUtil


/**
 * Created by Asna on 28.03.2019.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var auth: FirebaseAuth
    var colors: Array<String> = emptyArray()
    var tabNames: Array<String> = emptyArray()
    var sess: SessionManager? = null;
    var startTimeMillis = "1577750400000" // start date new Date('2019-12-31').getTime()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sess = SessionManager(this@MainActivity)
        setContentView(R.layout.activity_horizontal_coordinator_ntb)
        setupInitialData()
        initUI()
    }

    private fun setupInitialData() {
        auth = FirebaseAuth.getInstance()
        colors = resources.getStringArray(R.array.default_preview)
        tabNames = resources.getStringArray(R.array.tab_names)
        if (sess!!.isLoggedIn && !StringUtil.isBlank(sess!!.email)) {
            tabNames[0] = sess!!.email
        }
    }

    private fun initUI() {
        attachViewPagerListener()

        setupTabs()

        fab!!.setOnClickListener { UploadPost.uploadAlert(this@MainActivity, vp_horizontal_ntb!!.currentItem) }
    }

    private fun attachViewPagerListener() {
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
                var view = LayoutInflater.from(
                        baseContext).inflate(R.layout.item_vp_list, null, false)
                if (position == 0 && !sess!!.isLoggedIn) {
                    view = LayoutInflater.from(
                            baseContext).inflate(R.layout.login_loayout, null, false)
                    view.login.setOnClickListener {
                        GoogleLogin(this@MainActivity).loginWithgmail()
                    }
                } else {
                    iniRecyckerView(view, position)
                }
                container.addView(view)
                return view
            }
        }
    }

    private fun iniRecyckerView(view: View, position: Int) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        val fu = FirestoreUtils()
        val arrayList = ArrayList<Post>()
        val adapter = RecycleAdapter(this@MainActivity, arrayList)
        recyclerView.adapter = adapter
        fu.generateRVData(this@MainActivity, tabNames[position], adapter, arrayList, startTimeMillis)

        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                val lastIndexTimeMillis = arrayList[arrayList.size - 1].datemillis
                Toast.makeText(this@MainActivity, "Page: $page: $lastIndexTimeMillis", Toast.LENGTH_SHORT).show()
                fu.generateRVData(this@MainActivity, tabNames[position], adapter, arrayList, lastIndexTimeMillis.toString())
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
    }

    private fun setupTabs() {
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
        ntb_horizontal!!.setViewPager(vp_horizontal_ntb, 1)
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
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.RC_SIGN_IN) GoogleLogin(this@MainActivity).firebaseAuthWithGoogle(auth, data!!)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            SessionManager(this@MainActivity).createLoginSession(currentUser!!.email!!, currentUser.uid, currentUser.displayName!!)
            Toast.makeText(this@MainActivity, "Already Login", Toast.LENGTH_LONG).show();
        }
    }
}
