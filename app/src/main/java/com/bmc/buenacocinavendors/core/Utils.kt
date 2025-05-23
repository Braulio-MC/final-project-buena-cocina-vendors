package com.bmc.buenacocinavendors.core

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import com.bmc.buenacocinavendors.R
import com.google.firebase.Timestamp
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.Locale

fun getOrderStatusColor(status: String): Int {
    return when (status) {
        OrderStatus.CREATED.status -> R.color.order_status_created
        OrderStatus.ACTIVE.status -> R.color.order_status_active
        OrderStatus.PROGRESS.status -> R.color.order_status_progress
        OrderStatus.ON_WAY.status -> R.color.order_status_way
        OrderStatus.DELIVERED.status -> R.color.order_status_arrived
        OrderStatus.ERROR.status -> R.color.order_status_error
        else -> R.color.order_status_unassigned
    }
}

fun getOrderStatusLevel(status: String): Int {
    return when (status) {
        OrderStatus.CREATED.status -> ORDER_STATUS_CREATED_LEVEL
        OrderStatus.ACTIVE.status -> ORDER_STATUS_ACTIVE_LEVEL
        OrderStatus.PROGRESS.status -> ORDER_STATUS_PROGRESS_LEVEL
        OrderStatus.ON_WAY.status -> ORDER_STATUS_ON_WAY_LEVEL
        OrderStatus.DELIVERED.status -> ORDER_STATUS_DELIVERED_LEVEL
        OrderStatus.ERROR.status -> ORDER_STATUS_ERROR_LEVEL
        else -> ORDER_STATUS_UNASSIGNED_LEVEL
    }
}

fun bubbleShape(): GenericShape {
    return GenericShape { size: Size, _: LayoutDirection ->
        val cornerRadius = 24f
        val triangleSize = 20f

        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height - triangleSize,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            )
            moveTo(size.width / 2 - triangleSize / 2, size.height - triangleSize)
            lineTo(size.width / 2, size.height)
            lineTo(size.width / 2 + triangleSize / 2, size.height - triangleSize)
            close()
        }
        addPath(path)
    }
}

// TODO: create color values in res/value/colors

fun Modifier.shimmerEffect(shape: Shape = RectangleShape): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
        shape = shape
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Composable
fun makeBulletedList(items: List<String>): AnnotatedString {
    val bulletString = "\u2022\t\t"
    val textStyle = LocalTextStyle.current
    val textMeasurer = rememberTextMeasurer()
    val bulletStringWidth = remember(textStyle, textMeasurer) {
        textMeasurer.measure(text = bulletString, style = textStyle).size.width
    }
    val restLine = with(LocalDensity.current) { bulletStringWidth.toSp() }
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = restLine))

    return buildAnnotatedString {
        items.forEach { text ->
            withStyle(style = paragraphStyle) {
                append(bulletString)
                append(text)
            }
        }
    }
}

fun uriToBase64(contentResolver: ContentResolver, uri: Uri): String? {
    return contentResolver.openInputStream(uri)?.use { inputStream ->
        val byteArrayOutputStream = ByteArrayOutputStream()
        inputStream.copyTo(byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        Base64.getEncoder().encodeToString(byteArray)
    }
}

class DateUtils {
    companion object {
        fun convertMillisToLocalDateTime(millis: Long): LocalDateTime {
            return Instant
                .ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }

        private fun convertMillisToLocalDateTimeWithFormatter(
            date: LocalDateTime,
            dateTimeFormatter: DateTimeFormatter
        ): LocalDateTime {
            val dateTimeInMillis =
                LocalDate.parse(date.format(dateTimeFormatter), dateTimeFormatter)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            return Instant
                .ofEpochMilli(dateTimeInMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }

        fun firebaseTimestampToLocalDateTime(timestamp: Timestamp?): LocalDateTime? {
            return timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())
                ?.toLocalDateTime()
        }

        fun localDateTimeToStringWithoutTime(date: LocalDateTime): String {
            val dateTimeFormatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
            return date.format(dateTimeFormatter)
        }

        fun localDateTimeToString(date: LocalDateTime): String {
            val dateTimeFormatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a", Locale.getDefault())
            return date.format(dateTimeFormatter)
        }

        fun localDateTimeToStringISO8601(date: LocalDateTime): String {
            val dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            return date.format(dateTimeFormatter)
        }

        fun stringISO8601ToDateTime(date: String): LocalDateTime {
            val dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            return LocalDateTime.parse(date, dateTimeFormatter)
        }

        fun localDateTimeWithLocalTime(date: LocalDateTime, time: LocalTime): LocalDateTime {
            return date.with(time)
        }

        fun localDateTimeToMillis(date: LocalDateTime): Long {
            return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        fun localDateTimeToFirebaseTimestamp(datetime: LocalDateTime): Timestamp {
            return Timestamp(datetime.atZone(ZoneId.systemDefault()).toInstant().epochSecond, 0)
        }

        fun getRemainingPeriod(startDate: LocalDateTime, endDate: LocalDateTime): Period {
            val diff = Period.between(startDate.toLocalDate(), endDate.toLocalDate())
            return diff
        }

        fun getRemainingPeriodString(startDate: LocalDateTime, endDate: LocalDateTime): String {
            val diff = Period.between(startDate.toLocalDate(), endDate.toLocalDate())
            var str = ""
            if (diff.years > 0) {
                str += "${diff.years} años, "
            }
            if (diff.months > 0) {
                str += "${diff.months} meses y "
            }
            if (diff.days > 0) {
                str += "${diff.days} dias"
            }
            return str
        }

        fun getElapsedTime(startDate: LocalDateTime, endDate: LocalDateTime): Long {
            return ChronoUnit.MINUTES.between(startDate, endDate)
        }

        fun isInRange(
            date: LocalDateTime,
            startDate: LocalDateTime,
            endDate: LocalDateTime
        ): Boolean {
            return date.isAfter(startDate) && date.isBefore(endDate)
        }

        fun iso8601UTCStringDateToLocalDate(date: String): LocalDate {
            val utcZonedDateTime = LocalDate.parse(date).atStartOfDay(ZoneId.of("UTC"))
            val localZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
            return localZonedDateTime.toLocalDate()
        }
    }
}

class TimeUtils {
    companion object {
        fun rawValuesToLocalTime(hours: Int, minutes: Int): LocalTime {
            return LocalTime.of(hours, minutes)
        }

        fun localTimeToString(time: LocalTime): String {
            return time.format(DateTimeFormatter.ofPattern("HH:mm a"))
        }

        fun getRemainingTimeString(startTime: LocalTime, endTime: LocalTime): String {
            val diff = startTime.until(endTime, ChronoUnit.MINUTES)
            val hours = diff / 60
            val minutes = diff % 60
            return "${hours}h y ${minutes}m"
        }
    }
}

