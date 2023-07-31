package com.mohamed.sampleapp.presentation.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.domain.usecase.cart.ClearCartProductsUseCase
import com.mohamed.sampleapp.domain.usecase.cart.GetCartProductsUseCase
import com.mohamed.sampleapp.domain.usecase.products.RemoveProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val removeProductUseCase: RemoveProductUseCase,
    private val getProductsUseCase: GetCartProductsUseCase,
    private val clearCartProductsUseCase: ClearCartProductsUseCase,
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun getCartProducts() {
        viewModelScope.launch {
            _products.value = getProductsUseCase()!!
        }
    }

    fun clearProducts() {
        viewModelScope.launch {
            clearCartProductsUseCase()
        }

    }

    fun removeProduct(product: Product) {
        viewModelScope.launch {
            removeProductUseCase(product)
            _products.value = getProductsUseCase()!!
        }
    }


}