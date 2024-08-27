package com.bmc.buenacocinavendors.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bmc.buenacocinavendors.data.local.DateTypeConverter
import java.time.LocalDateTime

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String = "",

    @ColumnInfo(name = "image")
    val image: String = "",

    @ColumnInfo(name = "userId")
    val userId: String = "",

    @ColumnInfo(name = "updatedAt")
    @field:TypeConverters(DateTypeConverter::class)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "createdAt")
    @field:TypeConverters(DateTypeConverter::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "rating")
    val rating: Double = 0.0
)