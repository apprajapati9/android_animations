package com.apprajapati.myanimations.ui.fragments.MovingBallCustomView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apprajapati.myanimations.ui.fragments.MovingBallCustomView.MovingBallView

/*
learning this from https://www.youtube.com/watch?v=ecgvwg-rELc
and  https://rdcworld-android.blogspot.com/2011/
exercise pdf link - http://naturalprogramming.com/for_students/android/android_exercises.pdf
follow these vids, tutorial.
 */
class BouncingBallViewFragment : Fragment() {

    private lateinit var bouncingBallView: MovingBallView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bouncingBallView = MovingBallView(requireActivity())

        return bouncingBallView
    }

    override fun onResume() {
        super.onResume()

        bouncingBallView.startAnimationThread()
    }


    override fun onStop() {
        super.onStop()
        bouncingBallView.stopAnimationThread()
    }

}