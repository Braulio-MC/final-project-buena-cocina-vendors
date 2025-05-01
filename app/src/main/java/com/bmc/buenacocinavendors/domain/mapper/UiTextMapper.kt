package com.bmc.buenacocinavendors.domain.mapper

import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.CATEGORY_MAXIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.CATEGORY_MINIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.DISCOUNT_MAXIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.DISCOUNT_MAXIMUM_PERCENTAGE
import com.bmc.buenacocinavendors.core.DISCOUNT_MINIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.DISCOUNT_MINIMUM_PERCENTAGE
import com.bmc.buenacocinavendors.core.LOCATION_MAXIMUM_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.LOCATION_MAXIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.LOCATION_MINIMUM_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.LOCATION_MINIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.MAXIMUM_EMAIL_LENGTH
import com.bmc.buenacocinavendors.core.PRODUCT_MAXIMUM_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.PRODUCT_MAXIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.PRODUCT_MINIMUM_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.PRODUCT_MINIMUM_NAME_LENGTH
import com.bmc.buenacocinavendors.core.PRODUCT_MINIMUM_PRICE
import com.bmc.buenacocinavendors.core.PRODUCT_MINIMUM_QUANTITY
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MAXIMUM_STORE_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MAXIMUM_STORE_NAME_LENGTH
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MINIMUM_STORE_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MINIMUM_STORE_NAME_LENGTH
import com.bmc.buenacocinavendors.domain.error.FormError
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.UiText

object UiTextMapper {
    fun asUiText(formError: FormError): UiText {
        return when (formError) {
            FormError.EmailError.EMAIL_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_email_is_blank)
            FormError.EmailError.EMAIL_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_email_too_long,
                arrayOf(MAXIMUM_EMAIL_LENGTH)
            )

