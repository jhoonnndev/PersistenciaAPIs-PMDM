package com.example.trabajonavegacionpmdm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trabajonavegacionpmdm.data.local.dao.ShopDao
import com.example.trabajonavegacionpmdm.data.local.entities.*

// Listamos las 5 entidades
@Database(
    entities = [
        BrandEntity::class,
        VehicleEntity::class,
        TechnicalSpecsEntity::class,
        ExtraFeatureEntity::class,
        VehicleExtraCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ShopDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDao //Implementamos el DAO

    companion object {
        @Volatile
        private var INSTANCE: ShopDatabase? = null

        fun getDatabase(context: Context): ShopDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ShopDatabase::class.java,
                    "shop_database" // Nombre del archivo de la BBDD
                )
                    .fallbackToDestructiveMigration() // Útil en desarrollo si cambias las tablas
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}