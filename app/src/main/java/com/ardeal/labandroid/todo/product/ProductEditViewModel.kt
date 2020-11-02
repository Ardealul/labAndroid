package com.ardeal.labandroid.todo.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardeal.labandroid.core.TAG
import com.ardeal.labandroid.todo.data.Product
import com.ardeal.labandroid.todo.data.ProductRepository
import kotlinx.coroutines.launch

class ProductEditViewModel : ViewModel() {
    private val mutableProduct = MutableLiveData<Product>().apply { value = Product("", "", "", "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val product: LiveData<Product> = mutableProduct
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadProduct...")
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutableProduct.value = ProductRepository.load(productId)
                Log.i(TAG, "loadProduct succeeded")
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadProduct failed", e)
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }

    fun saveOrUpdateProduct(name: String, description: String, price: String) {
        viewModelScope.launch {
            Log.i(TAG, "saveOrUpdateProduct...");
            val product = mutableProduct.value ?: return@launch
            product.name = name
            product.description = description
            product.price = price
            mutableFetching.value = true
            mutableException.value = null
            try {
                if (product.id.isNotEmpty()) {
                    mutableProduct.value = ProductRepository.update(product)
                } else {
                    mutableProduct.value = ProductRepository.save(product)
                }
                Log.i(TAG, "saveOrUpdateProduct succeeded");
                mutableCompleted.value = true
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "saveOrUpdateProduct failed", e);
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }
}