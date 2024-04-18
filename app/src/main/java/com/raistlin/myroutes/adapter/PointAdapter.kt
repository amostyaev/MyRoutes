package com.raistlin.myroutes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raistlin.myroutes.R
import com.raistlin.myroutes.dto.Point

typealias PointCallback = (Point) -> Unit

class PointAdapter(private val callback: PointCallback) : ListAdapter<Point, PointViewHolder>(PointDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        return PointViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_point, parent, false), callback)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PointViewHolder(view: View, private val callback: PointCallback) : RecyclerView.ViewHolder(view) {

    private val title = view.findViewById<TextView>(R.id.point_title)
    private val coordinates = view.findViewById<TextView>(R.id.point_coordinates)
    private val menu = view.findViewById<View>(R.id.point_menu)

    @SuppressLint("SetTextI18n")
    fun bind(point: Point) {
        title.text = point.title
        coordinates.text = "${point.lat}, ${point.lon}"
        menu.setOnClickListener {
            PopupMenu(title.context, it).apply {
                inflate(R.menu.menu_item)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_delete -> {
                            callback(point)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
    }

}

object PointDiffUtil : DiffUtil.ItemCallback<Point>() {

    override fun areItemsTheSame(oldItem: Point, newItem: Point) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Point, newItem: Point) = oldItem.id == newItem.id

}