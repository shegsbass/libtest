package com.shegs.libtest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shegs.libtest.ui.theme.screens.LoginScreen
import com.shegs.libtest.ui.theme.screens.ProfileScreen
import com.shegs.libtest.ui.theme.screens.SignupUi

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = "signupScreen"){
        composable("signupScreen"){
            SignupUi(navController = navController)
        }

        composable("loginScreen"){
            LoginScreen(navController)
        }

        composable("profileScreen"){
            ProfileScreen()
        }
    }
}