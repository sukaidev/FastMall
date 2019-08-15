package com.sukaidev.user.presenter

import com.sukaidev.common.ext.execute
import com.sukaidev.common.presenter.BasePresenter
import com.sukaidev.common.rx.BaseSubscriber
import com.sukaidev.user.data.protocol.UserInfo
import com.sukaidev.user.presenter.view.ILoginView
import com.sukaidev.user.service.IUserService
import javax.inject.Inject

/**
 * Created by sukaidev on 2019/08/12.
 *
 */
class LoginPresenter @Inject constructor() : BasePresenter<ILoginView>() {

    @Inject
    lateinit var userService: IUserService

    fun login(mobile: String, pwd: String, pushId: String) {
        if (!checkNetWork()) {
            return
        }
        mView.showLoading()
        userService.login(mobile, pwd, pushId).execute(object : BaseSubscriber<UserInfo>(mView) {
            override fun onNext(t: UserInfo) {
                mView.onLoginResult(t)
            }
        }, lifecycleProvider)
    }
}