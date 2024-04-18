package com.raistlin.myroutes.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.raistlin.myroutes.dto.Point
import com.raistlin.myroutes.dto.Route

@Entity
data class PointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val lat: Double,
    val lon: Double
) {
    fun toDto() = Point(id, title, lat, lon)

    companion object {
        fun fromDto(point: Point) = PointEntity(point.id, point.title, point.lat, point.lon)
    }
}

@Entity(
    foreignKeys = [ForeignKey(
        entity = PointEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("pointFromId"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = PointEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("pointToId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class RouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val pointFromId: Int,
    val pointToId: Int,
) {
    companion object {
        fun fromDto(route: Route) = RouteEntity(id = route.id, pointFromId = route.from.id, pointToId = route.to.id)
    }
}

data class RouteAndPoints(
    @Embedded
    val route: RouteEntity,
    @Relation(
        parentColumn = "pointFromId",
        entityColumn = "id"
    )
    val pointFrom: PointEntity,
    @Relation(
        parentColumn = "pointToId",
        entityColumn = "id"
    )
    val pointTo: PointEntity
) {

    fun toDto() = Route(id = route.id, from = pointFrom.toDto(), to = pointTo.toDto())

}

