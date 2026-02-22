package com.auris.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.auris.feature.diary.DiaryScreen
import com.auris.feature.habits.HabitScreen
import com.auris.feature.home.HomeScreen
import com.auris.feature.log.LogScreen
import com.auris.feature.overview.OverviewScreen
import com.auris.feature.profile.ProfileScreen
import com.auris.feature.vitamins.VitaminsScreen
import com.auris.ui.components.AurisBottomNav
import com.auris.ui.components.AurisTab

/**
 * AurisNavHost — root NavHost wiring all screens + floating AurisBottomNav.
 * The nav pill floats over content (Box layout, not Column).
 */
@Composable
fun AurisNavHost(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController    = navController,
            startDestination = Screen.Home.route,
            modifier         = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Log.route) {
                LogScreen()
            }
            composable(Screen.Diary.route) {
                DiaryScreen()
            }
            composable(Screen.Overview.route) {
                OverviewScreen(navController = navController)
            }
            composable(Screen.Vitamins.route) {
                VitaminsScreen(navController = navController)
            }
            composable(Screen.Habits.route) {
                HabitScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(
                route = Screen.Confirmation.route,
                deepLinks = listOf(androidx.navigation.navDeepLink { uriPattern = "auris://log?v=1&meal={meal}&items={items}" })
            ) { backStackEntry ->
                val encodedUri = backStackEntry.arguments?.getString(Screen.Confirmation.ARG_ENCODED_URI)
                LogScreen(encodedUri = encodedUri)
            }
            composable(Screen.SharedText.route) { backStackEntry ->
                val encodedText = backStackEntry.arguments?.getString(Screen.SharedText.ARG_ENCODED_TEXT)
                LogScreen(encodedSharedText = encodedText)
            }
            composable(Screen.VitaminDetail.route) {
                VitaminsScreen(navController = navController)
            }
        }

        // Floating pill nav — overlays content
        AurisBottomNav(
            currentRoute  = currentRoute,
            onTabSelected = { tab: AurisTab ->
                navController.navigate(tab.route) {
                    popUpTo(Screen.Home.route) { saveState = true }
                    launchSingleTop = true
                    restoreState    = true
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}