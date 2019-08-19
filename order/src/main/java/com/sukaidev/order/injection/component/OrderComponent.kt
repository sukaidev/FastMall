package com.sukaidev.order.injection.component

import com.sukaidev.common.injection.PerComponentScope
import com.sukaidev.common.injection.component.ActivityComponent
import com.sukaidev.order.injection.module.OrderModule
import com.sukaidev.order.ui.activity.OrderActivity
import com.sukaidev.order.ui.activity.OrderConfirmActivity
import com.sukaidev.order.ui.activity.OrderDetailActivity
import com.sukaidev.order.ui.fragment.OrderFragment
import dagger.Component

/**
 * Created by sukaidev on 2019/08/17.
 * 订单Component.
 */
@PerComponentScope
@Component(dependencies = [ActivityComponent::class], modules = [OrderModule::class])
interface OrderComponent {
    fun inject(activity: OrderConfirmActivity)
    fun inject(fragment: OrderFragment)
    fun inject(activity: OrderDetailActivity)
}