package com.sukaidev.user.presenter

import com.sukaidev.core.ext.execute
import com.sukaidev.core.presenter.BasePresenter
import com.sukaidev.core.rx.BaseSubscriber
import com.sukaidev.user.data.protocol.UserInfo
import com.sukaidev.user.presenter.view.LoginView
import com.sukaidev.user.service.UserService
import javax.inject.Inject

/**
 * Created by sukaidev on 2019/08/12.
 *
 */
class LoginPresenter @Inject constructor() : BasePresenter<LoginView>() {

    @Inject
    lateinit var userService: UserService

    fun login(mobile: String, pwd: String, pushId: String) {
        if (!checkNetWork()) {
            return
        }
//        mView.showLoading()
        userService.login(mobile, pwd, pushId).execute(object : BaseSubscriber<UserInfo>(mView) {
            override fun onNext(t: UserInfo) {
                mView.onLoginResult(t)
            }
        }, lifecycleProvider)
    }
}