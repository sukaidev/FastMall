package com.sukaidev.user.ui.activity

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.eightbitlab.rxbus.Bus
import com.sukaidev.common.event.LoginSuccessEvent
import com.sukaidev.common.ext.enable
import com.sukaidev.common.ext.onClick
import com.sukaidev.common.ui.activity.BaseMvpActivity
import com.sukaidev.provider.IPushProvider
import com.sukaidev.provider.router.RouterPath
import com.sukaidev.user.R
import com.sukaidev.user.data.protocol.UserInfo
import com.sukaidev.user.injection.component.DaggerUserComponent
import com.sukaidev.user.injection.module.UserModule
import com.sukaidev.user.presenter.LoginPresenter
import com.sukaidev.user.presenter.view.ILoginView
import com.sukaidev.user.utils.UserPrefsUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created by sukaidev on 2019/08/12.
 * 登录.
 */
@Route(path = RouterPath.User.PATH_LOGIN)
class LoginActivity : BaseMvpActivity<LoginPresenter>(), ILoginView, View.OnClickListener {

    @Autowired(name = RouterPath.Message.PATH_MESSAGE_PUSH)
    @JvmField
    var mIPushProvider: IPushProvider? = null

    override fun injectComponent() {
        DaggerUserComponent
            .builder()
            .activityComponent(mActivityComponent)
            .userModule(UserModule())
            .build()
            .inject(this)
        mPresenter.mView = this
    }

    override fun setLayout(): Int {
        return R.layout.activity_login
    }

    override fun onBindView(savedInstanceState: Bundle?) {
        mLoginBtn.enable(mMobileEt) { isBtnEnable() }
        mLoginBtn.enable(mPwdEt) { isBtnEnable() }
        mLoginBtn.onClick(this)
        mHeaderBar.getRightTv().onClick(this)
        mForgetPwdTv.onClick(this)
    }

    override fun onClick(v: View) {
        when (v) {
            mHeaderBar.getRightTv() -> {
                startActivity<RegisterActivity>()
            }
            mLoginBtn -> {
                mPresenter.login(
                    mMobileEt.text.toString(),
                    mPwdEt.text.toString(),
                    mIPushProvider?.getPushId() ?: ""
                )
            }
            mForgetPwdTv -> {
                startActivity<ForgetPwdActivity>()
            }
        }
    }

    /**
     * 检测登录按钮是否可用.
     */
    private fun isBtnEnable(): Boolean {
        return mMobileEt.text.isNullOrEmpty().not()
                && mPwdEt.text.isNullOrEmpty().not()
    }

    override fun onLoginResult(result: UserInfo) {
        toast("登录成功")
        UserPrefsUtils.putUserInfo(result)
        Bus.send(LoginSuccessEvent())
        finish()
    }
}