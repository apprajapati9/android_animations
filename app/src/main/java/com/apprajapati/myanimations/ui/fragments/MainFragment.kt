package com.apprajapati.myanimations.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.apprajapati.myanimations.R
import com.apprajapati.myanimations.databinding.FragmentMainBinding
import com.apprajapati.myanimations.ui.BaseFragment

/**
 * @Author: Ajay P. Prajapati (https://github.com/apprajapati9)
 * @Date: 10,January,2022
 */
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rollDiceButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_RollDiceFragment)
        }

        binding.snowAnimation.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_SnowFragment)
        }

        binding.animationButton.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_androidAnimations)
        }

        binding.rotateSquareAnimation.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_RotateSquareFragment)
        }

        binding.objectAnimatorButton.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_objectAnimatorFragment)
        }

        binding.rotateSquareTime.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_rotatingSquareTime)
        }

        binding.rotatingBallCustomView.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_bouncingBallViewFragment)
        }
    }


}