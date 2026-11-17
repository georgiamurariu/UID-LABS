package com.example.laboratory_uid.authScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(25.dp)
    ) {
        Text(
            text = "Username",
            fontSize = 30.sp
        )

        TextField(
            value = viewModel.username,
            onValueChange = { newUsername ->
                viewModel.updateUsername(newUsername)
            },
            singleLine = true,
            supportingText = {
                Text(
                    text = viewModel.usernameError
                )
            },
            isError = viewModel.usernameError.isNotEmpty()
        )

        // Sau asa:

//        if(viewModel.usernameError.isNotEmpty()) {
//            Text(
//                text = viewModel.usernameError,
//                color = Color.Red
//            )
//        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = "Password",
            fontSize = 30.sp
        )

        TextField(
            value = viewModel.password,
            onValueChange = { newPassword ->
                viewModel.updatePassword(newPassword)
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            supportingText = { Text(text = viewModel.passwordError) },
            isError = viewModel.passwordError.isNotEmpty()
        )

        Button(
            onClick = {
                viewModel.signIn()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Sign In"
            )
        }

        if (viewModel.authenticationMessage.isNotEmpty()) {
            Text(
                text = viewModel.authenticationMessage,
                color = if (viewModel.isAuthenticated) Color(0xFF2E7D32) else Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    //AuthScreen()
}

















