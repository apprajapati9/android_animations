package com.apprajapati.snowfall

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import java.util.*

/**
 * @Author: Ajay P. Prajapati (https://github.com/apprajapati9)
 * @Date: 12,January,2022
 */
class SnowFlakeView(context: Context, attrs: AttributeSet) :
    View(context, attrs){

    private val snowflakesNum : Int
    private val snowflakeImage: Bitmap?
    private val snowflakeAlphaMin: Int
    private val snowflakeAlphaMax: Int
    private val snowflakeAngleMax: Int
    private val snowflakeSizeMinInPx : Int  //TODO: Get dimensions
    private val snowflakeSizeMaxInPx : Int
    private val snowflakeSpeedMin: Int
    private val snowflakeSpeedMax: Int
    private val snowflakesFadingEnabled: Boolean
    private val snowflakesAlreadyFalling: Boolean

    private lateinit var updateSnowflakesThread : UpdateSnowflakesThread
    private var snowflakes: Array<SnowFlake>? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SnowfallView)
        try{
            snowflakesNum = a.getInt(R.styleable.SnowfallView_snowflakesNum, DEFAULT_SNOWFLAKES_NUM)
            snowflakeImage = a.getDrawable(R.styleable.SnowfallView_snowflakeImage)?.toBitmap()

            snowflakeAlphaMin = a.getInt(R.styleable.SnowfallView_snowflakeAlphaMin, DEFAULT_SNOWFLAKE_ALPHA_MIN)
            snowflakeAlphaMax = a.getInt(R.styleable.SnowfallView_snowflakeAlphaMax, DEFAULT_SNOWFLAKE_ALPHA_MAX)

            snowflakeAngleMax = a.getInt(R.styleable.SnowfallView_snowflakeAngleMax, DEFAULT_SNOWFLAKE_ANGLE_MAX)

            snowflakeSizeMinInPx = a.getInt(R.styleable.SnowfallView_snowflakeSizeMin, dpToPx( DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP))
            snowflakeSizeMaxInPx = a.getInt(R.styleable.SnowfallView_snowflakeSizeMax,dpToPx ( DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP))

            snowflakeSpeedMin = a.getInt(R.styleable.SnowfallView_snowflakeSpeedMin, DEFAULT_SNOWFLAKE_SPEED_MIN)
            snowflakeSpeedMax = a.getInt(R.styleable.SnowfallView_snowflakeSpeedMax, DEFAULT_SNOWFLAKE_SPEED_MAX)


            snowflakesFadingEnabled = a.getBoolean(R.styleable.SnowfallView_snowflakesFadingEnabled, DEFAULT_SNOWFLAKES_FADING_ENABLED)
            snowflakesAlreadyFalling = a.getBoolean(R.styleable.SnowfallView_snowflakesAlreadyFalling, DEFAULT_SNOWFLAKES_ALREADY_FALLING)
        } finally {
            a.recycle()
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateSnowflakesThread = UpdateSnowflakesThread()
    }

    override fun onDetachedFromWindow() {
        updateSnowflakesThread.quit()
        super.onDetachedFromWindow()
    }

    /*
        whatever DP is, multiplying with displaymatrics density will give you Pixel value.
     */
    private fun dpToPx(dp : Int) : Int{
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        snowflakes = createSnowflakes(); //Whenever size changes, create new default snowflakes with params
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if(changedView == this && visibility == GONE){
            snowflakes?.forEach { it.reset() }
        }
    }

    // TODO : difference between Class? and Class and how to pass it
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(isInEditMode){
            return
        }

        var haveAtLeastOneVisibleSnowflake = false

        val localSnowflakes = snowflakes
        if(localSnowflakes != null){
            for(snowflake in localSnowflakes){
                if(snowflake.isStillFalling()){
                    haveAtLeastOneVisibleSnowflake = true
                    snowflake.draw(canvas)
                }
            }
        }

        if(haveAtLeastOneVisibleSnowflake){
            updateSnowflakes()
        }else{
            visibility = GONE
        }

        val fallingSnowflakes = snowflakes?.filter { it.isStillFalling()}
        if(fallingSnowflakes?.isNotEmpty() == true){
            fallingSnowflakes.forEach { it.draw(canvas) }
            updateSnowflakes()
        }else{
            visibility = GONE
        }
    }

    fun stopFalling(){
        snowflakes?.forEach { it.shouldRecycleFalling = false }
    }

    fun restartFalling(){
        snowflakes?.forEach { it.shouldRecycleFalling = true }
    }

    private fun updateSnowflakes(){
        updateSnowflakesThread.handler.post {
            var haveAtLeastOneVisibleSnowflake = false

            val localSnowflakes = snowflakes ?: return@post //what is this? TODO
            for(snowflake in localSnowflakes){
                if(snowflake.isStillFalling()){
                    haveAtLeastOneVisibleSnowflake = true
                    snowflake.update()
                }
            }

            if(haveAtLeastOneVisibleSnowflake){
                postInvalidateOnAnimation() // what is this ? TODO
            }
        }
    }

    private fun createSnowflakes() : Array<SnowFlake> {
        val randomizer =  Randomizer()

        val snowflakeParams = SnowFlake.Params(
            parentWidth = width,
            parentHeight = height,
            image =  snowflakeImage,
            alphaMax =  snowflakeAlphaMax,
            alphaMin =  snowflakeAlphaMin,
            sizeMaxInPx = snowflakeSizeMaxInPx,
            sizeMinInPx = snowflakeSizeMinInPx,
            speedMin =  snowflakeSpeedMin,
            speedMax = snowflakeSpeedMax,
            fadingEnabled = snowflakesFadingEnabled,
            alreadyFalling = snowflakesAlreadyFalling,
            angleMax = snowflakeAngleMax)

        return Array(snowflakesNum) { SnowFlake(randomizer, snowflakeParams) }
    }


    companion object {
        private const val DEFAULT_SNOWFLAKES_NUM = 200
        private const val DEFAULT_SNOWFLAKE_ALPHA_MIN = 150
        private const val DEFAULT_SNOWFLAKE_ALPHA_MAX = 250
        private const val DEFAULT_SNOWFLAKE_ANGLE_MAX = 10
        private const val DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP = 2
        private const val DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP = 8
        private const val DEFAULT_SNOWFLAKE_SPEED_MIN = 2
        private const val DEFAULT_SNOWFLAKE_SPEED_MAX = 8
        private const val DEFAULT_SNOWFLAKES_FADING_ENABLED = false
        private const val DEFAULT_SNOWFLAKES_ALREADY_FALLING = false
    }


    private class UpdateSnowflakesThread: HandlerThread("SnowflakesComputations"){
        val handler : Handler
        init {
            start()
            handler = Handler(looper)
        }
    }




}