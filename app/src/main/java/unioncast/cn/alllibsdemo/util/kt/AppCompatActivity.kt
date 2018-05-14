package unioncast.cn.alllibsdemo.util.kt

import android.graphics.Color
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

inline fun AppCompatActivity.setToolBar(toolbar: Toolbar, indicator: Int = 0,
                                        title: CharSequence? = null,
                                        indicatorBack: Boolean = true) {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
        if (indicator > 0)
            setHomeAsUpIndicator(indicator)
        title?.let { setTitle(title) }
        setDisplayHomeAsUpEnabled(true)
    }
    if (indicatorBack) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }


}