package com.cibertec.productapp.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.cibertec.productapp.data.local.dao.ProductDao
import com.cibertec.productapp.data.local.dao.CartDao
import com.cibertec.productapp.data.local.dao.OrderDao
import com.cibertec.productapp.data.local.entity.ProductEntity
import com.cibertec.productapp.data.local.entity.CartItemEntity
import com.cibertec.productapp.data.local.entity.OrderEntity

/**
 * Base de datos Room de la aplicación
 * Implementa el patrón Singleton para garantizar una sola instancia
 */
@Database(
    entities = [ProductEntity::class, CartItemEntity::class, OrderEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Proporciona acceso al DAO de productos
     */
    abstract fun productDao(): ProductDao

    /**
     * Proporciona acceso al DAO del carrito
     */
    abstract fun cartDao(): CartDao

    /**
     * Proporciona acceso al DAO de órdenes
     */
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos
         * Utiliza el patrón Singleton con double-checked locking
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
