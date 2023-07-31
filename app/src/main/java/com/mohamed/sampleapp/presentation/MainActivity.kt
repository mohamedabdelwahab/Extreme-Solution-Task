package com.mohamed.sampleapp.presentation

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.mohamed.sampleapp.R
import com.mohamed.sampleapp.core.common.delegate.viewBinding
import com.mohamed.sampleapp.core.utilities.Helper.setBadgeCount
import com.mohamed.sampleapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    var cartCount = 0
    private val binding by viewBinding(ActivityMainBinding::inflate)
    lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
        }
        mainViewModel.getCartProducts()
        mainViewModel.products.observe(this) {
            cartCount = it.size
            invalidateMenu()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_cart) {
            navHostFragment.navController.navigate(R.id.cartProductsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //inflate menu
        menuInflater.inflate(R.menu.menu_cart, menu)
        // Get the notifications MenuItem and LayerDrawable (layer-list)
        val cartItem = menu.findItem(R.id.action_cart)
        cartItem.isVisible = true
        val icon = cartItem.icon as LayerDrawable?
        // Update LayerDrawable's BadgeDrawable
        setBadgeCount(this, icon, cartCount)
        return true
    }

    fun updateCartNumber() {
        mainViewModel.getCartProducts()
    }

}