package com.bmc.buenacocinavendors.domain.usecase

import com.bmc.buenacocinavendors.core.DateUtils
import com.bmc.buenacocinavendors.domain.Result
import com.bmc.buenacocinavendors.domain.error.FormError
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class ValidateDiscountDateConsistency @Inject constructor() {
    operator fun invoke(
        startDate: LocalDateTime?,
        startTime: LocalTime?,
        endDate: LocalDateTime?,
        endTime: LocalTime?
    ): Result<List<LocalDateTime>, FormError.DiscountDateConsistencyError> {
        if (startDate == null || endDate == null) {
            return Result.Error(FormError.DiscountDateConsistencyError.DATES_ARE_NULL)
        }
        if (startTime == null || endTime == null) {
            return Result.Error(FormError.DiscountDateConsistencyError.TIMES_ARE_NULL)
        }
        val start = DateUtils.localDateTimeWithLocalTime(startDate, startTime)
        val end = DateUtils.localDateTimeWithLocalTime(endDate, endTime)
        if (start == end) {
            return Result.Error(FormError.DiscountDateConsistencyError.DATES_ARE_EQUAL)
        }
        if (start.isAfter(end)) {
            return Result.Error(FormError.DiscountDateConsistencyError.START_DATE_AFTER_END_DATE)
        }
        if (start.isBefore(LocalDateTime.now())) {
            return Result.Error(FormError.DiscountDateConsistencyError.START_DATE_OUT_OF_RANGE)
        }
        if (end.isBefore(start)) {
            return Result.Error(FormError.DiscountDateConsistencyError.END_DATE_BEFORE_START_DATE)
        }
        return Result.Success(listOf(start, end))
    }
}