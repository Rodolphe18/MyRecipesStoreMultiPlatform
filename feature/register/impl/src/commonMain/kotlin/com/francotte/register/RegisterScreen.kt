package com.francotte.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francotte.designsystem.component.CustomButton
import com.francotte.designsystem.component.TopAppBar
import com.francotte.designsystem.theme.Orange
import com.francotte.register.resources.Res
import com.francotte.register.resources.subscribe
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
) {
    val topAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = "Register",
                scrollBehavior = topAppBarScrollBehavior,
                navigationIconEnabled = true,
                onNavigationClick = { onAction(RegisterAction.OnBackClick) },
            )
        },
    ) { padding ->
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 500, easing = FastOutLinearInEasing),
            ),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(padding.calculateTopPadding() + 12.dp))
                // Avatar placeholder (image upload via picker is deferred).
                Box(
                    Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(Orange.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Profile picture",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                RegisterTextField(
                    state.name,
                    { onAction(RegisterAction.OnNameChange(it)) },
                    state.isNameValid,
                    "Valid name ✅",
                    "At least 6 caracters",
                    "UserName",
                )
                Spacer(modifier = Modifier.height(12.dp))
                RegisterTextField(
                    state.email,
                    { onAction(RegisterAction.OnEmailChange(it)) },
                    state.isEmailValid,
                    "Valid email ✅",
                    "Email incorrect",
                    "Email",
                )
                Spacer(modifier = Modifier.height(12.dp))
                RegisterPasswordField(
                    state.password,
                    { onAction(RegisterAction.OnPasswordChange(it)) },
                    state.isPasswordValid,
                    "Secure password ✅",
                    "Invalid password (1 maj, 1 number, 6 characters min)",
                    "Password",
                )
                Spacer(modifier = Modifier.height(12.dp))
                RegisterPasswordField(
                    state.confirmPassword,
                    { onAction(RegisterAction.OnConfirmPasswordChange(it)) },
                    state.isConfirmPasswordValid,
                    "Password confirmed successfully",
                    "Password is not the same",
                    "Confirm password",
                )
                Spacer(modifier = Modifier.height(36.dp))
                CustomButton(
                    onClick = { onAction(RegisterAction.OnRegisterClick) },
                    enabled = state.canRegister && !state.isLoading,
                    contentText = stringResource(Res.string.subscribe),
                )
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun RegisterTextField(
    text: String,
    onTextChange: (String) -> Unit,
    isTextValid: Boolean,
    textValid: String = "",
    textInvalid: String = "",
    label: String = "",
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFF6E8D6), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color(0xFF6D4C41)),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            decorationBox = { innerTextField ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text(text = label, color = Color(0xFF6D4C41), fontSize = 14.sp)
                        }
                        innerTextField()
                    }
                }
            },
        )
        if (text.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isTextValid) textValid else textInvalid,
                color = if (isTextValid) Color(0xFF2E7D32) else Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun RegisterPasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordValid: Boolean,
    passwordValid: String = "",
    passwordInvalid: String = "",
    label: String = "",
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFF6E8D6), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color(0xFF6D4C41)),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (password.isEmpty()) {
                            Text(text = label, color = Color(0xFF6D4C41), fontSize = 14.sp)
                        }
                        innerTextField()
                    }
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = Color(0xFF6D4C41),
                        )
                    }
                }
            },
        )
        if (password.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isPasswordValid) passwordValid else passwordInvalid,
                color = if (isPasswordValid) Color(0xFF2E7D32) else Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
