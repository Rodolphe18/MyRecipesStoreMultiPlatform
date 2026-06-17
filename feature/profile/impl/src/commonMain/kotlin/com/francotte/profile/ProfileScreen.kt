package com.francotte.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francotte.designsystem.component.DesignAsyncImage
import com.francotte.designsystem.component.TopAppBar
import com.francotte.designsystem.theme.Orange
import com.francotte.model.hasCustomImage
import com.francotte.profile.resources.Res
import com.francotte.profile.resources.supporting_text_email
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {
    val user = state.user ?: return
    val topAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = "Profile",
                scrollBehavior = topAppBarScrollBehavior,
                navigationIconEnabled = true,
                onNavigationClick = { onAction(ProfileAction.OnBackClick) },
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
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(padding.calculateTopPadding() + 20.dp))
                // Avatar (image edit via picker is deferred).
                if (user.hasCustomImage) {
                    DesignAsyncImage(
                        model = user.image,
                        width = 200.dp,
                        height = 200.dp,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clip(CircleShape),
                    )
                } else {
                    Box(
                        Modifier
                            .size(200.dp)
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
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text("Username", color = MaterialTheme.colorScheme.onSurface)
                    BasicTextField(
                        value = state.editedName,
                        onValueChange = { onAction(ProfileAction.OnNameChange(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color(0xFFF6E8D6), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        decorationBox = { innerTextField ->
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                                innerTextField()
                            }
                        },
                    )
                    if (state.editedName.isNotEmpty() && state.isNameChanged) {
                        Text(
                            text = if (state.isNameValid) "Valid ✅" else "At least 6 characters",
                            color = if (state.isNameValid) Color(0xFF81C784) else Color.Red,
                            fontSize = 12.sp,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text("Email", color = MaterialTheme.colorScheme.onSurface)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF6E8D6))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(text = user.email, color = Color.Black, fontSize = 14.sp)
                    }
                    Text(
                        text = stringResource(Res.string.supporting_text_email),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 11.sp,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        onAction(ProfileAction.OnSave)
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(46.dp),
                ) {
                    Text(text = "Update", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                }
            }
        }
    }
}
