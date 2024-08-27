package com.bmc.buenacocinavendors.ui.screen.home.inner.category.tabs.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmc.buenacocinavendors.domain.model.CategoryDomain
import com.bmc.buenacocinavendors.ui.theme.BuenaCocinaVendorsTheme
import java.time.LocalDateTime

@Composable
fun CategoryTabAddMyCategoryItem(
    category: CategoryDomain,
    onClick: (CategoryDomain) -> Unit
) {
    val parentCategoryName = category.parent.name.ifEmpty { "Sin supercategoria" }

    Column(
        modifier = Modifier
            .padding(5.dp)
            .width(150.dp)
            .height(IntrinsicSize.Max)
            .clickable { onClick(category) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.Category,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = category.name,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Supercategoria",
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = parentCategoryName,
                textAlign = TextAlign.End,
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CategoryTabAddMyCategoryItemPreview() {
    BuenaCocinaVendorsTheme {
        CategoryTabAddMyCategoryItem(
            category = CategoryDomain(
                id = "1",
                name = "Hamburger",
                parent = CategoryDomain.CategoryParentDomain(
                    id = "1",
                    name = "Comida"
                ),
                storeId = "1",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            onClick = {}
        )
    }
}