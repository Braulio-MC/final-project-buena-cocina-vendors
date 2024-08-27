package com.bmc.buenacocinavendors.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bmc.buenacocinavendors.data.local.DateTypeConverter
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "image")
    val image: String = "",

    @ColumnInfo(name = "price")
    val price: Double = 0.0,

    @ColumnInfo(name = "quantity")
    val quantity: Int = 0,

    @ColumnInfo(name = "rating")
    val rating: Double = 0.0,

    @ColumnInfo(name = "updatedAt")
    @field:TypeConverters(DateTypeConverter::class)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "createdAt")
    @field:TypeConverters(DateTypeConverter::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Embedded val store: ProductStore,
    @Embedded val category: ProductCategory,
    @Embedded val discount: ProductDiscount,
) : Parcelable {
    @Parcelize
    data class ProductStore(
        val storeId: String = "",
        val storeName: String = ""
    ) : Parcelable

    @Parcelize
    data class ProductCategory(
        val categoryId: String = "",
        val categoryName: String = "",
        val categoryParent: String = ""
    ) : Parcelable

    @Parcelize
    data class ProductDiscount(
        val discountId: String = "",
        val discountPercentage: Double = 0.0,
        @field:TypeConverters(DateTypeConverter::class)
        val discountStartDate: LocalDateTime = LocalDateTime.now(),
        @field:TypeConverters(DateTypeConverter::class)
        val discountEndDate: LocalDateTime = LocalDateTime.now()
    ) : Parcelable
}
