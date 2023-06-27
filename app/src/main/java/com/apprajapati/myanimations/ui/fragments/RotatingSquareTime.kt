package com.apprajapati.myanimations.ui.fragments

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.health.connect.datatypes.HeightRecord
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.apprajapati.myanimations.R
import com.apprajapati.myanimations.databinding.FragmentRotatingSquareTimeBinding
import com.apprajapati.myanimations.util.NetworkResult
import com.apprajapati.myanimations.util.getTimeOnly
import com.apprajapati.myanimations.util.getTimeOnlyTimeApi
import com.apprajapati.myanimations.util.rotateViewUsingObjectAnimator
import com.apprajapati.myanimations.viewmodels.MainViewModel
import com.google.android.material.dialog.InsetDialogOnTouchListener
import java.util.Timer
import java.util.TimerTask

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


        viewModel.time.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val time = response.data
                    binding.textViewTime.text = getTimeOnly(time?.datetime!!)
                }

                is NetworkResult.Error -> {
                    val message = response.message
                    binding.textViewTime.text = message
                    viewModel.stopTimer()
                }

                is NetworkResult.Loading -> binding.textViewTime.text = "Loading"
            }
        }

        viewModel.timeApi.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val time = response.data
                    binding.textViewTime.text = getTimeOnlyTimeApi(time?.dateTime!!)
                }

                is NetworkResult.Error -> {
                    val message = response.message
                    binding.textViewTime.text = message
                    viewModel.stopTimer()
                }

                is NetworkResult.Loading -> binding.textViewTime.text = "Loading"
            }
        }

        val animator = rotateViewUsingObjectAnimator(binding.textViewTime)
        animator.start()
        // animator.end()

//        binding.requestButton.setOnClickListener {
//            viewModel.getTime()
//        }
        binding.requestButton.visibility = View.GONE

        binding.textViewTime.setOnLongClickListener {
            //val item = ClipData.Item("","")
            val dragData = ClipData.newPlainText("", "")
            val shadow = MyDragShadowBuilder(binding.textViewTime)

            binding.textViewTime.startDragAndDrop(dragData, shadow, it, 0)
            true
        }


        binding.mainContainer.setOnDragListener(dragListener)
        return binding.root
    }


    val dragListener = View.OnDragListener { view, event ->

        val draggedView = event.localState as View

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                binding.textViewTime.visibility = View.GONE
                true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                binding.textViewTime.visibility = View.VISIBLE
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                binding.textViewTime.visibility = View.VISIBLE
                true
            }

            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }

            DragEvent.ACTION_DROP -> {

                draggedView.x = event.x - draggedView.width / 2
                draggedView.y = event.y - draggedView.height / 2


                true
            }

            else -> false
        }

    }


    private class MyDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {

        private val shadow = ContextCompat.getDrawable(view.context, R.drawable.rotating_square_box)
        private val mText = TextView(view.context)

        init {
            //mText.text = "Ajay"
            //mText.background = shadow
        }

        //to get the size and position of the drag shadow. It uses this data to construct a canvas object. then it calls onDrawShadow() so that your application can drw the shadow image in the canvas.
        override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
            super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint)
            //view.heigh/width gives the full size of original view.
            val width = view.width
            val height = view.height

            shadow?.setBounds(0, 0, width, height)
            //mText.background = shadow
            outShadowSize?.set(width, height)
            //mText.text = "Ajay"

            outShadowTouchPoint?.set(width / 2, height / 2)
        }

        override fun onDrawShadow(canvas: Canvas) {
            //shadow?.setBounds(0, 0, view.width, view.height)
            //mText.background = shadow

            //mText.layout(0,0, view.width, view.height)


            shadow?.draw(canvas)
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTimer()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startFetchingTime()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopTimer()
        _binding = null
    }
}