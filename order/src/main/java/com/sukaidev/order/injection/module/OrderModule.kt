package com.sukaidev.order.injection.module

import com.sukaidev.order.service.OrderService
import com.sukaidev.order.service.impl.OrderServiceImpl
import dagger.Module
import dagger.Provides

/**
 * Created by sukaidev on 2019/08/17.
 *
 */
@Module
class OrderModule {

    @Provides
    fun providesOrderService(service: OrderServiceImpl): OrderService {
        return service
    }
}