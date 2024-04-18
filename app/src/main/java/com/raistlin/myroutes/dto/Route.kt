package com.raistlin.myroutes.dto

data class Point(val id: Int, val title: String, val lat: Double, val lon: Double)

data class Route(val id: Int, val from: Point, val to: Point)