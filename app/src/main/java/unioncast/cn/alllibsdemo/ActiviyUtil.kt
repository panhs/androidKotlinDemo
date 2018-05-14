package unioncast.cn.alllibsdemo

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.app.FragmentManager as FM
import android.app.Fragment as F

object ActiviyUtil {
    fun addFragmentToActivity(fragmentManager: FM,
                              fragment: F, frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    fun addFragmentToActivity(fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int) {
        checkNotNull(fragmentManager)
        checkNotNull(fragment)
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
    }
}