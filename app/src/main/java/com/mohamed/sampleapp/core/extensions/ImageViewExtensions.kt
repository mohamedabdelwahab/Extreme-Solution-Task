package com.mohamed.sampleapp.core.extensions

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.mohamed.sampleapp.R

fun AppCompatImageView.loadImage(
    url: String?,
    defaultImg: Int = R.drawable.ic_default
) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    Glide.with(context).load(url).placeholder(circularProgressDrawable)
        .error(defaultImg).into(this)
}

fun AppCompatImageView.loadImageSlider(
    context: Context,
    url: String?,
    defaultImg: Int = R.drawable.ic_default
) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    Glide.with(context).load(url).placeholder(circularProgressDrawable)
        .error(defaultImg).into(this)
}