package com.sukaidev.goods.presenter

import com.sukaidev.common.ext.execute
import com.sukaidev.common.presenter.BasePresenter
import com.sukaidev.common.rx.BaseSubscriber
import com.sukaidev.goods.data.protocol.CartGoods
import com.sukaidev.goods.presenter.view.ICartView
import com.sukaidev.goods.service.ICartService
import javax.inject.Inject

/**
 * Created by sukaidev on 2019/08/16.
 *
 */
class CartPresenter @Inject constructor() : BasePresenter<ICartView>() {

    @Inject
    lateinit var service: ICartService

    /**
     * 获取购物车商品列表
     */
    fun getCartList() {
        if (!checkNetWork()) {
            return
        }
        service.getCartList()
            .execute(object : BaseSubscriber<MutableList<CartGoods>?>(mView) {
                override fun onNext(t: MutableList<CartGoods>?) {
                    mView.onGetCartListResult(t)
                }
            }, lifecycleProvider)
    }

    /**
     * 删除购物车商品
     */
    fun deleteCartList(deleteCartList: List<Int>) {
        if (!checkNetWork()) {
            return
        }
        service.deleteCartList(deleteCartList)
            .execute(object : BaseSubscriber<Boolean>(mView) {
                override fun onNext(t: Boolean) {
                    mView.onDeleteCartListResult(t)
                }
            }, lifecycleProvider)
    }

    /**
     * 提交购物车
     */
    fun submitCart(list: MutableList<CartGoods>, totalPrice: Long) {
        if (!checkNetWork()) {
            return
        }
        service.submitCart(list, totalPrice).execute(object : BaseSubscriber<Int>(mView) {
            override fun onNext(t: Int) {
                mView.onSubmitCartResult(t)
            }
        }, lifecycleProvider)
    }
}