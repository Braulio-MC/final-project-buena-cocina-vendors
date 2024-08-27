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
import com.bmc.buenacocinavendors.data.local.model.CategoryEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg categories: CategoryEntity): LongArray

    @Update
    suspend fun update(vararg categories: CategoryEntity)

    @Query("SELECT * FROM categories")
    fun get(): PagingSource<Int, CategoryEntity>

    @Query("SELECT * FROM categories WHERE id IN (:categoryIds)")
    fun get(categoryIds: List<String>): PagingSource<Int, CategoryEntity>

    @RawQuery(observedEntities = [CategoryEntity::class])
    fun get(query: SupportSQLiteQuery): PagingSource<Int, CategoryEntity>

    @Query("SELECT * FROM categories WHERE id LIKE :id LIMIT 1")
    suspend fun find(id: String): CategoryEntity?

    @Query("SELECT EXISTS (SELECT 1 FROM categories WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Query("SELECT (SELECT COUNT(*) FROM categories) == 0")
    suspend fun isEmpty(): Boolean

    @Delete
    suspend fun delete(category: CategoryEntity): Int

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun delete(id: String): Int

    @RawQuery(observedEntities = [CategoryEntity::class])
    suspend fun delete(query: SupportSQLiteQuery): Int

    @Query("DELETE FROM categories")
    suspend fun delete(): Int
}