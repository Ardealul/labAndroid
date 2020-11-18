package com.ardeal.labandroid.todo.products

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

class ProductListViewModel : ViewModel() {
    private val mutableProducts = MutableLiveData<List<Product>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val products: LiveData<List<Product>> = mutableProducts
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    fun loadProducts() {
        viewModelScope.launch {
            Log.v(TAG, "loadProducts...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = ProductRepository.loadAll()) {
                is Result.Success -> {
                    Log.d(TAG, "loadProducts succeeded");
                    mutableProducts.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "loadProducts failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}