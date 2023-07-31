package com.mohamed.sampleapp.presentation.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.sampleapp.core.extensions.loadImage
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.databinding.CartProductItemBinding

class CartProductAdapter(private val itemDeleted: (Product) -> Unit) :
    RecyclerView.Adapter<CartProductAdapter.ProductsViewHolder>() {

    private val list = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder =
        ProductsViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) =
        holder.bind(list[position])

    inner class ProductsViewHolder(private var binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivDeleteItem.setOnClickListener {
                itemDeleted(list[layoutPosition])
            }
        }

        fun bind(item: Product) {
            binding.productName.text = item.title
            binding.quantity.text = list[layoutPosition].quantiy.toString()
            binding.imageView.loadImage(item.image)
            binding.unitPrice.text = item.price.toString()
        }
    }

    override fun getItemCount() = list.size

    fun updateList(updatedList: List<Product>) {
        list.clear()
        list.addAll(updatedList)
        notifyItemRangeInserted(0, updatedList.size)
    }

}