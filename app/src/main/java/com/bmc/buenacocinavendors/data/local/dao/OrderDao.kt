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
import com.bmc.buenacocinavendors.data.local.model.OrderEntity

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg orders: OrderEntity): LongArray

    @Update
    suspend fun update(vararg orders: OrderEntity)

    @Query("SELECT * FROM orders")
    fun get(): PagingSource<Int, OrderEntity>

    @Query("SELECT * FROM orders WHERE id IN (:orderIds)")
    fun get(orderIds: List<String>): PagingSource<Int, OrderEntity>

    @RawQuery(observedEntities = [OrderEntity::class])
    fun get(query: SupportSQLiteQuery): PagingSource<Int, OrderEntity>

    @Query("SELECT * FROM orders WHERE id LIKE :id LIMIT 1")
    suspend fun find(id: String): OrderEntity?

    @Query("SELECT EXISTS (SELECT 1 FROM orders WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Query("SELECT (SELECT COUNT(*) FROM orders) == 0")
    suspend fun isEmpty(): Boolean

    @Delete
    suspend fun delete(order: OrderEntity): Int

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun delete(id: String): Int

    @RawQuery(observedEntities = [OrderEntity::class])
    suspend fun delete(query: SupportSQLiteQuery): Int

    @Query("DELETE FROM orders")
    suspend fun delete(): Int
}