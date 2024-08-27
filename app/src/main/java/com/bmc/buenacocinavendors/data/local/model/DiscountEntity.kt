package com.bmc.buenacocinavendors.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bmc.buenacocinavendors.data.local.DateTypeConverter
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "discounts")
data class DiscountEntity(
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "percentage")
    val percentage: Double = 0.0,

    @ColumnInfo(name = "startDate")
    @field:TypeConverters(DateTypeConverter::class)
    val startDate: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "endDate")
    @field:TypeConverters(DateTypeConverter::class)
    val endDate: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "storeId")
    val storeId: String = "",

    @ColumnInfo(name = "createdAt")
    @field:TypeConverters(DateTypeConverter::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "updatedAt")
    @field:TypeConverters(DateTypeConverter::class)
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) : Parcelable
