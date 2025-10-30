package com.cibertec.productapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cibertec.productapp.R
import com.cibertec.productapp.data.local.entity.ProductEntity
import com.cibertec.productapp.data.remote.model.ProductRequest
import com.cibertec.productapp.data.repository.ProductRepository
import kotlinx.coroutines.launch

/**
 * ViewModel que expone LiveData para la UI y coordina operaciones con el repositorio.
 */
class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductRepository(application.applicationContext)
    private val context = application.applicationContext

    val products: LiveData<List<ProductEntity>> = repository.getAllProducts()

    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> = _statusMessage

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    // Obtener producto por ID (suspend) -> devuelve a través de callback
    fun getProductById(id: Long, callback: (ProductEntity?) -> Unit) {
        viewModelScope.launch {
            val p = repository.getProductById(id)
            callback(p)
        }
    }

    fun addProductLocal(product: ProductEntity, sendToApi: Boolean = false) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val newId = repository.insertProduct(product)
                if (sendToApi) {
                    // crear request y enviar
                    val request = ProductRequest(
                        title = product.title,
                        price = product.price,
                        description = product.description,
                        category = product.category,
                        image = product.image
                    )
                    val res = repository.createProductOnApi(request)
                    if (res.isSuccess) {
                        val body = res.getOrNull()!!
                        repository.updateProduct(
                            product.copy(id = newId, apiId = body.id, isSynced = true)
                        )
                    } else {
                        _statusMessage.value = context.getString(R.string.room_api_failed, res.exceptionOrNull()?.message)
                    }
                }
                _statusMessage.value = context.getString(R.string.product_saved)
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.save_error, e.message)
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateProduct(product: ProductEntity, updateOnApi: Boolean = false) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.updateProduct(product)
                if (updateOnApi && product.apiId != null) {
                    val request = ProductRequest(
                        title = product.title,
                        price = product.price,
                        description = product.description,
                        category = product.category,
                        image = product.image
                    )
                    val res = repository.updateProductOnApi(product.apiId, request)
                    if (res.isSuccess) {
                        _statusMessage.value = context.getString(R.string.api_updated)
                    } else {
                        _statusMessage.value = context.getString(R.string.api_failed_local_ok, res.exceptionOrNull()?.message)
                    }
                }
                _statusMessage.value = context.getString(R.string.product_updated)
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.update_error, e.message)
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.deleteProduct(product)
                if (product.apiId != null) {
                    // fire and ignore result
                    repository.deleteProductOnApi(product.apiId)
                }
                _statusMessage.value = context.getString(R.string.product_deleted)
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.delete_error, e.message)
            } finally {
                _loading.value = false
            }
        }
    }

    fun syncFromApi() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _statusMessage.value = "Conectando con FakeStore API..."
                val res = repository.fetchProductsFromApi()
                if (res.isSuccess) {
                    val list = res.getOrNull() ?: emptyList()
                    if (list.isNotEmpty()) {
                        // mapear e insertar en BD local
                        repository.deleteAllProducts()
                        for (item in list) {
                            repository.insertProduct(repository.mapResponseToEntity(item))
                        }
                        _statusMessage.value = context.getString(R.string.sync_complete) + " (${list.size} productos)"
                    } else {
                        _statusMessage.value = "API devolvió lista vacía"
                    }
                } else {
                    val error = res.exceptionOrNull()?.message ?: "Error desconocido"
                    _statusMessage.value = context.getString(R.string.sync_failed, error)
                }
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.sync_error, e.message)
            } finally {
                _loading.value = false
            }
        }
    }

    // Método para agregar productos de ejemplo para pruebas
    fun addSampleProducts() {
        viewModelScope.launch {
            try {
                val sampleProducts = listOf(
                    ProductEntity(
                        title = "Producto de Prueba 1",
                        price = 19.95,
                        description = "Este es un producto de prueba creado localmente",
                        category = "Prueba",
                        image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"
                    ),
                    ProductEntity(
                        title = "Producto de Prueba 2",
                        price = 39.99,
                        description = "Segundo producto de prueba",
                        category = "Prueba",
                        image = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg"
                    )
                )

                for (product in sampleProducts) {
                    repository.insertProduct(product)
                }

                _statusMessage.value = "Productos de prueba añadidos"
            } catch (e: Exception) {
                _statusMessage.value = "Error añadiendo productos de prueba: ${e.message}"
            }
        }
    }
}
