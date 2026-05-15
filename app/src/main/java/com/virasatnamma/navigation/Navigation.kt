package com.virasatnamma.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.virasatnamma.data.local.VirasatDatabase
import com.virasatnamma.data.repository.HeritageRepository
import com.virasatnamma.ui.screens.HomeScreen
import com.virasatnamma.ui.screens.PassportScreen
import com.virasatnamma.ui.screens.ScannerScreen
import com.virasatnamma.ui.screens.SiteDetailsScreen
import com.virasatnamma.ui.theme.VirasatColors
import com.virasatnamma.viewmodel.HomeViewModel
import com.virasatnamma.viewmodel.PassportViewModel
import com.virasatnamma.viewmodel.ScannerViewModel
import com.virasatnamma.viewmodel.SiteDetailsViewModel

sealed class NavRoute(val route: String) {
    object Home : NavRoute("home")
    object Scanner : NavRoute("scanner")
    object Passport : NavRoute("passport")
    object SiteDetails : NavRoute("site_details/{siteId}") {
        fun createRoute(siteId: String) = "site_details/$siteId"
    }
}

@Composable
fun AppNavigation(
    database: VirasatDatabase,
    navController: NavHostController = rememberNavController()
) {
    val repository = HeritageRepository(
        database.siteDao(),
        database.checkInDao()
    )
    
    val homeViewModel = remember { HomeViewModel(repository) }
    val scannerViewModel = remember { ScannerViewModel(repository) }
    val passportViewModel = remember { PassportViewModel(repository) }
    val siteDetailsViewModel = remember { SiteDetailsViewModel(repository) }
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(NavRoute.Home.route, NavRoute.Scanner.route, NavRoute.Passport.route)) {
                FloatingTempleNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        },
        containerColor = VirasatColors.SoftCream
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = NavRoute.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(NavRoute.Home.route) {
                    HomeScreen(
                        viewModel = homeViewModel,
                        onSiteClick = { id -> navController.navigate(NavRoute.SiteDetails.createRoute(id)) },
                        onScannerClick = { navController.navigate(NavRoute.Scanner.route) },
                        onPassportClick = { navController.navigate(NavRoute.Passport.route) }
                    )
                }
                composable(NavRoute.Scanner.route) {
                    ScannerScreen(
                        viewModel = scannerViewModel,
                        onSiteFound = { id -> navController.navigate(NavRoute.SiteDetails.createRoute(id)) },
                        onScanReset = {}
                    )
                }
                composable(NavRoute.Passport.route) {
                    PassportScreen(
                        viewModel = passportViewModel,
                        onSiteClick = { id, _ -> navController.navigate(NavRoute.SiteDetails.createRoute(id)) }
                    )
                }
                composable(NavRoute.SiteDetails.route) { backStackEntry ->
                    val siteId = backStackEntry.arguments?.getString("siteId") ?: return@composable
                    SiteDetailsScreen(
                        siteId = siteId,
                        viewModel = siteDetailsViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingTempleNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = VirasatColors.AntiqueGold,
                    spotColor = VirasatColors.AntiqueGold
                ),
            color = VirasatColors.TempleBrown,
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Brush.verticalGradient(
                listOf(VirasatColors.AntiqueGold, Color.Transparent)
            ))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val items = listOf(
                    NavItem("Pilgrimage", Icons.Default.Home, NavRoute.Home.route),
                    NavItem("Gateway", Icons.Default.QrCodeScanner, NavRoute.Scanner.route),
                    NavItem("Scroll", Icons.Default.MilitaryTech, NavRoute.Passport.route)
                )

                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    
                    val animatedSize by animateDpAsState(
                        targetValue = if (isSelected) 34.dp else 26.dp,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
                    
                    val animatedColor by animateColorAsState(
                        targetValue = if (isSelected) VirasatColors.AntiqueGold else Color.White.copy(alpha = 0.6f)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onNavigate(item.route) }
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = animatedColor,
                            modifier = Modifier.size(animatedSize)
                        )
                        AnimatedVisibility(
                            visible = isSelected,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Text(
                                text = item.label.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = VirasatColors.AntiqueGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class NavItem(val label: String, val icon: ImageVector, val route: String)
