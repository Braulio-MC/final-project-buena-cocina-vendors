package com.bmc.buenacocinavendors.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.bmc.buenacocinavendors.data.local.model.LocationEntity

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg locations: LocationEntity): LongArray

    @Update
    suspend fun update(vararg locations: LocationEntity)

    @Query("SELECT * FROM locations")
    fun get(): PagingSource<Int, LocationEntity>

    @Query("SELECT * FROM locations WHERE id IN (:locationIds)")
    fun get(locationIds: List<String>): PagingSource<Int, LocationEntity>

    @RawQuery(observedEntities = [LocationEntity::class])
    fun get(query: SupportSQLiteQuery): PagingSource<Int, LocationEntity>

    @Query("SELECT * FROM locations WHERE id LIKE :id LIMIT 1")
    suspend fun find(id: String): LocationEntity?

    @Query("SELECT EXISTS (SELECT 1 FROM locations WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Query("SELECT (SELECT COUNT(*) FROM locations) == 0")
    suspend fun isEmpty(): Boolean

    @Delete
    suspend fun delete(location: LocationEntity): Int

    @Query("DELETE FROM locations WHERE id = :id")
    suspend fun delete(id: String): Int

    @RawQuery(observedEntities = [LocationEntity::class])
    suspend fun delete(query: SupportSQLiteQuery): Int

    @Query("DELETE FROM locations")
    suspend fun delete(): Int
}