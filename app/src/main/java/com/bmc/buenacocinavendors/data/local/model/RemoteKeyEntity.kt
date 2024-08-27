package com.bmc.buenacocinavendors.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val label: String = "",

    @ColumnInfo(name = "nextKey")
    val nextKey: String? = null
)
