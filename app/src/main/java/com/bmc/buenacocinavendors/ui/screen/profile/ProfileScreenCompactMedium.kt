package com.bmc.buenacocinavendors.ui.screen.profile

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.NetworkStatus

@Composable
fun ProfileScreenCompactMedium(
    uiState: ProfileUiState,
    netState: NetworkStatus,
    scrollState: ScrollState,
    isLogoutButtonEnabled: Boolean,
    onLogoutButtonChanged: (Boolean) -> Unit,
    onStartLogout: (Context, () -> Unit, () -> Unit) -> Unit,
    onLogoutButton: (Boolean) -> Unit
) {
    if (uiState.userProfile != null) {
        val currentContext = LocalContext.current
        val userProfile = uiState.userProfile
        val userNickname =
            userProfile.nickname ?: stringResource(id = R.string.profile_screen_no_nickname_msg)
        val userEmail =
            userProfile.email ?: stringResource(id = R.string.profile_screen_no_email_msg)
        val isEmailVerified = userProfile.isEmailVerified ?: false
        val userProfilePictureContentDesc =
            stringResource(id = R.string.profile_screen_user_image_content_desc)
        val isEmailVerifiedText =
            stringResource(id = R.string.profile_screen_is_email_verified_text)
        val isEmailVerifiedYesMsg =
            stringResource(id = R.string.profile_screen_is_email_verified_yes_msg)
        val isEmailVerifiedNoMsg =
            stringResource(id = R.string.profile_screen_is_email_verified_no_msg)
        val logoutButtonText = stringResource(id = R.string.profile_screen_logout_button_text)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier
                    .padding(top = 30.dp, bottom = 10.dp)
                    .size(150.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userProfile.pictureURL)
                        .crossfade(true)
                        .build(),
                    contentDescription = userProfilePictureContentDesc,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                )
            }
            Card(
                shape = RoundedCornerShape(
                    15.dp
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 15.dp
                ),
                modifier = Modifier
                    .padding(15.dp)
                    .size(370.dp, 150.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = userNickname,
                        fontSize = 30.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                    )
                    Text(
                        text = userEmail,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Card(
                shape = RoundedCornerShape(
                    8.dp
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 15.dp
                ),
                modifier = Modifier
                    .padding(15.dp)
                    .size(350.dp, 50.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = isEmailVerifiedText,
                        fontSize = 17.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier
                            .weight(2f)
                    )
                    Text(
                        text = if (isEmailVerified) isEmailVerifiedYesMsg else isEmailVerifiedNoMsg,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
            Button(
                onClick = {
                    onLogoutButtonChanged(false)
                    onStartLogout(currentContext, {  // On logout error
                        onLogoutButtonChanged(true)
                        onLogoutButton(false)
                    }, {  // On logout success
                        onLogoutButton(true)
                    })
                },
                enabled = isLogoutButtonEnabled,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .padding(15.dp)
                    .size(220.dp, 50.dp)
            ) {
                Text(
                    text = logoutButtonText,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    } else {

    }
}