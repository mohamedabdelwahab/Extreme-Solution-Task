package com.mohamed.sampleapp.presentation.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mohamed.sampleapp.R
import com.mohamed.sampleapp.core.common.delegate.viewBinding
import com.mohamed.sampleapp.core.extensions.gone
import com.mohamed.sampleapp.core.extensions.show
import com.mohamed.sampleapp.core.extensions.showErrorMessage
import com.mohamed.sampleapp.core.utilities.RecyclerTouchListener
import com.mohamed.sampleapp.core.utilities.networkUtils.Failure
import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private val binding by viewBinding(FragmentCategoryBinding::bind)

    private val categoryProductsViewModel: CategoryProductsViewModel by viewModels()

    private val categoryProductsAdapter by lazy { CategoryProductsAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryProductsViewModel.getCategories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.error.btnTryAgain.setOnClickListener {
            categoryProductsViewModel.getCategories()
        }
        binding.rvCategory.addOnItemTouchListener(
            RecyclerTouchListener(
                context,
                binding.rvCategory,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        findNavController().navigate(
                            CategoryFragmentDirections.actionCategoryToProductsFragment(
                                categoryProductsAdapter.getList()[position]
                            )
                        )
                    }

                    override fun onLongClick(view: View?, position: Int) {
                    }

                })
        )
        categoryProductsViewModel.categories.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.error.root.gone()
                    binding.progressBar.gone()
                    categoryProductsAdapter.updateList(it.data)
                    binding.rvCategory.adapter = categoryProductsAdapter
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

    private fun handleError(error: Failure) {
        if (error is Failure.NetworkConnection) {
            binding.error.root.show()
        } else {
            context?.showErrorMessage(error.toString())
        }
    }
}