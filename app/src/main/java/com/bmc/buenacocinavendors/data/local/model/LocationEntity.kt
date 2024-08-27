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
@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "storeId")
    val storeId: String = "",

    @ColumnInfo(name = "createdAt")
    @field:TypeConverters(DateTypeConverter::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "updatedAt")
    @field:TypeConverters(DateTypeConverter::class)
    val updatedAt: LocalDateTime = LocalDateTime.now()
) : Parcelable