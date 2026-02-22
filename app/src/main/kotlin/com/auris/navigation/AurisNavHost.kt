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
                VitaminsScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.Confirmation.route) {
                OverviewScreen(navController = navController)
            }
            composable(Screen.SharedText.route) {
                OverviewScreen(navController = navController)
            }
            composable(Screen.VitaminDetail.route) {
                OverviewScreen(navController = navController)
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