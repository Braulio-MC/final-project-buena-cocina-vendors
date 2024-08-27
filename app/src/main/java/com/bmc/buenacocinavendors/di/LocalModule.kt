package com.bmc.buenacocinavendors.di

import android.content.Context
import androidx.room.Room
import com.bmc.buenacocinavendors.core.ROOM_DATABASE_NAME
import com.bmc.buenacocinavendors.data.local.LocalDatabase
import com.bmc.buenacocinavendors.data.local.dao.CategoryDao
import com.bmc.buenacocinavendors.data.local.dao.DiscountDao
import com.bmc.buenacocinavendors.data.local.dao.LocationDao
import com.bmc.buenacocinavendors.data.local.dao.OrderDao
import com.bmc.buenacocinavendors.data.local.dao.ProductDao
import com.bmc.buenacocinavendors.data.local.dao.RemoteKeyDao
import com.bmc.buenacocinavendors.data.local.dao.StoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext appContext: Context): LocalDatabase {
        return Room.databaseBuilder(
            appContext, LocalDatabase::class.java, ROOM_DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: LocalDatabase): CategoryDao {
        return db.getCategoryDao()
    }

    @Provides
    @Singleton
    fun provideDiscountDao(db: LocalDatabase): DiscountDao {
        return db.getDiscountDao()
    }

    @Provides
    @Singleton
    fun provideLocationDao(db: LocalDatabase): LocationDao {
        return db.getLocationDao()
    }

    @Provides
    @Singleton
    fun provideOrderDao(db: LocalDatabase): OrderDao {
        return db.getOrderDao()
    }

    @Provides
    @Singleton
    fun provideProductDao(db: LocalDatabase): ProductDao {
        return db.getProductDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeyDao(db: LocalDatabase): RemoteKeyDao {
        return db.getRemoteKeyDao()
    }

    @Provides
    @Singleton
    fun provideStoreDao(db: LocalDatabase): StoreDao {
        return db.getStoreDao()
    }
}