package com.bmc.buenacocinavendors.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bmc.buenacocinavendors.data.local.dao.CategoryDao
import com.bmc.buenacocinavendors.data.local.dao.DiscountDao
import com.bmc.buenacocinavendors.data.local.dao.LocationDao
import com.bmc.buenacocinavendors.data.local.dao.OrderDao
import com.bmc.buenacocinavendors.data.local.dao.ProductDao
import com.bmc.buenacocinavendors.data.local.dao.RemoteKeyDao
import com.bmc.buenacocinavendors.data.local.dao.StoreDao
import com.bmc.buenacocinavendors.data.local.model.CategoryEntity
import com.bmc.buenacocinavendors.data.local.model.DiscountEntity
import com.bmc.buenacocinavendors.data.local.model.LocationEntity
import com.bmc.buenacocinavendors.data.local.model.OrderEntity
import com.bmc.buenacocinavendors.data.local.model.ProductEntity
import com.bmc.buenacocinavendors.data.local.model.RemoteKeyEntity
import com.bmc.buenacocinavendors.data.local.model.StoreEntity

@Database(
    entities = [
        CategoryEntity::class,
        DiscountEntity::class,
        LocationEntity::class,
        OrderEntity::class,
        ProductEntity::class,
        RemoteKeyEntity::class,
        StoreEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateTypeConverter::class,
    ListTypeConverter::class
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getDiscountDao(): DiscountDao
    abstract fun getLocationDao(): LocationDao
    abstract fun getOrderDao(): OrderDao
    abstract fun getProductDao(): ProductDao
    abstract fun getRemoteKeyDao(): RemoteKeyDao
    abstract fun getStoreDao(): StoreDao
}
