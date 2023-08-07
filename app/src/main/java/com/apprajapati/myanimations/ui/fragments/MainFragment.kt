package com.apprajapati.myanimations.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apprajapati.myanimations.R
import com.apprajapati.myanimations.databinding.FragmentMainBinding

/**
 * @Author: Ajay P. Prajapati (https://github.com/apprajapati9)
 * @Date: 10,January,2022
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rollDiceButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_RollDiceFragment)
        }

//        binding.snowAnimation.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFragment_to_SnowFragment)
//        }
        binding.snowAnimation.visibility = View.GONE

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}