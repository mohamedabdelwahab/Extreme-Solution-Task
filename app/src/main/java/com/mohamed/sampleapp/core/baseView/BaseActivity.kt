package com.mohamed.sampleapp.core.baseView

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.mohamed.sampleapp.core.baseView.ViewDialog
import com.mohamed.sampleapp.core.extensions.hideKeyboard
import com.mohamed.sampleapp.core.utilities.LocaleHelper
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    val binding by lazy { initBinding() }
    lateinit var progessDialog: ViewDialog

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setContent()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initBinding(): VB {
        val superclass = javaClass.genericSuperclass as ParameterizedType
        val method = (superclass.actualTypeArguments[0] as Class<Any>)
            .getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }

    private fun setContent() {
        setContentView(binding.root)
    }

    override fun onPause() {
        hideKeyboard(currentFocus)
        super.onPause()
    }

    fun showProgress() {
        if (!this::progessDialog.isInitialized)
            progessDialog = ViewDialog(this)
        progessDialog.showDialog()
    }

    fun hidProgress() {
        progessDialog.hideDialog()
    }

}