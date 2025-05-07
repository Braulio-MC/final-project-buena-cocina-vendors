package com.bmc.buenacocinavendors.domain.error

sealed interface FormError : BaseError {
    enum class StoreNameError : FormError {
        NAME_IS_BLANK,
        NAME_TOO_SHORT,
        NAME_TOO_LONG
    }

    enum class StoreDescriptionError : FormError {
        DESCRIPTION_IS_BLANK,
        DESCRIPTION_TOO_SHORT,
        DESCRIPTION_TOO_LONG
    }

    enum class LocationError : FormError {
        NOT_A_VALID_LOCATION
    }

    enum class LocationNameError : FormError {
        NAME_IS_BLANK,
        NAME_TOO_SHORT,
        NAME_TOO_LONG
    }

    enum class LocationDescriptionError : FormError {
        DESCRIPTION_IS_BLANK,
        DESCRIPTION_TOO_SHORT,
        DESCRIPTION_TOO_LONG
    }

    enum class CategoryError : FormError {
        NOT_A_VALID_CATEGORY
    }

    enum class CategoryNameError : FormError {
        NAME_IS_BLANK,
        NAME_TOO_SHORT,
        NAME_TOO_LONG
    }

    enum class DiscountError : FormError {
        NOT_A_VALID_DISCOUNT
    }

    enum class DiscountPercentageError : FormError {
        PERCENTAGE_IS_BLANK,
        PERCENTAGE_TOO_SHORT,
        PERCENTAGE_TOO_LONG,
        NOT_A_VALID_PERCENTAGE
    }

    enum class DiscountDateConsistencyError : FormError {
        DATES_ARE_NULL,
        DATES_ARE_EQUAL,
        TIMES_ARE_NULL,
        START_DATE_AFTER_END_DATE,
        START_DATE_OUT_OF_RANGE,
        END_DATE_BEFORE_START_DATE,
    }

    enum class DiscountNameError : FormError {
        NAME_IS_BLANK,
        NAME_TOO_SHORT,
        NAME_TOO_LONG
    }

    enum class EmailError : FormError {
        EMAIL_IS_BLANK,
        NOT_A_VALID_EMAIL,
        EMAIL_TOO_LONG
    }

    enum class PhoneNumberError : FormError {
        PHONE_NUMBER_IS_BLANK,
        PHONE_NUMBER_TOO_SHORT,
        PHONE_NUMBER_TOO_LONG,
        NOT_A_VALID_PHONE_NUMBER
    }

    enum class ImagerError : FormError {
        NOT_VALID_IMAGE
    }

    enum class OrderStatusError : FormError {
        NOT_A_VALID_ORDER_STATUS
    }

    enum class ProductNameError : FormError {
        NAME_IS_BLANK,
        NAME_TOO_SHORT,
        NAME_TOO_LONG
    }

    enum class ProductDescriptionError : FormError {
        DESCRIPTION_IS_BLANK,
        DESCRIPTION_TOO_SHORT,
        DESCRIPTION_TOO_LONG
    }

    enum class ProductPriceError : FormError {
        PRICE_IS_BLANK,
        PRICE_TOO_SHORT,
        NOT_A_VALID_PRICE
    }

    enum class ProductQuantityError : FormError {
        QUANTITY_IS_BLANK,
        QUANTITY_TOO_SHORT,
        NOT_A_VALID_QUANTITY
    }

    enum class ProductError : FormError {
        NOT_A_VALID_PRODUCT
    }
}