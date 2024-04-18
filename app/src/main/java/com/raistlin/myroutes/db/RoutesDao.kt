package com.raistlin.myroutes.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePoint(point: PointEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRoute(route: RouteEntity)

    @Delete
    fun deletePoint(point: PointEntity)

    @Delete
    fun deleteRoute(route: RouteEntity)

    @Query("SELECT * FROM PointEntity")
    fun getPoints(): Flow<List<PointEntity>>

    @Transaction
    @Query("SELECT * FROM RouteEntity")
    fun getRoutes(): Flow<List<RouteAndPoints>>

}