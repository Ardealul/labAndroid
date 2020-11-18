package com.ardeal.labandroid.todo.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ardeal.labandroid.core.TAG
import com.ardeal.labandroid.core.Result
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
            when (val result = ProductRepository.load(productId)) {
                is Result.Success -> {
                    Log.d(TAG, "loadProduct succeeded")
                    mutableProduct.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "loadProduct failed", result.exception)
                    mutableException.value = result.exception
                }
            }
            mutableFetching.value = false
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
            val result: Result<Product>
            if (product._id.isNotEmpty()) {
                result = ProductRepository.update(product)
            } else {
                result = ProductRepository.save(product)
            }
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateProduct succeeded");
                    mutableProduct.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateProduct failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}