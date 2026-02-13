package com.it342.timesheets.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.it342.timesheets.data.UserResponse

@Composable
fun LogoutModal(
    user: UserResponse?,
    onKeepSignedIn: () -> Unit,
    onConfirmSignOut: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onKeepSignedIn,
        title = { Text("Sign out?") },
        text = {
            Text(
                "Currently signed in as ${user?.username ?: ""}\n${user?.email?.let { "Email: $it" } ?: ""}"
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmSignOut) {
                Text("Confirm sign-out", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onKeepSignedIn) {
                Text("Keep signed-in")
            }
        }
    )
}
