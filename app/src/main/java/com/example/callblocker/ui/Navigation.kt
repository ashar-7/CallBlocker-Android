package com.example.callblocker.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.plusAssign
import com.example.callblocker.ui.addtoblocklist.AddToBlockListScreen
import com.example.callblocker.ui.blocklist.BlockListScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

const val CONTACT_ID_NAV_ARG_KEY = "contactId"

sealed class Screen(val route: String) {
    object BlockList : Screen(route = "blocklist")
    object AddToBlockList :
        Screen(route = "addtoblocklist?$CONTACT_ID_NAV_ARG_KEY={$CONTACT_ID_NAV_ARG_KEY}") {

        fun getRoute(id: Int?) = "addtoblocklist?$CONTACT_ID_NAV_ARG_KEY=${id ?: 0}"
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun Navigation(navController: NavHostController) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider += bottomSheetNavigator

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetElevation = 8.dp,
        scrimColor = MaterialTheme.colors.surface.copy(alpha = 0.5f)
    ) {
        NavHost(navController, startDestination = Screen.BlockList.route) {
            addBlockList(navController)
            addAddToBlockList(navController)
        }
    }
}

private fun NavGraphBuilder.addBlockList(navController: NavHostController) {
    composable(Screen.BlockList.route) {
        BlockListScreen(
            viewModel = hiltViewModel(),
            onAddNewContact = { contactId ->
                navController.navigate(Screen.AddToBlockList.getRoute(contactId))
            }
        )
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
private fun NavGraphBuilder.addAddToBlockList(navController: NavHostController) {
    // bottom sheet reappearing bug: https://github.com/google/accompanist/pull/741
    bottomSheet(
        Screen.AddToBlockList.route,
        arguments = listOf(navArgument(CONTACT_ID_NAV_ARG_KEY) {
            type = NavType.IntType
        })
    ) {
        AddToBlockListScreen(
            viewModel = hiltViewModel(),
            onDismiss = { navController.navigateUp() }
        )
    }
}
