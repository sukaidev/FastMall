package com.sukaidev.pay.injection.module

import com.sukaidev.pay.service.IPayService
import com.sukaidev.pay.service.impl.PayServiceImpl
import dagger.Module
import dagger.Provides

/**
 * Created by sukaidev on 2019/08/19.
 *
 */
@Module
class PayModule {

    @Provides
    fun providesPayService(service: PayServiceImpl): IPayService {
        return service
    }
}