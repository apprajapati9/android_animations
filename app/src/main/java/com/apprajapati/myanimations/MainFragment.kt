package com.apprajapati.myanimations

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.apprajapati.myanimations.databinding.FragmentFirstBinding
import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

const val DIE_INDEX_KEY = "die_index"
const val DIE_VALUE_KEY = "die_value"

class MainFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private lateinit var imageViews : Array<ImageView>
    private val drawables = arrayOf(R.drawable.die_1,
        R.drawable.die_2,
        R.drawable.die_3,
        R.drawable.die_4,
        R.drawable.die_5,
        R.drawable.die_6)

    private val dieHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            val data = msg.data

            val dieIndex =  data?.getInt(DIE_INDEX_KEY) ?: 0
            val dieValue = data?.getInt(DIE_VALUE_KEY) ?: 1

            imageViews.get(dieIndex).
                setImageResource(drawables.get(dieValue -1))
        }
    }

    //Uncomment below code if you want to test Handler()
//    private val handler = object : Handler(Looper.getMainLooper()) {
//        override fun handleMessage(msg: Message) {
//            val bundle = msg.data
//
//            displayData(bundle.getString("Ajay")!!)
//        }
//    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

        /*
        binding.buttonStart.setOnClickListener {
            thread (start = true) {
                val bundle = Bundle()

                for (i in 1..10) {
                    bundle.putString("Ajay", "Thread counter $i \n")

                    Message().also {
                        it.data = bundle
                        handler.sendMessage(it)
                    }
                    Thread.sleep(1000) //every one second
                }
                bundle.putString("Ajay", "All counters done!")
                val msg = Message()
                msg.data = bundle
                handler.sendMessage(msg)
                // You can use also like before to add message data but this is just to illustrate how it can be done without also{} block.
            }

        }
        */

        imageViews = arrayOf(binding.die1,
            binding.die2,
            binding.die3,
            binding.die4,
            binding.die5)

        binding.rollButton.setOnClickListener {
            rollTheDice()
        }

    }

    private fun rollTheDice() {

        for (dieIndex in imageViews.indices){

            thread (start = true) {
                Thread.sleep(dieIndex * 100L) //delaying starts so all dies look differently animating.

                val bundle = Bundle()
                bundle.putInt(DIE_INDEX_KEY, dieIndex)

                for(i in 1..20) {
                    val dieNumber = getDieValue()
                    bundle.putInt(DIE_VALUE_KEY, dieNumber)
                    Thread.sleep(100)
                    Message().also {
                        it.data = bundle
                        dieHandler.sendMessage(it)
                    }
                }

            }


        }

    }

//    private fun displayData(data : String) {
//        binding.textviewFirst.append(data)
//    }

    private fun getDieValue() : Int {
        return Random.nextInt(1, 7) //until isn't inclusive.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}