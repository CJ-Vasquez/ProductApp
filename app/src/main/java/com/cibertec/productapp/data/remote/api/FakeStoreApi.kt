package com.cibertec.productapp.data.remote.api

import com.cibertec.productapp.data.remote.model.ProductRequest
import com.cibertec.productapp.data.remote.model.ProductResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface de la API FakeStore para operaciones REST
 */
interface FakeStoreApi {

    /**
     * Obtiene todos los productos de la API
     */
    @GET("products")
    suspend fun getAllProducts(): Response<List<ProductResponse>>

    /**
     * Obtiene un producto específico por su ID
     */
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductResponse>

    /**
     * Crea un nuevo producto en la API
     */
    @POST("products")
    suspend fun createProduct(@Body product: ProductRequest): Response<ProductResponse>

    /**
     * Actualiza un producto existente en la API
     */
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: ProductRequest
    ): Response<ProductResponse>

    /**
     * Elimina un producto de la API
     */
    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<ProductResponse>
}
