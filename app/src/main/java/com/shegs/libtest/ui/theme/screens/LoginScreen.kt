package com.shegs.libtest.ui.theme.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shegs.hng_auth_library.R
import com.shegs.hng_auth_library.authlibrary.AuthLibrary
import com.shegs.hng_auth_library.common.HeaderText
import com.shegs.hng_auth_library.common.LibraryButton
import com.shegs.hng_auth_library.common.RoundedTextField
import com.shegs.hng_auth_library.common.SubtitleText
import com.shegs.hng_auth_library.common.TextFieldHeaderText
import com.shegs.hng_auth_library.model.LoginRequest
import com.shegs.hng_auth_library.network.ApiResponse
import com.shegs.libtest.apiService
import com.shegs.libtest.isValidEmail
import com.shegs.libtest.validatePassword
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current
    val dataStoreRepository = AuthLibrary.createDataStoreRepository(context)
    val loginRepository = AuthLibrary.createLoginRepository(apiService, dataStoreRepository)
    var spinning by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Scaffold(
    ) {
        val focusManager = LocalFocusManager.current
        LazyColumn(
            modifier = Modifier.padding(start = 34.dp, end = 34.dp)
        ) {
            val appName = "Kotlin Auth Library"

            item {
                Spacer(modifier = Modifier.padding(vertical = 50.dp))
            }
            item {
                HeaderText(text = "Welcome back to \n${appName}")
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 6.dp))
            }

            item {
                SubtitleText(
                    text = "Hello again! This is to demonstrate\n" +
                            "that the Auth library is working\n" +
                            "Let’s dive you back in already."
                )
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 40.dp))
            }
            item {
                TextFieldHeaderText(text = "Email")
                RoundedTextField(
                    value = email,
                    visualTransformation = null,
                    keyboardActions = null,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    placeHolderText = "Email Address",
                    onValueChange = { newEmail ->
                        email = newEmail
                        emailError = if (isValidEmail(newEmail)) null else "Invalid email"
                    }
                )
                // Display error message if email is invalid
                emailError?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))

            }
            item {
                var passwordVisibility by remember {
                    mutableStateOf(false)
                }

                TextFieldHeaderText(text = "Password")
                RoundedTextField(
                    trailingIcon = {
                        val icon =
                            if (passwordVisibility) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff

                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    value = password,
                    placeHolderText = "Enter Password",
                    onValueChange = { newPassword ->
                        password = newPassword
                        passwordError = validatePassword(newPassword)
                    }
                )
                // Display password error message
                passwordError?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = 10.dp))

            }

            item {
                Spacer(modifier = Modifier.padding(vertical = 20.dp))
            }
            item {
                LibraryButton(
                    onClick = {
                        // This code will be executed when the button is clicked
                        coroutineScope.launch {
                            spinning = true
                            val result = loginRepository.login(
                                LoginRequest(
                                    email = email,
                                    password = password
                                )
                            )

                            when (result) {
                                is ApiResponse.Success -> {
                                    // Handle successful signup
                                    val user = result.data
                                    Toast.makeText(
                                        context,
                                        "Login successful: ${user.data.email}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("profileScreen")
                                }

                                is ApiResponse.Error -> {
                                    // Handle signup error
                                    val errorMessage = result.message
                                    // Display error message to the user
                                    Toast.makeText(
                                        context,
                                        "Login failed: $errorMessage",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            spinning = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    buttonColors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    text = "Login",
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter_regular)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    ),
                    shape = RoundedCornerShape(size = 10.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
            }

            item {
                if (spinning) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
            }
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Don't have an account?",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.inter_regular)),
                                fontWeight = FontWeight(200),
                                color = Color(0xFF000000),
                            )
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        ClickableText(
                            text = buildAnnotatedString {
                                Text(text = "Register")
                            },
                            modifier = Modifier.clickable { },
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF000000),
                            ),
                            onClick = {
                                navController.navigate("signupScreen")
                            }
                        )
                    }
                }
            }

        }
    }
}
