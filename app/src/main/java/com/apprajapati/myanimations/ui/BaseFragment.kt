package com.apprajapati.myanimations.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/*
Common base fragment to avoid rewriting binding logic for every fragment.
Passing higher order function Inflate from LayoutInflater.
 */
abstract class BaseFragment<VB : ViewBinding>(private val bindingInflater: (inflater: LayoutInflater) -> VB) :
    Fragment() {

    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater) //bindingInflater(inflater)

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}