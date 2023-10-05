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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shegs.hng_auth_library.authlibrary.AuthLibrary
import com.shegs.hng_auth_library.common.HeaderText
import com.shegs.hng_auth_library.common.LibraryButton
import com.shegs.hng_auth_library.common.RoundedTextField
import com.shegs.hng_auth_library.common.SubtitleText
import com.shegs.hng_auth_library.common.TextFieldHeaderText
import com.shegs.hng_auth_library.model.SignupRequest
import com.shegs.hng_auth_library.network.ApiResponse
import com.shegs.hng_auth_library.ui.screens.SignUpScreen
import com.shegs.libtest.apiService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun SignupUi(navController: NavHostController) {

    var name by remember {mutableStateOf("")}
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var confirm_password by remember {mutableStateOf("")}
    var spinning by remember { mutableStateOf(false) }

    val signupRepository = AuthLibrary.createSignupRepository(apiService)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
    ) {
        val focusManager = LocalFocusManager.current
        LazyColumn(
            modifier = Modifier.padding(start = 34.dp, end = 34.dp)
        ) {
            item {
                Spacer(modifier = Modifier.padding(vertical = 28.dp))
            }
            item {
                HeaderText(text = "Let’s get you \nstarted")
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 6.dp))
            }

            item {
                SubtitleText(text = "Your journey begins here. This is a proof \nthat the auth library is working.")
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 30.dp))
            }
            item {
                TextFieldHeaderText(text = "Username")
                RoundedTextField(
                    value = name,
                    visualTransformation = null,
                    keyboardActions = null,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    placeHolderText = "Enter Username",
                    onValueChange = {name = it})
                Spacer(modifier = Modifier.padding(vertical = 10.dp))

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
                    onValueChange = {email = it})
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
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    ),
                    value = password,
                    placeHolderText = "Enter Password",
                    onValueChange = {password = it})
                Spacer(modifier = Modifier.padding(vertical = 10.dp))

            }
            item {
                var passwordVisibility by remember {
                    mutableStateOf(false)
                }
                TextFieldHeaderText(text = "Confirm Password")
                RoundedTextField(
                    value = confirm_password,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        val icon =
                            if (passwordVisibility) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff

                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    placeHolderText = "Enter Password Again",
                    onValueChange = {confirm_password = it})
                Spacer(modifier = Modifier.padding(vertical = 10.dp))

            }

            item {
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
            }
            item {
                LibraryButton(
                    onClick = {
                        // This code will be executed when the button is clicked
                        coroutineScope.launch {
                            spinning = true
                            val result = signupRepository.signup(
                                SignupRequest(
                                    name = name,
                                    email = email,
                                    password = password,
                                    confirm_password = confirm_password
                                )
                            )
                            when (result) {
                                is ApiResponse.Success -> {
                                    // Handle successful signup
                                    val user = result.data
                                    Toast.makeText(context, "Signup successful: ${user.data.name}", Toast.LENGTH_SHORT).show()
                                    navController.navigate("LoginScreen")
                                }

                                is ApiResponse.Error -> {
                                    // Handle signup error
                                    val errorMessage = result.message
                                    // Display error message to the user
                                    Toast.makeText(context, "Signup failed: $errorMessage", Toast.LENGTH_SHORT).show()
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
                    text = "Register",
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(com.shegs.hng_auth_library.R.font.inter_regular)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    ),
                    shape = RoundedCornerShape(size = 10.dp),
                )
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
                            text = "Have an account?",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(com.shegs.hng_auth_library.R.font.inter_regular)),
                                fontWeight = FontWeight(200),
                                color = Color(0xFF000000),
                            )
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                        ClickableText(
                            text = buildAnnotatedString {
                                                        Text(text = "Login")
                            },
                            modifier = Modifier.clickable {  },
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(com.shegs.hng_auth_library.R.font.inter_regular)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF000000),
                            ),
                            onClick = {
                                navController.navigate("LoginScreen")
                            },

                        )
                    }
                }
            }



        }
    }
}

@Preview
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen()
}