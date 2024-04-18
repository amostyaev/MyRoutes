package com.raistlin.myroutes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raistlin.myroutes.R
import com.raistlin.myroutes.dto.Route

interface RouteCallback {
    fun onAction(route: Route)
    fun onDelete(route: Route)
}

class RouteAdapter(private val callback: RouteCallback) : ListAdapter<Route, RouteViewHolder>(RouteDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        return RouteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false), callback)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class RouteViewHolder(view: View, private val callback: RouteCallback) : RecyclerView.ViewHolder(view) {

    private val from = view.findViewById<TextView>(R.id.route_from)
    private val to = view.findViewById<TextView>(R.id.route_to)
    private val menu = view.findViewById<View>(R.id.route_menu)
    private val action = view.findViewById<View>(R.id.route_action)

    fun bind(route: Route) {
        from.text = route.from.title
        to.text = route.to.title
        menu.setOnClickListener {
            PopupMenu(from.context, it).apply {
                inflate(R.menu.menu_item)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_delete -> {
                            callback.onDelete(route)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
        action.setOnClickListener {
            callback.onAction(route)
        }
    }

}

object RouteDiffUtil : DiffUtil.ItemCallback<Route>() {

    override fun areItemsTheSame(oldItem: Route, newItem: Route) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Route, newItem: Route) = oldItem.id == newItem.id

}