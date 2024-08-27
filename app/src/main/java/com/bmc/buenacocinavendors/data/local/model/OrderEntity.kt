package com.bmc.buenacocinavendors.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.bmc.buenacocinavendors.data.local.DateTypeConverter
import com.bmc.buenacocinavendors.data.local.ListTypeConverter
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "total")
    val total: Double = 0.0,

    @ColumnInfo(name = "status")
    val status: String = "",

    @ColumnInfo(name = "createdAt")
    @field:TypeConverters(DateTypeConverter::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "updatedAt")
    @field:TypeConverters(DateTypeConverter::class)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @field:TypeConverters(ListTypeConverter::class)
    val orderLines: List<OrderLineEntity> = ArrayList(),

    @Embedded val user: OrderUserEntity,
    @Embedded val deliveryLocation: OrderDeliveryLocationEntity,
    @Embedded val store: OrderStoreEntity,
    @Embedded val paymentMethod: OrderPaymentMethodEntity,
) : Parcelable {
    @Parcelize
    data class OrderUserEntity(
        val userId: String = "",
        val userName: String = ""
    ) : Parcelable

    @Parcelize
    data class OrderDeliveryLocationEntity(
        val deliveryLocationId: String = "",
        val deliveryLocationName: String = ""
    ) : Parcelable

    @Parcelize
    data class OrderStoreEntity(
        val storeId: String = "",
        val storeName: String = ""
    ) : Parcelable

    @Parcelize
    data class OrderPaymentMethodEntity(
        val paymentMethodId: String = "",
        val paymentMethodName: String = ""
    ) : Parcelable

    @Parcelize
    data class OrderLineEntity(
        val orderLineId: String = "",
        val orderLineTotal: Double = 0.0,
        val orderLineQuantity: Int = 0,
        val product: OrderLineProductEntity,
        val orderLineCreatedAt: Long = 0,
        val orderLineUpdatedAt: Long = 0,
    ) : Parcelable {
        @Parcelize
        data class OrderLineProductEntity(
            val orderLineProductId: String = "",
            val orderLineProductName: String = "",
            val orderLineProductImage: String = "",
            val orderLineProductPrice: Double = 0.0
        ) : Parcelable
    }
}
