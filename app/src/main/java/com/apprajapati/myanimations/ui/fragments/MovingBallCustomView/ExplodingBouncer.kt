import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import com.apprajapati.myanimations.ui.fragments.MovingBallCustomView.RotatingBouncer
internal class ExplodingBouncer(givenPosition: PointF, givenColor: Int, givenBouncingArea: RectF) :
    RotatingBouncer(givenPosition, givenColor, givenBouncingArea) {

    companion object {
        const val BALL_ALIVE_AND_WELL = 0
        const val BALL_EXPLODING = 1
        const val BALL_EXPLODED = 2
    }

    var ballState = BALL_ALIVE_AND_WELL

    var explosionColorAlphaValue = 0

    fun explodeBall(){
        ballState = BALL_EXPLODING
        larger()
    }

    fun isExploded() : Boolean{
        return ballState == BALL_EXPLODED
    }

    override fun move() {
        if(ballState == BALL_ALIVE_AND_WELL){
            super.move()
        }
    }

    override fun draw(canvas: Canvas) {
        if(ballState == BALL_ALIVE_AND_WELL){
            super.draw(canvas)
        }else if(ballState == BALL_EXPLODING){
            if(explosionColorAlphaValue > 0xFF){
                ballState = BALL_EXPLODED
            }
            else{

                super.draw(canvas)

                val explosionPaint = Paint().apply {
                    color = Color.YELLOW
                    alpha = explosionColorAlphaValue
                    style = Paint.Style.FILL
                }

                canvas.drawCircle(objectCenterPoint.x, objectCenterPoint.y, bouncerRadius, explosionPaint)

                explosionColorAlphaValue += 4  //decrease transparency
            }
        }
    }
}