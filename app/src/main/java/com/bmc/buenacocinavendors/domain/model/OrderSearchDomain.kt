package com.bmc.buenacocinavendors.domain.model

import com.bmc.buenacocinavendors.core.Searchable
import com.bmc.buenacocinavendors.core.SearchableTypes
import java.time.LocalDateTime

data class OrderSearchDomain(
    override val id: String,
    val store: OrderSearchStoreDomain,
    val status: String,
    val paymentMethod: OrderSearchPaymentMethodDomain,
    val user: OrderSearchUserDomain,
    val updatedAt: LocalDateTime,
    val rated: Boolean,
    override val type: SearchableTypes
) : Searchable {
    data class OrderSearchPaymentMethodDomain(
        val name: String
    )

    data class OrderSearchStoreDomain(
        val id: String,
        val name: String,
        val ownerId: String
    )

    data class OrderSearchUserDomain(
        val id: String,
        val name: String
    )
}
