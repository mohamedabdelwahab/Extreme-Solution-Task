package com.mohamed.sampleapp.presentation.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.sampleapp.core.extensions.hide
import com.mohamed.sampleapp.core.extensions.loadImage
import com.mohamed.sampleapp.core.extensions.show
import com.mohamed.sampleapp.data.source.remote.reposnse.Product
import com.mohamed.sampleapp.databinding.ProductItemBinding

class ProductsAdapter(var listner: ProductListener) :
    RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    private val list = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder =
        ProductsViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) =
        holder.bind(list[position])

    inner class ProductsViewHolder(private var binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.addToCart.setOnClickListener {
                binding.quantity.text = "1"
                binding.addedContainer.show()
                binding.addToCart.hide()
                list[layoutPosition].isSelected = true
                list[layoutPosition].quantiy = 1
                listner.onClickAddProduct(list[layoutPosition])
            }

            binding.plusQuantity.setOnClickListener {
                list[layoutPosition].quantiy += 1
                binding.quantity.text = list[layoutPosition].quantiy.toString()
                listner.onUpdateProduct(list[layoutPosition])
            }

            binding.minusQuantity.setOnClickListener {
                if (list[layoutPosition].quantiy == 1) {
                    binding.addedContainer.hide()
                    binding.addToCart.show()
                    listner.onRemoveProduct(list[layoutPosition])
                } else {
                    list[layoutPosition].quantiy -= 1
                    binding.quantity.text = list[layoutPosition].quantiy.toString()
                    listner.onUpdateProduct(list[layoutPosition])
                }
            }
        }

        fun bind(item: Product) {
            binding.productName.text = item.title
            binding.imageView.loadImage(item.image)
            binding.tvPrice.text = item.price.toString()
            if (item.rating?.rate != null) {
                binding.ratingBar.rating = item.rating.rate
                binding.rateUsers.text = "(${item.rating.count})"
                binding.ratingBar.show()
                binding.rateUsers.show()
            } else {
                binding.ratingBar.hide()
                binding.rateUsers.hide()
            }

            if (item.isSelected) {
                binding.quantity.text = list[layoutPosition].quantiy.toString()
                binding.addedContainer.show()
                binding.addToCart.hide()
            } else {
                binding.addedContainer.hide()
                binding.addToCart.show()
            }
        }
    }

    override fun getItemCount() = list.size

    fun updateList(updatedList: List<Product>) {
        list.clear()
        list.addAll(updatedList)
        notifyItemRangeInserted(0, updatedList.size)
    }

    interface ProductListener {
        fun onClickAddProduct(product: Product)
        fun onUpdateProduct(product: Product)
        fun onRemoveProduct(product: Product)
    }
}