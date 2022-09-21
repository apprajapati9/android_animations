package com.apprajapati.snowfall

import java.util.*
import kotlin.math.abs


/**
 * @Author: Ajay P. Prajapati (https://github.com/apprajapati9)
 * @Date: 11,January,2022
 */

//internal means that any client inside this module who sees
// the declaring class sees its internal members.
internal class Randomizer {

    //No need in random instance to be lazy
    private val random = Random(System.currentTimeMillis())

    fun randomDouble(max: Int) : Double {
        return random.nextDouble() * ( max + 1 )
    }

    fun randomInt(min: Int, max: Int, gaussian: Boolean = false): Int {
        return randomInt(max - min, gaussian) + min
    }

    fun randomInt(max: Int, guassian: Boolean = false) : Int {
        return if(guassian) {
            (abs(randomGaussian()) * (max + 1)).toInt()
        }else {
            random.nextInt(max + 1)
        }
    }

    fun randomGaussian(): Double {
        val gaussian = random.nextGaussian() / 3
        return if (gaussian > -1 && gaussian < 1) gaussian else randomGaussian()
    }

    fun randomSignum() : Int {
        return if (random.nextBoolean()) 1 else -1
    }

}