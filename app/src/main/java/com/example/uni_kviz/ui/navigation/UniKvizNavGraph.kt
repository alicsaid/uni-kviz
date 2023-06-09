package com.example.uni_kviz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uni_kviz.ui.about.AboutUniKvizDestination
import com.example.uni_kviz.ui.about.AboutUniKvizScreen
import com.example.uni_kviz.ui.end.EndDestination
import com.example.uni_kviz.ui.home.HomeDestination
import com.example.uni_kviz.ui.home.HomeScreen
import com.example.uni_kviz.ui.how_to_play.HowToPlayDestination
import com.example.uni_kviz.ui.how_to_play.HowToPlayScreen
import com.example.uni_kviz.ui.uni_kviz.UniKvizDestination
import com.example.uni_kviz.ui.uni_kviz.UniKvizScreen

@Composable
fun UniKvizNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToUniKviz = { navController.navigate(UniKvizDestination.route) },
                navigateToHowToPlay = { navController.navigate(HowToPlayDestination.route) },
                navigateToAboutUniKviz = { navController.navigate(AboutUniKvizDestination.route) }
            )
        }
        composable(route = UniKvizDestination.route) {
            UniKvizScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(route = HowToPlayDestination.route) {
            HowToPlayScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable(route = AboutUniKvizDestination.route) {
            AboutUniKvizScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}