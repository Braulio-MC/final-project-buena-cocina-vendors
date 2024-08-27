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
import com.bmc.buenacocinavendors.data.local.model.ProductEntity
import java.util.Date

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg products: ProductEntity): LongArray

    @Update
    suspend fun update(vararg products: ProductEntity)

    @Query("SELECT * FROM products")
    fun get(): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products WHERE id IN (:productIds)")
    fun get(productIds: List<String>): PagingSource<Int, ProductEntity>

    @RawQuery(observedEntities = [ProductEntity::class])
    fun get(query: SupportSQLiteQuery): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products WHERE id LIKE :id LIMIT 1")
    suspend fun find(id: String): ProductEntity?

    @Query("SELECT EXISTS (SELECT 1 FROM products WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Delete
    suspend fun delete(product: ProductEntity): Int

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun delete(id: String): Int

    @RawQuery(observedEntities = [ProductEntity::class])
    suspend fun delete(query: SupportSQLiteQuery): Int

    @Query("DELETE FROM products")
    suspend fun delete(): Int
}