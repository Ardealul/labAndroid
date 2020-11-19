package com.ardeal.labandroid.todo.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.ardeal.labandroid.R
import com.ardeal.labandroid.auth.data.AuthRepository
import com.ardeal.labandroid.core.Constants
import com.ardeal.labandroid.core.TAG
import kotlinx.android.synthetic.main.fragment_product_list.*

class ProductListFragment: Fragment() {
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var productsModel: ProductListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
//        if (!AuthRepository.isLoggedIn) {
//            findNavController().navigate(R.id.LoginFragment)
//            return;
//        }
        if(Constants.instance()?.fetchValueString("token") == null){
            findNavController().navigate(R.id.LoginFragment)
//            return;
        }
        setupProductList()
        fab.setOnClickListener {
            Log.v(TAG, "add new product")
            findNavController().navigate(R.id.ProductEditFragment)
        }
        logout.setOnClickListener{
            Log.v(TAG, "logout")
            AuthRepository.logout()
            findNavController().navigate(R.id.LoginFragment)
        }
    }

    private fun setupProductList() {
        productListAdapter = ProductListAdapter(this)
        product_list.adapter = productListAdapter
        productsModel = ViewModelProvider(this).get(ProductListViewModel::class.java)
        productsModel.products.observe(viewLifecycleOwner) { products ->
            Log.v(TAG, "update products")
            productListAdapter.products = products
        }
        productsModel.loading.observe(viewLifecycleOwner) { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        }
        productsModel.loadingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
        productsModel.loadProducts()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }
}