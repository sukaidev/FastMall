package com.sukaidev.goods.injection.module

import com.sukaidev.goods.service.IGoodsService
import com.sukaidev.goods.service.impl.GoodsServiceImpl
import dagger.Module
import dagger.Provides

/**
 * Created by sukaidev on 2019/08/15.
 *
 */
@Module
class GoodsModule {

    @Provides
    fun providesGoodsService(service: GoodsServiceImpl): IGoodsService {
        return service
    }
}