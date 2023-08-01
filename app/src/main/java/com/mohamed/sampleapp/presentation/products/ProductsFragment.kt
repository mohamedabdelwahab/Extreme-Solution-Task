package com.mohamed.sampleapp.presentation.products

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mohamed.sampleapp.R
import com.mohamed.sampleapp.core.common.delegate.viewBinding
import com.mohamed.sampleapp.core.extensions.gone
import com.mohamed.sampleapp.core.extensions.show
import com.mohamed.sampleapp.core.extensions.showErrorMessage
import com.mohamed.sampleapp.core.utilities.networkUtils.Failure
import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.databinding.FragmentProductsBinding
import com.mohamed.sampleapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {
    val args by navArgs<ProductsFragmentArgs>()

    private val binding by viewBinding(FragmentProductsBinding::bind)

    private val productsViewModel: ProductsViewModel by viewModels()

    private val productsAdapter by lazy {
        ProductsAdapter(object : ProductsAdapter.ProductListener {
            override fun onClickAddProduct(product: Product) {
                productsViewModel.addProduct(product)
            }

            override fun onUpdateProduct(product: Product) {
                productsViewModel.updateProduct(product)
            }

            override fun onRemoveProduct(product: Product) {
                productsViewModel.removeProduct(product)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productsViewModel.getProducts(args.category)
        productsViewModel.getCartProducts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListners()
        observeLiveData()

    }

    private fun observeLiveData() {
        productsViewModel.cartProducts.observe(viewLifecycleOwner) {
            (activity as MainActivity).updateCartNumber()
            if (it.isEmpty()) {
                binding.selectedShadow.gone()
                binding.addedItems.gone()
            } else {
                binding.selectedShadow.show()
                binding.addedItems.show()
                binding.addedCount.text = it.size.toString()
                calculateTotal(it)
            }
        }

        productsViewModel.searchProducts.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.error.root.gone()
                    binding.progressBar.gone()
                    productsAdapter.updateList(it.data)
                    binding.rvCategory.adapter = productsAdapter
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    handleError(it.error)
                }
                Resource.Loading -> {
                    binding.error.root.gone()
                    binding.progressBar.show()
                }
            }
        }
    }

    private fun addListners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    productsViewModel.searchProducts(it)
                }
                return false
            }
        })
        binding.addedItems.setOnClickListener {
            findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToCartProductsFragment())
        }
        binding.error.btnTryAgain.setOnClickListener {
            productsViewModel.getProducts(args.category)
        }
    }

    private fun handleError(error: Failure) {
        if (error is Failure.NetworkConnection) {
            binding.error.root.show()
        } else {
            context?.showErrorMessage(error.toString())
        }
    }

    private fun calculateTotal(products: List<Product>) {
        val costs = products.map { it.price * it.quantiy }.sum()
        binding.addedTotalPrices.text = costs.toString()
    }

}