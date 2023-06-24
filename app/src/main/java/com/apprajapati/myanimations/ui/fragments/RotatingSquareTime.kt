package com.apprajapati.myanimations.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apprajapati.myanimations.databinding.FragmentRotatingSquareTimeBinding
import com.apprajapati.myanimations.util.getTimeOnly
import com.apprajapati.myanimations.viewmodels.MainViewModel

class RotatingSquareTime : Fragment() {

    private var _binding: FragmentRotatingSquareTimeBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

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


        viewModel.getTime()

        viewModel.time.observe(viewLifecycleOwner, {
            binding.textViewTime.text = getTimeOnly(it.datetime)
            //binding.textViewTime.text = it.datetime
        })

        binding.requestButton.setOnClickListener {
            viewModel.getTime()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}