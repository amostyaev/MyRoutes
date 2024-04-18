package com.raistlin.myroutes.utils

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.raistlin.myroutes.dto.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Utils {

    fun Route.toUri(): Uri = Uri.parse("yandexmaps://build_route_on_map/?lat_to=${to.lat}&lon_to=${to.lon}&lat_from=${from.lat}&lon_from=${from.lon}&rtt=auto")

    fun <T> Fragment.onIOThread(action: suspend () -> T) = lifecycleScope.launch { withContext(Dispatchers.IO) { action() } }

}