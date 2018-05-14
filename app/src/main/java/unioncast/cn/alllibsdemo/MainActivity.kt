package unioncast.cn.alllibsdemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.view.View
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.warn
import unioncast.cn.alllibsdemo.di.module.network.IRetrofit
import unioncast.cn.alllibsdemo.modal.domain.Story
import unioncast.cn.alllibsdemo.util.kt.forNet
import unioncast.cn.alllibsdemo.util.kt.setToolBar
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), OnListFragmentInteractionListener, AnkoLogger {
    override fun visiableGroupTitleChanged(newTitle: String?) {
        toolbar.title = newTitle
    }

    @Inject
    lateinit var retrofit: IRetrofit

    override fun onListFragmentInteraction(item: Story) {
        var showTitle = item?.type > 1
        warn("showTitle $showTitle")
        startActivity<NewsDetailActivity>("id" to item?.id, "showTitle" to showTitle)
    }

    @Inject
    lateinit var homeFragment: HomeFragment

    @Inject
    lateinit var mThemeNewsFragment: ThemeNewsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Set up the toolbar.
        setToolBar(toolbar, R.drawable.ic_menu, "首页", false)
        getThemes()
        // Set up the navigation drawer.
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark)

        initDrawerNaviView()

        ActiviyUtil.addFragmentToActivity(supportFragmentManager, homeFragment, R.id.contentFrame)
    }

    private fun getThemes() {
        val observable = retrofit.getTheme()
        observable.forNet().subscribe({
            val withIndex = it.themes.withIndex()
            warn("themes:${withIndex.iterator().next().value.name}")
            withIndex.forEach { e2 ->
                val mId = e2.index// + 1
                e2.value.id?.let { nav_view.menu.add(1, it, mId, e2.value.name) }
            }
        }, { warn("Throwable:${it.message}") })
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.menu_item1 -> {
                toggleFragment(homeFragment, "首页")
            }
        }
    }

    private fun toggleFragment(fragment: Fragment, title: CharSequence) {
        ActiviyUtil.addFragmentToActivity(supportFragmentManager, fragment, R.id.contentFrame)
        toolbar.title = title
        drawerLayout.closeDrawers()
    }

    private fun initDrawerNaviView() {

        nav_view.setNavigationItemSelectedListener { menuItem: MenuItem ->
            if (!menuItem.isChecked) {
                toggleFragment(mThemeNewsFragment, menuItem.title)
                mThemeNewsFragment.getListAndShow(menuItem.itemId.toLong())
            }
            warn("ItemSelected $menuItem")
            nav_view.setCheckedItem(menuItem.itemId)
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(nav_view)) {
            drawerLayout.closeDrawers()
            return
        }
        super.onBackPressed()
    }
}
