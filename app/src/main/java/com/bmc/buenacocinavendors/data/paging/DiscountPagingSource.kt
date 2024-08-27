package com.bmc.buenacocinavendors.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bmc.buenacocinavendors.core.DISCOUNT_COLLECTION_NAME
import com.bmc.buenacocinavendors.data.network.model.DiscountNetwork
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class DiscountPagingSource(
    private val query: (Query) -> Query = { it },
    private val firestore: FirebaseFirestore
) : PagingSource<QuerySnapshot, DiscountNetwork>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, DiscountNetwork>): QuerySnapshot? =
        null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, DiscountNetwork> =
        try {
            val ref = firestore.collection(DISCOUNT_COLLECTION_NAME)
            val q = query(ref)
            val currentPage = params.key ?: q.get().await()
            val lastVisibleDoc = currentPage.documents.lastOrNull()
            val nextPage =
                if (lastVisibleDoc == null) null else q.startAfter(lastVisibleDoc).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(DiscountNetwork::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}