package com.sukaidev.goods.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.kennyc.view.MultiStateView
import com.sukaidev.common.ext.onClick
import com.sukaidev.common.ext.setVisible
import com.sukaidev.common.ext.startLoading
import com.sukaidev.common.ui.fragment.BaseMvpFragment
import com.sukaidev.common.utils.AppPrefsUtils
import com.sukaidev.common.utils.MoneyConverter
import com.sukaidev.goods.R
import com.sukaidev.goods.common.GoodsConstant
import com.sukaidev.goods.data.protocol.CartGoods
import com.sukaidev.goods.event.CartAllCheckedEvent
import com.sukaidev.goods.event.UpdateCartSizeEvent
import com.sukaidev.goods.event.UpdateTotalPriceEvent
import com.sukaidev.goods.injection.component.DaggerCartComponent
import com.sukaidev.goods.injection.module.CartModule
import com.sukaidev.goods.presenter.CartPresenter
import com.sukaidev.goods.presenter.view.ICartView
import com.sukaidev.goods.ui.adapter.CartGoodsAdapter
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.mMultiStateView
import org.jetbrains.anko.toast

/**
 * Created by sukaidev on 2019/08/16.
 *
 */
class CartFragment : BaseMvpFragment<CartPresenter>(), ICartView {

    private lateinit var mCartGoodsAdapter: CartGoodsAdapter

    private var mTotalPrice: Long = 0

    override fun injectComponent() {
        DaggerCartComponent.builder()
            .activityComponent(activityComponent)
            .cartModule(CartModule())
            .build()
            .inject(this)
        mPresenter.mView = this
    }

    override fun setLayout(): Any {
        return R.layout.fragment_cart
    }

    override fun onBindView(savedInstanceState: Bundle?, rootView: View) {
        initView()
        initObserve()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun initView() {
        mCartGoodsRv.layoutManager = LinearLayoutManager(context)
        mCartGoodsAdapter = CartGoodsAdapter(context!!)
        mCartGoodsRv.adapter = mCartGoodsAdapter

        mHeaderBar.getRightTv().onClick {
            refreshEditStatus()
        }

        mAllCheckedCb.onClick {
            for (item in mCartGoodsAdapter.dataList) {
                item.isSelected = mAllCheckedCb.isChecked
            }
            mCartGoodsAdapter.notifyDataSetChanged()
            updateTotalPrice()
        }

        mDeleteBtn.onClick {
            val cartIdList: MutableList<Int> = arrayListOf()
            mCartGoodsAdapter.dataList.filter {
                it.isSelected
            }.mapTo(cartIdList) {
                it.id
            }
            if (cartIdList.isEmpty()) {
                context?.toast("请选择需要删除的数据")
            } else {
                mPresenter.deleteCartList(cartIdList)
            }
        }

        mSettleAccountsBtn.onClick {
            val cartGoodsList: MutableList<CartGoods> = arrayListOf()
            mCartGoodsAdapter.dataList.filter {
                it.isSelected
            }.mapTo(cartGoodsList) {
                it
            }
            if (cartGoodsList.isEmpty()) {
                context?.toast("请选择需要提交的数据")
            } else {
                mPresenter.submitCart(cartGoodsList, mTotalPrice)
            }
        }
    }

    /**
     * 更新编辑按钮状态
     */
    private fun refreshEditStatus() {
        val isEditStatus = getString(R.string.common_edit) == mHeaderBar.getRightTv().text
        mTotalPriceTv.setVisible(isEditStatus.not())
        mSettleAccountsBtn.setVisible(isEditStatus.not())
        mDeleteBtn.setVisible(isEditStatus)

        mHeaderBar.getRightTv().text =
            if (isEditStatus) getString(R.string.common_complete) else getString(R.string.common_edit)
    }

    private fun loadData() {
        mMultiStateView.startLoading()
        mPresenter.getCartList()
    }

    override fun onGetCartListResult(result: MutableList<CartGoods>?) {
        if (result != null && result.size > 0) {
            mCartGoodsAdapter.setData(result)
            mHeaderBar.getRightTv().setVisible(true)
            mAllCheckedCb.isChecked = false
            mMultiStateView.viewState = MultiStateView.ViewState.CONTENT
        } else {
            mHeaderBar.getRightTv().setVisible(false)
            mMultiStateView.viewState = MultiStateView.ViewState.EMPTY
        }
        AppPrefsUtils.putInt(GoodsConstant.SP_CART_SIZE, result?.size ?: 0)
        Bus.send(UpdateCartSizeEvent())
        updateTotalPrice()
    }

    private fun initObserve() {
        // 订阅全选事件
        Bus.observe<CartAllCheckedEvent>()
            .subscribe {
                run {
                    mAllCheckedCb.isChecked = it.isAllChecked
                    updateTotalPrice()
                }
            }.registerInBus(this)
        // 订阅总价变化事件
        Bus.observe<UpdateTotalPriceEvent>()
            .subscribe {
                updateTotalPrice()
            }.registerInBus(this)
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPrice() {
        mTotalPrice = mCartGoodsAdapter.dataList
            .filter { it.isSelected }
            .map { it.goodsCount * it.goodsPrice }.sum()
        mTotalPriceTv.text = "合计${MoneyConverter.changeF2YWithUnit(mTotalPrice)}"
    }

    override fun onDeleteCartListResult(result: Boolean) {
        context?.toast("删除成功")
        refreshEditStatus()
        loadData()
    }

    override fun onSubmitCartResult(result: Int) {
        context?.toast("$result")
    }

    override fun onDestroy() {
        super.onDestroy()
        Bus.unregister(this)
    }

    fun setBackVisible(isVisible: Boolean) {
        mHeaderBar.getLeftIv().setVisible(isVisible)
    }
}