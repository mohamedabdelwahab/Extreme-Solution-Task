@file:Suppress("IncorrectScope")

package com.mohamed.sampleapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.domain.repository.ProductsRepository
import com.mohamed.sampleapp.domain.usecase.products.GetProductsUseCase
import com.mohamed.sampleapp.presentation.products.ProductsViewModel
import com.nhaarman.mockitokotlin2.mock
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.Mockito

class SearchProductsViewModelTest {
    @get:Rule
    val exceptionRule = ExpectedException.none()
    private lateinit var viewModel: ProductsViewModel

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() = runTest {
        val repository: ProductsRepository = mock(verboseLogging = true)
        Mockito.`when`(repository.getProductsByCategory(""))
            .thenReturn(
                Resource.Success(
                    arrayListOf(
                        Product(title = "title"),
                        Product(title = "title2"),
                        Product(title = "title3"),
                    )
                )
            )
        viewModel =
            ProductsViewModel(GetProductsUseCase(repository), mock(), mock(), mock(), mock())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_searchListAllExist() = runTest {
        viewModel.getProducts("")
        viewModel.searchProducts("title")
        val todos = (viewModel.searchProducts.value as Resource.Success).data
        assertNotNull(todos)
        assertEquals(3, todos.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_searchListOneExist() = runTest {
        viewModel.getProducts("")
        viewModel.searchProducts("title2")

        val todos = (viewModel.searchProducts.value as Resource.Success).data
        assertNotNull(todos)
        assertEquals(1, todos.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_searchListNotExist() = runTest {
        viewModel.getProducts("")
        viewModel.searchProducts("fake")

        val todos = (viewModel.searchProducts.value as Resource.Success).data
        assertNotNull(todos)
        assertEquals(0, todos.size)
    }

}