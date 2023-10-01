package com.apprajapati.myanimations.ui.fragments.progressview

import android.os.Bundle
import android.view.View
import com.apprajapati.myanimations.databinding.FragmentProgressViewBinding
import com.apprajapati.myanimations.ui.BaseFragment

class ProgressViewFragment : BaseFragment<FragmentProgressViewBinding>(FragmentProgressViewBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hideShowButton.setOnClickListener {
            if(binding.loadingView.visibility == View.VISIBLE){
                binding.loadingView.visibility = View.GONE
            }else{
                binding.loadingView.visibility = View.VISIBLE
            }
        }
    }
}