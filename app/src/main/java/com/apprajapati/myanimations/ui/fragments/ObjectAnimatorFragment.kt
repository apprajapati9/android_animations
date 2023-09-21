package com.apprajapati.myanimations.ui.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apprajapati.myanimations.databinding.FragmentObjectAnimatorBinding
import com.apprajapati.myanimations.ui.BaseFragment

class ObjectAnimatorFragment :
    BaseFragment<FragmentObjectAnimatorBinding>(FragmentObjectAnimatorBinding::inflate) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        //animateButton()

        //animateMultipleAnimators()    //to animate several animators at the same time.

        //chainedAnimators()




        return binding.root
    }

    private fun chainedAnimators() {

    }

    /*
        playing several animators at the same time.
     */
    private fun animateMultipleAnimators() {
        val translateAnim = ObjectAnimator.ofFloat(binding.buttonStart, "translationY", 100f)
        translateAnim.repeatMode = ValueAnimator.REVERSE
        translateAnim.repeatCount = ValueAnimator.INFINITE

        val xScaleAnim = ObjectAnimator.ofFloat(binding.buttonStart, "scaleX", 2f)
        xScaleAnim.repeatMode = ValueAnimator.REVERSE
        xScaleAnim.repeatCount = ValueAnimator.INFINITE

        val yScaleAnim = ObjectAnimator.ofFloat(binding.buttonStart, "scaleY", 2f)
        yScaleAnim.repeatMode = ValueAnimator.REVERSE
        yScaleAnim.repeatCount = ValueAnimator.INFINITE

        val colorAnim = ObjectAnimator.ofObject(
            binding.buttonStart,
            "textColor",
            ArgbEvaluator(),
            Color.RED,
            Color.GREEN
        )
        colorAnim.repeatMode = ValueAnimator.REVERSE
        colorAnim.repeatCount = ValueAnimator.INFINITE

        val set = AnimatorSet()
        set.playTogether(
            translateAnim,
            xScaleAnim,
            yScaleAnim,
            colorAnim
        ) //play sequentially option is available too.
        set.duration = 1000
        //set.start()

        binding.buttonStart.setOnClickListener {
            if (set.isRunning) {
                set.end()
                //it.animate().translationX(0f).translationY(0f).scaleX(0f).scaleY(0f).setDuration(1000) //to reset it back to its original state but its not working.
            } else {
                set.start()
            }
        }

    }

    //Zoom in and out animation on a view.
    private fun animateButton() {
        binding.buttonStart.setOnClickListener { button ->
            val mScaleX = ObjectAnimator.ofFloat(button, "scaleX", 5f)
            val mScaleY = ObjectAnimator.ofFloat(button, "scaleY", 5f)
            mScaleY.duration = 1000
            mScaleX.duration = 1000
            mScaleX.repeatCount = ValueAnimator.INFINITE
            mScaleY.repeatCount = ValueAnimator.INFINITE
            mScaleX.repeatMode = ValueAnimator.REVERSE
            mScaleY.repeatMode = ValueAnimator.REVERSE
            mScaleX.start()
            mScaleY.start()
        }
    }
}