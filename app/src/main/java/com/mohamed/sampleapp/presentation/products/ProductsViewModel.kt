package com.mohamed.sampleapp.presentation.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.domain.usecase.cart.GetCartProductsUseCase
import com.mohamed.sampleapp.domain.usecase.products.AddProductUseCase
import com.mohamed.sampleapp.domain.usecase.products.GetProductsUseCase
import com.mohamed.sampleapp.domain.usecase.products.RemoveProductUseCase
import com.mohamed.sampleapp.domain.usecase.products.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getCartUseCase: GetCartProductsUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val removeProductUseCase: RemoveProductUseCase,
) : ViewModel() {
    var allProducts: List<Product>? = null
    private val _searchProducts = MutableLiveData<Resource<List<Product>>>(Resource.Loading)
    val searchProducts: LiveData<Resource<List<Product>>> = _searchProducts

    fun getProducts(category: String) {
        viewModelScope.launch {
            _searchProducts.value = Resource.Loading
            _searchProducts.value = getProductsUseCase(category)!!
            if (_searchProducts.value is Resource.Success)
                allProducts = (_searchProducts.value as Resource.Success<List<Product>>).data
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            addProductUseCase(product)
            _cartProducts.value = getCartUseCase()!!
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            updateProductUseCase(product)
            _cartProducts.value = getCartUseCase()!!

        }
    }

    fun removeProduct(product: Product) {
        viewModelScope.launch {
            removeProductUseCase(product)
            _cartProducts.value = getCartUseCase()!!
        }
    }

    private val _cartProducts = MutableLiveData<List<Product>>()
    val cartProducts: LiveData<List<Product>> = _cartProducts

    fun getCartProducts() {
        viewModelScope.launch {
            _cartProducts.value = getCartUseCase()!!
        }
    }

    fun searchProducts(query: String) {
        _searchProducts.value = Resource.Success(allProducts?.filter {
            it.title?.lowercase()?.contains(query.lowercase()) ?: false
        } ?: emptyList())
    }

}