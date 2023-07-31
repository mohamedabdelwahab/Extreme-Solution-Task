package com.mohamed.sampleapp.presentation.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mohamed.sampleapp.R
import com.mohamed.sampleapp.core.common.delegate.viewBinding
import com.mohamed.sampleapp.core.extensions.showSuccessMessage
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.databinding.FragmentCartBinding
import com.mohamed.sampleapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartProductsFragment : Fragment(R.layout.fragment_cart) {

    private val binding by viewBinding(FragmentCartBinding::bind)

    private val cartViewModel: CartViewModel by viewModels()

    private val cartProductAdapter by lazy {
        CartProductAdapter {
            cartViewModel.removeProduct(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartViewModel.getCartProducts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.products.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                findNavController().navigateUp()
                return@observe
            }
            cartProductAdapter.updateList(it)
            calculateTotal(it)
            (activity as MainActivity).updateCartNumber()
            binding.rvCategory.adapter = cartProductAdapter
        }
        binding.btnCheckOut.setOnClickListener {
            cartViewModel.clearProducts()
            context?.showSuccessMessage("Save Sucess") {
                findNavController().navigate(CartProductsFragmentDirections.actionCartProductsFragmentToCategory())
            }
        }
    }

    private fun calculateTotal(products: List<Product>) {
        val costs = products.map { it.price * it.quantiy }.sum()
        binding.tvTotalAmounttValue.text = costs.toString()
    }
}