@file:Suppress("IncorrectScope")

package com.mohamed.sampleapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mohamed.sampleapp.data.source.local.LocalDataSourceImpl
import com.mohamed.sampleapp.data.source.local.ProductsRoomDB
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith

class ProductsRoomRepositoryTest {
    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    val exceptionRule = ExpectedException.none()

    private lateinit var db: ProductsRoomDB

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ProductsRoomDB::class.java)
            .allowMainThreadQueries().build()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun test_getUpcomingTodoCountEmpty() = runBlocking {
        val dao = spy(db.productDAO())
        val repo = LocalDataSourceImpl(dao, Dispatchers.Default)
        val expected = 0

        val actual = repo.getCartProducts()?.size

        assertEquals(expected, actual)
    }

    @Test
    fun test_getUpcomingTodoCountSingleMatch() = runBlocking {
        db.productDAO().addToCart(mock())
        val dao = spy(db.productDAO())
        val repo = LocalDataSourceImpl(dao, Dispatchers.Default)
        val expected = 1

        val actual = repo.getCartProducts()?.size

        assertEquals(expected, actual)
    }

    @Test
    fun test_deleteProduct() = runBlocking {
        val product = Product(1)
        db.productDAO().addToCart(product)
        db.productDAO().addToCart(mock())
        val dao = spy(db.productDAO())
        val repo = LocalDataSourceImpl(dao, Dispatchers.Default)

        repo.deleteFromCart(1)

        val actual = repo.getCartProducts()

        assertEquals(1, actual)

        Assert.assertTrue(actual?.contains(product) == false)

    }

}