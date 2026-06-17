package com.francotte.reset

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.francotte.ui.CustomTextField

@Composable
fun ResetPasswordScreen(
    token: String,
    state: ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
) {
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp)) {
        Text("New password")
        CustomTextField(text = password, onTextChange = { password = it })
        Button(onClick = { onAction(ResetPasswordAction.OnConfirmClick(token, password)) }) {
            Text("Confirm")
        }

        if (state.message.isNotBlank()) {
            Text(state.message, color = if (state.isSuccess) Color.Green else Color.Red)
        }
    }
}
