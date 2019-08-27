package com.sukaidev.kotlinmall.ui.activitiy

import android.os.Bundle
import com.sukaidev.core.ui.activities.ProxyActivity
import com.sukaidev.core.ui.delegates.BaseDelegate
import com.sukaidev.kotlinmall.ui.fragment.BottomDelegate
import com.sukaidev.kotlinmall.ui.fragment.LauncherDelegate
import com.sukaidev.core.ui.launcher.ILauncherListener

/**
 * Created by sukaidev on 2019/08/26.
 * 主Activity，也是此工程唯一Activity.
 */
class MainActivity : ProxyActivity(), ILauncherListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    /**
     * 设置根Fragment
     * 此处为启动页
     */
    override fun setRootDelegate(): BaseDelegate {
        return LauncherDelegate()
    }

    override fun onLauncherFinished() {
        startWithPop(BottomDelegate())
    }
}