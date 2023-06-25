package com.apprajapati.myanimations.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.apprajapati.myanimations.databinding.FragmentRotatingSquareTimeBinding
import com.apprajapati.myanimations.util.NetworkResult
import com.apprajapati.myanimations.util.getTimeOnly
import com.apprajapati.myanimations.util.getTimeOnlyTimeApi
import com.apprajapati.myanimations.util.rotateViewUsingObjectAnimator
import com.apprajapati.myanimations.viewmodels.MainViewModel
import java.util.Timer
import java.util.TimerTask

class RotatingSquareTime : Fragment() {

    private var _binding: FragmentRotatingSquareTimeBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    private var timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRotatingSquareTimeBinding.inflate(inflater, container, false)


        viewModel.time.observe(viewLifecycleOwner) { response ->
            when(response) {
                is NetworkResult.Success -> {
                    val time = response.data
                    binding.textViewTime.text = getTimeOnly(time?.datetime!!)
                }
                is NetworkResult.Error -> {
                    val message = response.message
                    binding.textViewTime.text =  message
                    timer.cancel()
                }
                is NetworkResult.Loading -> binding.textViewTime.text =  "Loading"
            }
        }

        viewModel.timeApi.observe(viewLifecycleOwner) {
                response ->
            when(response) {
                is NetworkResult.Success -> {
                    val time = response.data
                    binding.textViewTime.text = getTimeOnlyTimeApi(time?.dateTime!!)
                }
                is NetworkResult.Error -> {
                    val message = response.message
                    binding.textViewTime.text =  message
                    timer.cancel()
                }
                is NetworkResult.Loading -> binding.textViewTime.text =  "Loading"
            }
        }

        val animator = rotateViewUsingObjectAnimator(binding.textViewTime)
        animator.start()
        animator.end()

        binding.requestButton.setOnClickListener {
            viewModel.getTime()
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onResume() {
        super.onResume()
        val task = object : TimerTask() {
            override fun run() {
                viewModel.getTime()
            }

        }
        timer = Timer()
        timer.scheduleAtFixedRate(task, 0, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        timer.purge()
        _binding = null
    }
}