            FormError.EmailError.NOT_A_VALID_EMAIL -> UiText.StringResource(R.string.ui_text_form_error_email_not_valid)
            FormError.ImagerError.NOT_VALID_IMAGE -> UiText.StringResource(R.string.ui_text_form_error_image_not_valid)
            FormError.PhoneNumberError.PHONE_NUMBER_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_phone_is_blank)
            FormError.PhoneNumberError.PHONE_NUMBER_TOO_SHORT -> UiText.StringResource(R.string.ui_text_form_error_phone_too_short)
            FormError.PhoneNumberError.PHONE_NUMBER_TOO_LONG -> UiText.StringResource(R.string.ui_text_form_error_phone_too_long)
            FormError.PhoneNumberError.NOT_A_VALID_PHONE_NUMBER -> UiText.StringResource(R.string.ui_text_form_error_phone_not_valid)
            FormError.StoreTimeError.NOT_VALID_TIME_FORMAT -> UiText.StringResource(R.string.ui_text_form_error_time_not_valid)
            FormError.StoreDescriptionError.DESCRIPTION_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_store_description_is_blank)
            FormError.StoreDescriptionError.DESCRIPTION_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_store_description_too_short,
                arrayOf(STORE_REGISTRATION_MINIMUM_STORE_DESCRIPTION_LENGTH)
            )

            FormError.StoreDescriptionError.DESCRIPTION_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_store_description_too_long,
                arrayOf(STORE_REGISTRATION_MAXIMUM_STORE_DESCRIPTION_LENGTH)
            )

            FormError.StoreNameError.NAME_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_store_name_is_blank)
            FormError.StoreNameError.NAME_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_store_name_too_short,
                arrayOf(STORE_REGISTRATION_MINIMUM_STORE_NAME_LENGTH)
            )

            FormError.StoreNameError.NAME_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_store_name_too_long,
                arrayOf(STORE_REGISTRATION_MAXIMUM_STORE_NAME_LENGTH)
            )

            FormError.CategoryNameError.NAME_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_category_name_is_blank)
            FormError.CategoryNameError.NAME_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_category_name_too_short,
                arrayOf(CATEGORY_MINIMUM_NAME_LENGTH)
            )

            FormError.CategoryNameError.NAME_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_category_name_too_long,
                arrayOf(CATEGORY_MAXIMUM_NAME_LENGTH)
            )

            FormError.CategoryError.NOT_A_VALID_CATEGORY -> UiText.StringResource(R.string.ui_text_form_error_category_not_valid)
            FormError.LocationDescriptionError.DESCRIPTION_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_location_description_is_blank)
            FormError.LocationDescriptionError.DESCRIPTION_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_location_description_too_short,
                arrayOf(LOCATION_MINIMUM_DESCRIPTION_LENGTH)
            )

            FormError.LocationDescriptionError.DESCRIPTION_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_location_description_too_long,
                arrayOf(LOCATION_MAXIMUM_DESCRIPTION_LENGTH)
            )

            FormError.LocationNameError.NAME_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_location_name_is_blank)
            FormError.LocationNameError.NAME_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_location_name_too_short,
                arrayOf(LOCATION_MINIMUM_NAME_LENGTH)
            )

            FormError.LocationNameError.NAME_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_location_name_too_long,
                arrayOf(LOCATION_MAXIMUM_NAME_LENGTH)
            )

            FormError.LocationError.NOT_A_VALID_LOCATION -> UiText.StringResource(R.string.ui_text_form_error_location_not_valid)
            FormError.DiscountDateConsistencyError.START_DATE_AFTER_END_DATE -> UiText.StringResource(
                R.string.ui_text_form_error_discount_start_date_after_end_date
            )

            FormError.DiscountDateConsistencyError.START_DATE_OUT_OF_RANGE -> UiText.StringResource(
                R.string.ui_text_form_error_discount_start_date_out_range
            )

            FormError.DiscountDateConsistencyError.END_DATE_BEFORE_START_DATE -> UiText.StringResource(
                R.string.ui_text_form_error_discount_end_date_before_start_date
            )

            FormError.DiscountPercentageError.PERCENTAGE_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_discount_percentage_is_blank)
            FormError.DiscountPercentageError.PERCENTAGE_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_discount_percentage_too_short,
                arrayOf(DISCOUNT_MINIMUM_PERCENTAGE)
            )

            FormError.DiscountPercentageError.PERCENTAGE_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_discount_percentage_too_long,
                arrayOf(DISCOUNT_MAXIMUM_PERCENTAGE)
            )

            FormError.DiscountPercentageError.NOT_A_VALID_PERCENTAGE -> UiText.StringResource(R.string.ui_text_form_error_discount_percentage_not_valid)
            FormError.DiscountDateConsistencyError.DATES_ARE_NULL -> UiText.StringResource(R.string.ui_text_form_error_discount_dates_are_null)
            FormError.DiscountDateConsistencyError.TIMES_ARE_NULL -> UiText.StringResource(R.string.ui_text_form_error_discount_times_are_null)
            FormError.DiscountDateConsistencyError.DATES_ARE_EQUAL -> UiText.StringResource(R.string.ui_text_form_error_discount_dates_are_equal)
            FormError.DiscountError.NOT_A_VALID_DISCOUNT -> UiText.StringResource(R.string.ui_text_form_error_discount_not_valid)
            FormError.OrderStatusError.NOT_A_VALID_ORDER_STATUS -> UiText.StringResource(R.string.ui_text_form_error_order_status_not_valid)
            FormError.DiscountNameError.NAME_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_discount_name_is_blank)
            FormError.DiscountNameError.NAME_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_discount_name_too_short,
                arrayOf(DISCOUNT_MINIMUM_NAME_LENGTH)
            )

            FormError.DiscountNameError.NAME_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_discount_name_too_long,
                arrayOf(DISCOUNT_MAXIMUM_NAME_LENGTH)
            )

            FormError.ProductDescriptionError.DESCRIPTION_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_product_description_is_blank)
            FormError.ProductDescriptionError.DESCRIPTION_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_product_description_too_short,
                arrayOf(PRODUCT_MINIMUM_DESCRIPTION_LENGTH)
            )

            FormError.ProductDescriptionError.DESCRIPTION_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_product_description_too_long,
                arrayOf(PRODUCT_MAXIMUM_DESCRIPTION_LENGTH)
            )

            FormError.ProductNameError.NAME_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_product_name_is_blank)
            FormError.ProductNameError.NAME_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_product_name_too_short,
                arrayOf(PRODUCT_MINIMUM_NAME_LENGTH)
            )

            FormError.ProductNameError.NAME_TOO_LONG -> UiText.StringResource(
                R.string.ui_text_form_error_product_name_too_long,
                arrayOf(PRODUCT_MAXIMUM_NAME_LENGTH)
            )

            FormError.ProductPriceError.PRICE_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_product_price_is_blank)
            FormError.ProductPriceError.PRICE_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_product_price_too_short,
                arrayOf(PRODUCT_MINIMUM_PRICE)
            )

            FormError.ProductPriceError.NOT_A_VALID_PRICE -> UiText.StringResource(R.string.ui_text_form_error_product_price_not_valid)
            FormError.ProductQuantityError.QUANTITY_IS_BLANK -> UiText.StringResource(R.string.ui_text_form_error_product_quantity_is_blank)
            FormError.ProductQuantityError.QUANTITY_TOO_SHORT -> UiText.StringResource(
                R.string.ui_text_form_error_product_quantity_too_short,
                arrayOf(PRODUCT_MINIMUM_QUANTITY)
            )

            FormError.ProductQuantityError.NOT_A_VALID_QUANTITY -> UiText.StringResource(R.string.ui_text_form_error_product_quantity_not_valid)
            FormError.ProductError.NOT_A_VALID_PRODUCT -> UiText.StringResource(R.string.ui_text_form_error_product_not_valid)
        }
    }
}

fun FormError.asUiText() = UiTextMapper.asUiText(this)
fun Result.Error<*, FormError>.asFormErrorUiText(): UiText {
    return error.asUiText()
}
