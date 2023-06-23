package com.apprajapati.myanimations

import android.animation.ValueAnimator
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apprajapati.myanimations.databinding.FragmentAndroidAnimationsBinding

class AndroidAnimations : Fragment() {

    private var _binding: FragmentAndroidAnimationsBinding? = null

    private val binding get() = _binding!!

    private val mAdapter = MyItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAndroidAnimationsBinding.inflate(inflater, container, false)

        //Use below functions to test each function. Every demo uses views and hides views accordingly.

        //animateTextViewUsingCode()
        // animateUsingXML()
        // animationScreenTransition() //-- cool transition from this fragment to secondactivity.
        animationListXML() //-using images to create an animation using animation-list

        //animateRecyclerViewItems()


        return binding.root
    }

    private fun animateRecyclerViewItems() {
        binding.helloButton.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.animationRecyclerview.visibility = View.VISIBLE

        binding.animationRecyclerview.adapter = mAdapter
        binding.animationRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun animationListXML() {
        var animationRunning = false

        binding.helloButton.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.background_animation)

        val animationDrawable = binding.helloButton.background as AnimationDrawable

        binding.helloButton.setOnClickListener {
            if (animationRunning) {
                animationDrawable.stop()
                binding.helloButton.text = "Start"
                animationRunning = false
            } else {
                animationDrawable.start()
                binding.helloButton.text = "Stop"
                animationRunning = true
            }
        }

    }

    private fun animationScreenTransition() {
        binding.helloButton.setOnClickListener {
            val viewAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.animate_view)
            viewAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    findNavController().navigate(R.id.action_androidAnimations_to_secondActivity)
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
            binding.helloButton.startAnimation(viewAnimation)
        }
    }

    private fun animateUsingXML() {
        binding.helloButton.text = "Start"
        binding.helloButton.setOnClickListener {
            val animation1 = AnimationUtils.loadAnimation(requireContext(), R.anim.animate_one)
            val animation2 = AnimationUtils.loadAnimation(requireContext(), R.anim.animate_two)

            it.startAnimation(animation1)
            binding.imageView.startAnimation(animation2)
            //binding.root.startAnimation(animation1)
        }
    }

    private fun animateTextViewUsingCode() {
        binding.helloButton.setOnClickListener {
            it.startAnimation(rotateTranslateAnimation())
        }
    }

    private fun rotateTranslateAnimation(): Animation {
        val translate = TranslateAnimation(0f, 200f, 0f, 200f)
        val rotate = RotateAnimation(0f, 360f)
        //rotate.repeatMode = ValueAnimator.INFINITE
        // val scale = ScaleAnimation(0f,3f, 0f, 3f)

        val animationSet = AnimationSet(requireContext(), null)
        animationSet.addAnimation(translate)
        //animationSet.addAnimation(rotate)
        // animationSet.addAnimation(scale)
        animationSet.duration = 3000
        return animationSet
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/*
TranslateAnimation = allows you to create a smooth animation for translating ( moving ) a View object
    from one position to another on the screen. Part of Android view animation framework.
 */