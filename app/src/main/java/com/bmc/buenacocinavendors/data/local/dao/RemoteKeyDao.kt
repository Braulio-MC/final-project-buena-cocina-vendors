package com.bmc.buenacocinavendors.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bmc.buenacocinavendors.data.local.model.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKeyEntity: RemoteKeyEntity): Long

    @Query("SELECT * FROM remote_keys WHERE label = :label")
    suspend fun find(label: String): RemoteKeyEntity

    @Query("DELETE FROM remote_keys WHERE label = :label")
    suspend fun delete(label: String): Int

    @Query("DELETE FROM remote_keys")
    suspend fun delete(): Int
}