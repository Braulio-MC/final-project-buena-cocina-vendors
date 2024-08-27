package com.bmc.buenacocinavendors.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bmc.buenacocinavendors.core.CATEGORY_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.model.CategoryNetwork
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class CategoryPagingSource(
    private val query: (Query) -> Query = { it },
    private val firestore: FirebaseFirestore
) : PagingSource<QuerySnapshot, CategoryNetwork>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, CategoryNetwork>): QuerySnapshot? =
        null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, CategoryNetwork> {
        return try {
            val ref = firestore.collection(CATEGORY_COLLECTION_NAME)
            val q = query(ref)
            val currentPage = params.key ?: q.get().await()
            val lastVisibleDoc = currentPage.documents.lastOrNull()
            val nextPage =
                if (lastVisibleDoc == null) null else q.startAfter(lastVisibleDoc).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(CategoryNetwork::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}