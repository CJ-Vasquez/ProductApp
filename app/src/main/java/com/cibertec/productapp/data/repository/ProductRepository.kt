package com.cibertec.productapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.cibertec.productapp.data.local.database.AppDatabase
import com.cibertec.productapp.data.local.entity.ProductEntity
import com.cibertec.productapp.data.remote.RetrofitInstance
import com.cibertec.productapp.data.remote.model.ProductRequest
import com.cibertec.productapp.data.remote.model.ProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio que actúa como única fuente de verdad para datos de productos.
 * Encapsula las operaciones con Room (local) y Retrofit (remoto).
 */
class ProductRepository(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val productDao = db.productDao()
    private val api = RetrofitInstance.api

    // --- Operaciones locales (Room) ---

    fun getAllProducts(): LiveData<List<ProductEntity>> = productDao.getAllProducts()

    suspend fun getProductById(id: Long): ProductEntity? = withContext(Dispatchers.IO) {
        productDao.getProductById(id)
    }

    suspend fun insertProduct(product: ProductEntity): Long = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.deleteProduct(product)
    }

    suspend fun getUnsyncedProducts(): List<ProductEntity> = withContext(Dispatchers.IO) {
        productDao.getUnsyncedProducts()
    }

    suspend fun deleteAllProducts() = withContext(Dispatchers.IO) {
        productDao.deleteAllProducts()
    }

    // --- Operaciones remotas (Retrofit) ---

    suspend fun fetchProductsFromApi(): Result<List<ProductResponse>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.getAllProducts()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProductOnApi(request: ProductRequest): Result<ProductResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.createProduct(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProductOnApi(id: Int, request: ProductRequest): Result<ProductResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.updateProduct(id, request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProductOnApi(id: Int): Result<ProductResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.deleteProduct(id)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Helper: map ProductResponse -> ProductEntity
    fun mapResponseToEntity(response: ProductResponse): ProductEntity {
        return ProductEntity(
            apiId = response.id,
            title = response.title,
            price = response.price,
            description = response.description,
            category = response.category,
            image = response.image,
            isSynced = true
        )
    }
}
