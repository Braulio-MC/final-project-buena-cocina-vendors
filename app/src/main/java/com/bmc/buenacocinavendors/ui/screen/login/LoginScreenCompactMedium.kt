package com.bmc.buenacocinavendors.ui.screen.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmc.buenacocinavendors.R
import com.bmc.buenacocinavendors.core.NetworkStatus

@Composable
fun LoginScreenCompactMedium(
    netState: NetworkStatus,
    onLoginButton: (Boolean) -> Unit,
    isLoginButtonEnabled: Boolean,
    updateEnableLoginButton: (Boolean) -> Unit,
    onStartLogin: (Context, () -> Unit, () -> Unit) -> Unit
) {
    val currentContext = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.logo_transparent_background
            ),
            contentDescription = stringResource(id = R.string.login_screen_app_main_logo_content_desc),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(300.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Card(
            shape = RoundedCornerShape(
                15.dp
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            ),
            modifier = Modifier
                .size(350.dp, 200.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.login_screen_screen_desc),
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(5.dp)
                )
                Button(
                    onClick = {
                        // Login button was pressed and login was triggered, button is not enabled
                        updateEnableLoginButton(false)
                        onStartLogin(currentContext, {  // On login error
                            // Login process failed, button needs to be available
                            updateEnableLoginButton(true)
                            onLoginButton(false)
                        }, { // On login success
                            onLoginButton(true)
                        })
                    },
                    enabled = isLoginButtonEnabled,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .size(200.dp, 50.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.login_screen_login_button_text),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.login_screen_iam_msg),
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        )
    }
}