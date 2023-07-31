package com.mohamed.sampleapp.presentation.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.sampleapp.databinding.CategoryItemBinding

class CategoryProductsAdapter : RecyclerView.Adapter<CategoryProductsAdapter.ProductsViewHolder>() {

    private val list = ArrayList<String>()
    fun getList(): ArrayList<String> {
        return list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder =
        ProductsViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) =
        holder.bind(list[position])

    inner class ProductsViewHolder(private var binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.root.text = item

            binding.root.setOnClickListener {

            }
        }
    }

    override fun getItemCount() = list.size

    fun updateList(updatedList: List<String>) {
        list.clear()
        list.addAll(updatedList)
        notifyItemRangeInserted(0, updatedList.size)
    }
}