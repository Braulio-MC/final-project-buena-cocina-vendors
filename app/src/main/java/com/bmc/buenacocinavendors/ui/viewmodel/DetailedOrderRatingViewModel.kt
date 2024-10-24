package com.bmc.buenacocinavendors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmc.buenacocinavendors.core.SHARING_COROUTINE_TIMEOUT_IN_SEC
import com.bmc.buenacocinavendors.domain.repository.OrderLineRepository
import com.bmc.buenacocinavendors.domain.repository.ProductReviewRepository
import com.bmc.buenacocinavendors.domain.repository.StoreReviewRepository
import com.bmc.buenacocinavendors.ui.screen.order.detailed.rating.DetailedOrderRatingUiState
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = DetailedOrderRatingViewModel.DetailedOrderRatingViewModelFactory::class)
class DetailedOrderRatingViewModel @AssistedInject constructor(
    storeReviewRepository: StoreReviewRepository,
    private val productReviewRepository: ProductReviewRepository,
    orderLineRepository: OrderLineRepository,
    @Assisted("orderId") private val orderId: String,
    @Assisted("userId") private val userId: String,
    @Assisted("storeId") private val storeId: String
) : ViewModel() {
    private val _qStoreReview: (Query) -> Query = { query ->
        query.where(
            Filter.and(
                Filter.equalTo("userId", userId),
                Filter.equalTo("storeId", storeId)
            )
        )
    }
    private val _storeReview = storeReviewRepository.get(_qStoreReview)
    private val _orderLines = orderLineRepository.get(orderId)
    val uiState: StateFlow<DetailedOrderRatingUiState> = combine(
        _storeReview,
        _orderLines
    ) { storeReview, orderLines ->
        val productIds = orderLines.map { it.product.id }
        val qProductReview: (Query) -> Query = { query ->
            query.where(
                Filter.and(
                    Filter.equalTo("userId", userId),
                    Filter.inArray("productId", productIds)
                )
            )
        }
        val productReviews = productReviewRepository.get(qProductReview).first()
        DetailedOrderRatingUiState(
            storeRating = storeReview.firstOrNull(),
            orderLines = orderLines,
            itemRatings = productReviews
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SHARING_COROUTINE_TIMEOUT_IN_SEC),
        initialValue = DetailedOrderRatingUiState(isLoading = true)
    )

    @AssistedFactory
    interface DetailedOrderRatingViewModelFactory {
        fun create(
            @Assisted("orderId") orderId: String,
            @Assisted("userId") userId: String,
            @Assisted("storeId") storeId: String
        ): DetailedOrderRatingViewModel
    }
}