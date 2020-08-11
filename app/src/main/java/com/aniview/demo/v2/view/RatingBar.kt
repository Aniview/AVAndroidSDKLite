package com.aniview.demo.v2.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.aniview.demo.R
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

/**
 * @author Maksym Popovych
 */
class RatingBar : View {

	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	private val emptyStar = resources.getDrawable(R.drawable.ic_empty_star, null)
	private val filledStar = resources.getDrawable(R.drawable.ic_filled_star, null)

	private val starWidth = max(filledStar.intrinsicWidth, emptyStar.intrinsicWidth)
	private val starHeight = max(filledStar.intrinsicHeight, emptyStar.intrinsicHeight)

	init {
		emptyStar.setBounds(0, 0, starWidth, starHeight)
		filledStar.setBounds(0, 0, starWidth, starHeight)
	}

	var rating: Float = 0.0f
		set(value) {
			field = value
			invalidate()
		}

	var starCount: Int = 5
		set(value) {
			field = value
			invalidate()
		}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		setMeasuredDimension(starWidth * starCount, starHeight)
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

		val filledStarsWidth = starWidth * rating
		val flooredStarsWidth = starWidth * floor(rating)

		canvas.save()
		canvas.clipRect(0f, 0f, filledStarsWidth, starHeight.toFloat())
		drawStars(ceil(rating).toInt(), filledStar, canvas)
		canvas.restore()

		canvas.save()
		canvas.clipRect(filledStarsWidth, 0f, width.toFloat(), starHeight.toFloat())
		canvas.translate(flooredStarsWidth, 0f)
		drawStars(ceil(starCount - rating).toInt(), emptyStar, canvas)
		canvas.restore()
	}

	private fun drawStars(starsCount : Int, starDrawable: Drawable, canvas: Canvas) {
		for (x in 0 until starsCount) {
			starDrawable.draw(canvas)
			canvas.translate(starWidth.toFloat(), 0f)
		}
	}

}