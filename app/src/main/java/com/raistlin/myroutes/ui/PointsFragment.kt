package com.raistlin.myroutes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.raistlin.myroutes.R
import com.raistlin.myroutes.adapter.PointAdapter
import com.raistlin.myroutes.db.PointEntity
import com.raistlin.myroutes.db.RouteEntity
import com.raistlin.myroutes.db.RoutesDao
import com.raistlin.myroutes.db.RoutesDatabase
import com.raistlin.myroutes.utils.Utils.onIOThread
import kotlinx.coroutines.launch

class PointsFragment: Fragment() {

    private lateinit var databaseDao: RoutesDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        databaseDao = RoutesDatabase.getInstance(requireContext().applicationContext).routesDao()
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val adapter = PointAdapter {
            onIOThread {
                databaseDao.deletePoint(PointEntity.fromDto(it))
            }
        }
        view.findViewById<RecyclerView>(R.id.list).adapter = adapter

        view.findViewById<View>(R.id.add).setOnClickListener {
            findNavController().navigate(R.id.newPointFragment)
        }

        lifecycleScope.launch {
            databaseDao.getPoints().collect { points ->
                adapter.submitList(points.map { it.toDto() })
            }
        }

        return view
    }

}