package com.raistlin.myroutes.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.raistlin.myroutes.R
import com.raistlin.myroutes.adapter.RouteAdapter
import com.raistlin.myroutes.adapter.RouteCallback
import com.raistlin.myroutes.db.RouteEntity
import com.raistlin.myroutes.db.RoutesDao
import com.raistlin.myroutes.db.RoutesDatabase
import com.raistlin.myroutes.dto.Route
import com.raistlin.myroutes.utils.Utils.onIOThread
import com.raistlin.myroutes.utils.Utils.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoutesFragment : Fragment() {

    private lateinit var databaseDao: RoutesDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        databaseDao = RoutesDatabase.getInstance(requireContext().applicationContext).routesDao()
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val adapter = RouteAdapter(object : RouteCallback {
            override fun onAction(route: Route) {
                startActivity(Intent(Intent.ACTION_VIEW, route.toUri()))
            }

            override fun onDelete(route: Route) {
                onIOThread {
                    databaseDao.deleteRoute(RouteEntity.fromDto(route))
                }
            }

        })
        view.findViewById<RecyclerView>(R.id.list).adapter = adapter

        view.findViewById<View>(R.id.add).setOnClickListener {
            findNavController().navigate(R.id.newRouteFragment)
        }

        lifecycleScope.launch {
            databaseDao.getRoutes().collect { routes ->
                adapter.submitList(routes.map { it.toDto() })
            }
        }

        return view
    }
}