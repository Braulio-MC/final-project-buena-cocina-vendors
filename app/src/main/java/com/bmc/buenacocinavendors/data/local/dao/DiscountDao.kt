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
import com.bmc.buenacocinavendors.data.local.model.DiscountEntity


@Dao
interface DiscountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg discounts: DiscountEntity): LongArray

    @Update
    suspend fun update(vararg discounts: DiscountEntity)

    @Query("SELECT * FROM discounts")
    fun get(): PagingSource<Int, DiscountEntity>

    @Query("SELECT * FROM discounts WHERE id IN (:discountIds)")
    fun get(discountIds: List<String>): PagingSource<Int, DiscountEntity>

    @RawQuery(observedEntities = [DiscountEntity::class])
    fun get(query: SupportSQLiteQuery): PagingSource<Int, DiscountEntity>

    @Query("SELECT * FROM discounts WHERE id LIKE :id LIMIT 1")
    suspend fun find(id: String): DiscountEntity?

    @Query("SELECT EXISTS (SELECT 1 FROM discounts WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Query("SELECT (SELECT COUNT(*) FROM discounts) == 0")
    suspend fun isEmpty(): Boolean

    @Delete
    suspend fun delete(discount: DiscountEntity): Int

    @Query("DELETE FROM discounts WHERE id = :id")
    suspend fun delete(id: String): Int

    @RawQuery(observedEntities = [DiscountEntity::class])
    suspend fun delete(query: SupportSQLiteQuery): Int

    @Query("DELETE FROM discounts")
    suspend fun delete(): Int
}