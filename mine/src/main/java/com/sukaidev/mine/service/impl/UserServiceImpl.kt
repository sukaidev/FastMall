package com.sukaidev.mine.service.impl

import com.sukaidev.core.ext.convert
import com.sukaidev.core.ext.convertBoolean
import com.sukaidev.mine.data.protocol.UserInfo
import com.sukaidev.mine.data.repository.UserRepository
import com.sukaidev.mine.service.UserService
import rx.Observable
import javax.inject.Inject

/**
 * Created by sukaidev on 2019/08/10.
 *
 */
class UserServiceImpl @Inject constructor() : UserService {

    @Inject
    lateinit var repository: UserRepository

    override fun login(mobile: String, pwd: String, pushId: String): Observable<UserInfo> {
        return repository.login(mobile, pwd, pushId)
            .convert()
    }

    override fun register(mobile: String, pwd: String, verifyCode: String): Observable<Boolean> {
        return repository.register(mobile, pwd, verifyCode)
            .convertBoolean()
    }

    override fun forgetPwd(mobile: String, verifyCode: String): Observable<Boolean> {
        return repository.forgetPwd(mobile, verifyCode)
            .convertBoolean()
    }

    override fun resetPwd(mobile: String, pwd: String): Observable<Boolean> {
        return repository.resetPwd(mobile, pwd)
            .convertBoolean()
    }

    /**
     * 修改用户信息
     */
    override fun editUser(
        userIcon: String,
        userName: String,
        userGender: String,
        userSign: String
    ): Observable<UserInfo> {
        return repository.editUser(
            userIcon,
            userName,
            userGender,
            userSign
        ).convert()
    }
}