package com.sukaidev.goods.injection.component

import com.sukaidev.core.injection.PerComponentScope
import com.sukaidev.core.injection.component.ActivityComponent
import com.sukaidev.goods.injection.module.CartModule
import com.sukaidev.goods.injection.module.GoodsModule
import com.sukaidev.goods.ui.goods.GoodsDetailDelegate
import com.sukaidev.goods.ui.goods.GoodsListDelegate
import dagger.Component

/**
 * Created by sukaidev on 2019/08/15.
 * 商品Component.
 */
@PerComponentScope
@Component(
    dependencies = [ActivityComponent::class],
    modules = [GoodsModule::class, CartModule::class]
)
interface GoodsComponent {
    fun inject(delegate: GoodsListDelegate)
    fun inject(delegate: GoodsDetailDelegate)
}