package com.bmc.buenacocinavendors.data.local

import androidx.room.TypeConverter
import com.bmc.buenacocinavendors.data.local.model.OrderEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
}

class ListTypeConverter {
    @TypeConverter
    fun fromStringToOrderLineList(data: String?): List<OrderEntity.OrderLineEntity> {
        if (data.isNullOrBlank()) {
            return emptyList()
        }
        val listType = object : TypeToken<List<OrderEntity.OrderLineEntity>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun fromOrderLineListToString(list: List<OrderEntity.OrderLineEntity>): String {
        if (list.isEmpty()) {
            return ""
        }
        val listType = object : TypeToken<List<OrderEntity.OrderLineEntity>>() {}.type
        return Gson().toJson(list, listType)
    }
}