package com.apprajapati.myanimations.ui.fragments

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ClipData
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnDragListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.apprajapati.myanimations.databinding.FragmentRotateSquareBinding
import kotlin.math.roundToInt

/*
    This class created to study how drag/drop mechanism work. Instead of using two containers, it uses only one container to demonstrate drag and drop in a single container. Usually 2 containers are used.
 */
class DragAndDropSingleContainer : Fragment() {

    private var _binding: FragmentRotateSquareBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRotateSquareBinding.inflate(inflater, container, false)
        //rotateSquare()

        val animator = rotateSquareUsingObjectAnimator()
        animator.start() //Just to have the ability to start and end in whole fragment/activity.
        screenWidthHeight()

        //Doesn't work when you try single onclicklistener. You can do it either on touchlistener or longclicklistener.
        binding.rotatingClockSquare.setOnLongClickListener {
            val dragShadowBuilder = View.DragShadowBuilder(binding.rotatingClockSquare)
            val clipData = ClipData.newPlainText("", "")
            binding.rotatingClockSquare.startDragAndDrop(
                clipData,
                dragShadowBuilder,
                binding.rotatingClockSquare,
                0
            )

//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                //support pre-Nougat versions
//                @Suppress("DEPRECATION")
//                view?.startDrag(clipData, dragShadowBuilder, view, 0)
//            } else {
//                //supports Nougat and beyond
//                view?.startDragAndDrop(clipData, dragShadowBuilder, view, 0)
//            }
             it.visibility = View.INVISIBLE
            true
        }

//        val viewWidth = binding.rotatingClockSquare.width
//        val viewHeight = binding.rotatingClockSquare.height
//
//        Log.d("View", "textView.width= $viewWidth")
//        Log.d("View", "textView.height= $viewHeight")

        binding.containerLayout.setOnDragListener(squareDragListener)


        return binding.root
    }

    //Class just to visualize where excatly X,Y coordinates points on the screen.
    class LineView(context: Context, x: Float, y: Float) : View(context) {

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            setColor(Color.RED)
            strokeWidth = 2f
            style = Paint.Style.FILL
        }

        val myx = x
        val myy = y
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            canvas.drawLine(0f, 0f, myx, myy, paint)
        }
    }

    private val squareDragListener = OnDragListener { v, event ->

        //event.x/y = gets X/Y coordinate of the drag point
        //getlocalstate() returns the local state object sent to the system as part of the call startDragandDrop()
        val draggedView = event.localState as TextView

        when (event.action) {

            DragEvent.ACTION_DRAG_STARTED -> {
                Log.d("View", "ACTION_DRAG_STARTED")

                Log.d("View", "draggableItem.x= ${pxToDp(draggedView.x.toInt())}")
                Log.d("View", "draggableItem.y= ${pxToDp(draggedView.y.toInt())}")
                Log.d("View", "event.x= ${pxToDp(event.x.toInt())}")
                Log.d("View", "event.y= ${pxToDp(event.y.toInt())}")

                true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.d("Ajay", "Action DRAG_ENTERED")
                true
            }

            DragEvent.ACTION_DRAG_EXITED -> {
                Log.d("Ajay", "Action DRAG_EXITED")
                v.invalidate() //This event will never be triggered because we are using single container in which the view resides.
                true
            }

            DragEvent.ACTION_DRAG_LOCATION -> {
                Log.d("Ajay", "ACTION_DRAG_LOCATION")
                true
            }

            DragEvent.ACTION_DROP -> {
                Log.d("View", "ACTION_DROP")


                //view.x = visual x position of this view, in pixels
                //view.y = visual y position of this view, in pixels.

                Log.d("View", "event.x= ${pxToDp(event.x.toInt())}")
                Log.d("View", "event.y= ${pxToDp(event.y.toInt())}")

                Log.d("View", "Before:draggableItem.x= ${pxToDp(draggedView.x.toInt())}")
                Log.d("View", "Before:draggableItem.y= ${pxToDp(draggedView.y.toInt())}")
                Log.d("View", "-----AFTER CHANGE------")
                draggedView.x =
                    event.x - (draggedView.width / 2) //Think 2 DIMENSIONAL VIEW to make visualization easier. Essentially resetting (0,0) coordinates to different position to exactly placing on the location where view dropped.
                draggedView.y = event.y - (draggedView.height / 2)
                //view.translateX/Y doesn't work. TODO: investigate why.

                /*
                //These width and height logs give you standard heights as set in XML, so no need to check.
                        Log.d("View", "draggableItem.width= ${pxToDp(draggedView.width)}")
                        Log.d("View", "draggableItem.height= ${pxToDp(draggedView.height)}")
                        Log.d("View", "view.width= ${pxToDp(v.width)}")
                        Log.d("View", "view.height= ${pxToDp(v.height)}")
                * */


                Log.d("View", "draggableItem.x= ${pxToDp(draggedView.x.toInt())}")
                Log.d("View", "draggableItem.y= ${pxToDp(draggedView.y.toInt())}")
                Log.d("View", "event.x= ${pxToDp(event.x.toInt())}")
                Log.d("View", "event.y= ${pxToDp(event.y.toInt())}")

                //drawLine(event.x, event.y)
                // lineView = lineView(requireContext(), event.x, event.y)

                //These two methods are to test how X,Y coordinates are manipulated in order to position the view to the center.
               // drawLinesToVisualize(event.x, event.y)
               // drawLinesToVisualize(draggedView.x, draggedView.y)

                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                Log.d("Ajay", "ACTION_DRAG_ENDED")
                draggedView.visibility = View.VISIBLE
                true
            }

            else -> {
                Log.d("Ajay", "Action ELSE")
                false
            }

        }
    }


    private fun screenWidthHeight() {
        val display = requireActivity().resources.displayMetrics
        Log.d("View", "ScreenHeight ${pxToDp(display.heightPixels)}")
        Log.d("View", "ScreenHeight ${pxToDp(display.widthPixels)}")
    }

    /*
        To visualize how X,Y coordinates are pointed to on the screen and reset.
        Just to test. If need to test, uncomment the code in ACTION_DROP.

     */
    private fun drawLinesToVisualize(cx: Float, cy: Float) {
        binding.containerLayout.addView(
            LineView(
                requireContext(),
                cx,
                cy
            )
        )
    }

    private fun rotateSquare() {

        val set = AnimationSet(requireContext(), null)
        val rotate = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.repeatCount = ValueAnimator.INFINITE
        set.addAnimation(rotate)
        set.duration = 1000
        binding.rotatingClockSquare.startAnimation(set)
    }

    private fun rotateSquareUsingObjectAnimator(): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(binding.rotatingClockSquare, "rotation", 0f, 360f)
        animator.duration = 1000
        animator.repeatCount = ObjectAnimator.INFINITE
        //animator.start()
        return animator
    }

    private class MyDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        private val shadow = ColorDrawable(Color.LTGRAY)

        override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
            super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint)

            val width: Int = view.width / 2
            val height: Int = view.height / 2

            shadow.setBounds(0, 0, width, height)
            outShadowSize.set(width, height)

            outShadowTouchPoint.set(width / 2, height / 2)

        }

        override fun onDrawShadow(canvas: Canvas) {
            shadow.draw(canvas)
        }

    }

    private fun pxToDp(px: Int): Int {
        return (px / requireContext().resources.displayMetrics.density).roundToInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}