package com.cibertec.productapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cibertec.productapp.data.local.entity.ProductEntity

/**
 * DAO (Data Access Object) para operaciones de base de datos de productos
 */
@Dao
interface ProductDao {

    /**
     * Obtener todos los productos ordenados por ID descendente
     */
    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAllProducts(): LiveData<List<ProductEntity>>

    /**
     * Obtener un producto específico por su ID
     */
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): ProductEntity?

    /**
     * Insertar un nuevo producto
     * @return ID del producto insertado
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    /**
     * Actualizar un producto existente
     */
    @Update
    suspend fun updateProduct(product: ProductEntity)

    /**
     * Eliminar un producto
     */
    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    /**
     * Obtener productos que no están sincronizados con la API
     */
    @Query("SELECT * FROM products WHERE isSynced = 0")
    suspend fun getUnsyncedProducts(): List<ProductEntity>

    /**
     * Eliminar todos los productos
     */
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}
