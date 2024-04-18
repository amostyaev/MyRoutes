package com.raistlin.myroutes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.raistlin.myroutes.R
import com.raistlin.myroutes.db.PointEntity
import com.raistlin.myroutes.db.RouteEntity
import com.raistlin.myroutes.db.RoutesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.main_toolbar)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setSupportActionBar(toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.routesFragment, R.id.pointsFragment),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val subMode = destination.id == R.id.newRouteFragment
            bottomNavigation.isVisible = !subMode
        }
        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNavigation.setupWithNavController(navController)

//        lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                RoutesDatabase.getInstance(applicationContext).routesDao().apply {
//                    savePoint(PointEntity(1, "Дом", 55.643160, 37.486050))
//                    savePoint(PointEntity(2, "Работа", 55.698736, 37.531406))
//                    savePoint(PointEntity(3, "Пересадка", 55.625357, 37.489248))
//                    savePoint(PointEntity(4, "Лобня", 56.015364, 37.462514))
//                    savePoint(PointEntity(5, "Рыбинск", 58.028569, 38.866392))
//                    savePoint(PointEntity(6, "Дача", 57.363420, 39.917231))
//                    savePoint(PointEntity(7, "Катя", 56.062610, 37.495222))
//
//                    saveRoute(RouteEntity(1, 1, 2))
//                    saveRoute(RouteEntity(2, 2, 1))
//                    saveRoute(RouteEntity(3, 2, 3))
//                    saveRoute(RouteEntity(4, 1, 4))
//                    saveRoute(RouteEntity(5, 1, 7))
//                    saveRoute(RouteEntity(6, 1, 5))
//                    saveRoute(RouteEntity(7, 1, 6))
//
//                    RoutesDatabase.getInstance(applicationContext).close()
//                }
//            }
//        }
    }
}