package com.raistlin.myroutes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.raistlin.myroutes.R
import com.raistlin.myroutes.db.RouteEntity
import com.raistlin.myroutes.db.RoutesDao
import com.raistlin.myroutes.db.RoutesDatabase
import com.raistlin.myroutes.dto.Point
import com.raistlin.myroutes.utils.Utils.onIOThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewRouteFragment : Fragment() {

    private lateinit var databaseDao: RoutesDao
    private lateinit var points: List<Point>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        databaseDao = RoutesDatabase.getInstance(requireContext().applicationContext).routesDao()
        val view = inflater.inflate(R.layout.fragment_new_route, container, false)

        val textInputTo = view.findViewById<TextInputLayout>(R.id.new_route_point_to)
        val textInputFrom = view.findViewById<TextInputLayout>(R.id.new_route_point_from)

        view.findViewById<View>(R.id.new_route_save).setOnClickListener {
            if (points.isEmpty()) return@setOnClickListener

            val fromText = textInputFrom.editText?.text?.toString().orEmpty()
            val from = points.find { it.title == fromText } ?: points.first()
            val toText = textInputTo.editText?.text?.toString().orEmpty()
            val to = points.find { it.title == toText } ?: points.first()

            onIOThread {
                databaseDao.saveRoute(RouteEntity(0, from.id, to.id))
                withContext(Dispatchers.Main) {
                    findNavController().navigateUp()
                }
            }
        }
        lifecycleScope.launch {
            points = databaseDao.getPoints().first().map { it.toDto() }
            if (points.isEmpty()) return@launch

            val items = points.map { it.title }.toTypedArray()
            (textInputTo.editText as? MaterialAutoCompleteTextView)?.apply {
                setSimpleItems(items)
                setText(items.first(), false)
            }
            (textInputFrom.editText as? MaterialAutoCompleteTextView)?.apply {
                setSimpleItems(items)
                setText(items.first(), false)
            }
        }

        return view
    }
}