package com.raistlin.myroutes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RouteEntity::class, PointEntity::class], version = 1)
abstract class RoutesDatabase : RoomDatabase() {
    abstract fun routesDao(): RoutesDao

    companion object {
        @Volatile
        private var instance: RoutesDatabase? = null

        fun getInstance(context: Context): RoutesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, RoutesDatabase::class.java, "routes.db").let {
                if (context.resources.assets.list("")?.contains("routes.db") == true) {
                    it.createFromAsset("routes.db")
                }
                it.build()
            }
    }

}