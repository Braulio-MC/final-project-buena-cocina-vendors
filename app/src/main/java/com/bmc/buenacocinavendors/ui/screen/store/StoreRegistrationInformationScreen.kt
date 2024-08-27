package com.bmc.buenacocinavendors.ui.screen.store

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.MAXIMUM_EMAIL_LENGTH
import com.bmc.buenacocinavendors.core.MAXIMUM_PHONE_NUMBER_LENGTH
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MAXIMUM_STORE_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MAXIMUM_STORE_NAME_LENGTH
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MINIMUM_STORE_DESCRIPTION_LENGTH
import com.bmc.buenacocinavendors.core.STORE_REGISTRATION_MINIMUM_STORE_NAME_LENGTH
import com.bmc.buenacocinavendors.core.makeBulletedList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegistrationInformationScreen(
    windowSizeClass: WindowSizeClass,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState),
    scrollState: ScrollState = rememberScrollState(),
    onBackButton: () -> Unit
) {
    StoreRegistrationInformationScreenContent(
        windowSizeClass = windowSizeClass,
        scrollBehavior = scrollBehavior,
        scrollState = scrollState,
        onBackButton = onBackButton
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreRegistrationInformationScreenContent(
    windowSizeClass: WindowSizeClass,
    scrollBehavior: TopAppBarScrollBehavior,
    scrollState: ScrollState,
    onBackButton: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.store_registration_information_screen_title),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackButton() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(start = 12.dp, end = 12.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val restrictions = listOf(
                stringResource(
                    id = R.string.store_registration_information_screen_name_restriction,
                    STORE_REGISTRATION_MINIMUM_STORE_NAME_LENGTH,
                    STORE_REGISTRATION_MAXIMUM_STORE_NAME_LENGTH
                ),
                stringResource(
                    id = R.string.store_registration_information_screen_desc_restriction,
                    STORE_REGISTRATION_MINIMUM_STORE_DESCRIPTION_LENGTH,
                    STORE_REGISTRATION_MAXIMUM_STORE_DESCRIPTION_LENGTH
                ),
                stringResource(
                    id = R.string.store_registration_information_screen_email_restriction,
                    MAXIMUM_EMAIL_LENGTH
                ),
                stringResource(
                    id = R.string.store_registration_information_screen_phone_restriction,
                    MAXIMUM_PHONE_NUMBER_LENGTH
                )
            )
            val recommendations = listOf(
                stringResource(id = R.string.store_registration_information_screen_image_recommendation1),
                stringResource(id = R.string.store_registration_information_screen_image_recommendation2),
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Text(
                text = stringResource(id = R.string.store_registration_information_screen_considerations),
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            Text(
                text = makeBulletedList(items = restrictions),
                fontSize = 17.5.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 30.sp,
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            )
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Text(
                text = stringResource(id = R.string.store_registration_information_screen_recommendations),
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            Text(
                text = makeBulletedList(items = recommendations),
                fontSize = 17.5.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 30.sp,
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            )
        }
    }
}