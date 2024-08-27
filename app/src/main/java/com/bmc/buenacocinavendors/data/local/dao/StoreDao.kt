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
import com.bmc.buenacocinavendors.data.local.model.StoreEntity

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg stores: StoreEntity): LongArray

    @Update
    suspend fun update(vararg stores: StoreEntity)

    @Query("SELECT * FROM stores")
    fun get(): PagingSource<Int, StoreEntity>

    @Query("SELECT * FROM stores WHERE id IN (:storeIds)")
    fun get(storeIds: List<String>): PagingSource<Int, StoreEntity>

    @RawQuery(observedEntities = [StoreEntity::class])
    suspend fun get(query: SupportSQLiteQuery): List<StoreEntity>

    @Query("SELECT * FROM stores WHERE id LIKE :id LIMIT 1")
    suspend fun find(id: String): StoreEntity?

    @Query("SELECT EXISTS (SELECT 1 FROM stores WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Query("SELECT (SELECT COUNT(*) FROM stores) == 0")
    suspend fun isEmpty(): Boolean

    @Delete
    suspend fun delete(store: StoreEntity): Int

    @Query("DELETE FROM stores WHERE id = :id")
    suspend fun delete(id: String): Int

    @RawQuery(observedEntities = [StoreEntity::class])
    suspend fun delete(query: SupportSQLiteQuery): Int

    @Query("DELETE FROM stores")
    suspend fun delete(): Int
}