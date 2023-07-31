package com.mohamed.sampleapp.presentation.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.domain.usecase.category.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _categories = MutableLiveData<Resource<List<String>>>(Resource.Loading)
    val categories: LiveData<Resource<List<String>>> = _categories

    fun getCategories() {
        viewModelScope.launch {
            _categories.value = Resource.Loading
            _categories.value = getCategoriesUseCase()!!
        }
    }
}