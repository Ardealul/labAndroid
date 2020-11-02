package com.ardeal.labandroid.todo.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.ardeal.labandroid.R
import com.ardeal.labandroid.core.TAG
import kotlinx.android.synthetic.main.fragment_product_edit.*

class ProductEditFragment : Fragment() {
    companion object {
        const val PRODUCT_ID = "PRODUCT_ID"
    }

    private lateinit var viewModel: ProductEditViewModel
    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(PRODUCT_ID)) {
                productId = it.getString(PRODUCT_ID).toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_product_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated")
        product_name.setText(productId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save product")
            viewModel.saveOrUpdateProduct(product_name.text.toString(), product_description.text.toString(), product_price.text.toString())
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(ProductEditViewModel::class.java)
        viewModel.product.observe(viewLifecycleOwner) { product ->
            Log.v(TAG, "update products")
            product_name.setText(product.name)
            product_description.setText(product.description)
            product_price.setText(product.price)
        }
        viewModel.fetching.observe(viewLifecycleOwner) { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        }
        viewModel.fetchingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = productId
        if (id != null) {
            viewModel.loadProduct(id)
        }
    }
}