package com.mohamed.sampleapp.core.baseView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mohamed.sampleapp.core.extensions.hideKeyboard
import java.lang.reflect.ParameterizedType

abstract class BaseFragmentBinding<VB : ViewBinding> : Fragment() {

    private val binding by lazy { initBinding() }
    private val loadingDialog by lazy { activity?.let { ViewDialog(it) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dismissLoading()
        return binding.root
    }

    override fun onDestroy() {
        // Dismiss dialog before fragment destroyed otherwise IllegalArgumentException will arise.
        dismissLoading()
        super.onDestroy()
    }

    /**
     * Show loading dialog
     */
    fun showLoading() {
        loadingDialog?.showDialog()
    }

    /**
     * Dismiss loading dialog
     */
    fun dismissLoading() {
        // Make sure that fragment is alive otherwise IllegalArgumentException will arise.
        if (isDetached.not()) loadingDialog?.hideDialog()
    }


    @Suppress("UNCHECKED_CAST")
    private fun initBinding(): VB {
        val superclass = javaClass.genericSuperclass as ParameterizedType
        val method = (superclass.actualTypeArguments[0] as Class<Any>)
            .getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, layoutInflater) as VB
    }


    fun <T : Any> setBackStackResult(key: String, data: T, doBack: Boolean = true) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack) {
            findNavController().popBackStack()
        }
    }

    fun <T : Any> getBackStackResult(key: String, result: (T) -> (Unit)) {
        val observer =
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
        observer?.observe(viewLifecycleOwner) {
            it?.let {
                result(it)
            }
        }
        observer?.postValue(null)
    }

    override fun onDestroyView() {
        view?.hideKeyboard()
        loadingDialog?.hideDialog()
        super.onDestroyView()
//        _binding = null
    }

}