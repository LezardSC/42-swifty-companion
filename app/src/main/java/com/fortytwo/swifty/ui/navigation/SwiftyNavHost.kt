package com.fortytwo.swifty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fortytwo.swifty.ui.profile.ProfileScreen
import com.fortytwo.swifty.ui.search.SearchScreen

@Composable
fun SwiftyNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, modifier = modifier, startDestination = "search") {
        composable("search") {
            SearchScreen(
                onUserFound = { login -> navController.navigate("profile/$login") }
            )
        }
        composable(
            route = "profile/{login}",
            arguments = listOf(navArgument("login") { type = NavType.StringType })
        ) { backStackEntry ->
            val login = backStackEntry.arguments?.getString("login").orEmpty()
            ProfileScreen(
                login = login,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
