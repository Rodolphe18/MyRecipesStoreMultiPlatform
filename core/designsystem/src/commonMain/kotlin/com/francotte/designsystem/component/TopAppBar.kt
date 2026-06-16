package com.francotte.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francotte.designsystem.resources.Res
import com.francotte.designsystem.resources.ic_back
import com.francotte.model.NO_PROFILE_IMAGE_URL
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    actionIcon: ImageVector? = null,
    navigationIcon: ImageVector = vectorResource(Res.drawable.ic_back),
    navigationIconColor: Color = MaterialTheme.colorScheme.onSurface,
    actionIconContentDescription: String? = "",
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onActionClick: () -> Unit = {},
    onNavigationClick: () -> Unit = {},
    navigationIconEnabled: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    profileImage: String? = null,
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = title.orEmpty(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = actionIcon,
                        contentDescription = actionIconContentDescription,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        navigationIcon = {
            if (navigationIconEnabled) {
                if (profileImage != null && profileImage != NO_PROFILE_IMAGE_URL) {
                    DesignAsyncImage(
                        model = profileImage,
                        width = 45.dp,
                        height = 45.dp,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .offset(x = 12.dp)
                                .clip(CircleShape)
                                .clickable { onNavigationClick() },
                    )
                } else {
                    IconButton(onClick = onNavigationClick) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = navigationIcon,
                            contentDescription = null,
                            tint = navigationIconColor,
                        )
                    }
                }
            }
        },
        colors = colors,
        modifier = modifier,
    )
}
