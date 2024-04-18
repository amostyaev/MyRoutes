package com.raistlin.myroutes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.raistlin.myroutes.R
import com.raistlin.myroutes.db.PointEntity
import com.raistlin.myroutes.db.RoutesDao
import com.raistlin.myroutes.db.RoutesDatabase
import com.raistlin.myroutes.utils.Utils.onIOThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class NewPointFragment : Fragment() {

    private lateinit var databaseDao: RoutesDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        databaseDao = RoutesDatabase.getInstance(requireContext().applicationContext).routesDao()
        val view = inflater.inflate(R.layout.fragment_new_point, container, false)

        val textInputTitle = view.findViewById<TextInputLayout>(R.id.new_point_title)
        val textInputCoordinates = view.findViewById<TextInputLayout>(R.id.new_point_coordinates)

        view.findViewById<View>(R.id.new_point_save).setOnClickListener {
            try {
                val title = textInputTitle.editText?.text?.takeIf { it.isNotBlank() }?.toString() ?: throw IllegalArgumentException("Empty title")
                val coordinates = textInputCoordinates.editText?.text?.toString().orEmpty()
                val (lat, lon) = coordinates.split(",").map { it.trim() }.map { it.toDouble() }

                onIOThread {
                    databaseDao.savePoint(PointEntity(0, title, lat, lon))
                    withContext(Dispatchers.Main) {
                        findNavController().navigateUp()
                    }
                }
            } catch (e: Exception) {
                Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }

        return view
    }
}