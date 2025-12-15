package com.example.lolokidsapp.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.lolokidsapp.R

/**
 * Custom view for drawing/tracing letters
 */
class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var path = Path()
    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.drawing_line)
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
    }

    // Paint for the letter placeholder/watermark
    private val letterPaint = Paint().apply {
        color = Color.parseColor("#E0E0E0") // Light gray
        textAlign = Paint.Align.CENTER
        textSize = 400f // Will be adjusted based on view size
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
        alpha = 100 // Semi-transparent (0-255, where 255 is opaque)
    }

    private val paths = mutableListOf<Pair<Path, Paint>>()

    // Letter to display as placeholder
    private var placeholderLetter: String? = null

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.drawing_background))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the letter placeholder/watermark first (behind the drawing)
        if (placeholderLetter != null) {
            drawLetterPlaceholder(canvas)
        }

        // Draw all previous paths
        for ((savedPath, savedPaint) in paths) {
            canvas.drawPath(savedPath, savedPaint)
        }

        // Draw current path
        canvas.drawPath(path, paint)
    }

    /**
     * Draw the letter as a placeholder/guide for tracing
     */
    private fun drawLetterPlaceholder(canvas: Canvas) {
        placeholderLetter?.let { letter ->
            // Adjust text size based on view dimensions
            val viewWidth = width.toFloat()
            val viewHeight = height.toFloat()
            val minDimension = minOf(viewWidth, viewHeight)

            // Set text size to 60% of the smaller dimension
            letterPaint.textSize = minDimension * 0.6f

            // Calculate position to center the letter
            val x = viewWidth / 2f
            val y = (viewHeight / 2f) - ((letterPaint.descent() + letterPaint.ascent()) / 2f)

            // Draw the letter
            canvas.drawText(letter, x, y, letterPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }

            MotionEvent.ACTION_UP -> {
                // Save the current path
                val newPaint = Paint(paint)
                paths.add(Pair(Path(path), newPaint))
                path.reset()
            }

            else -> return false
        }

        invalidate()
        return true
    }

    /**
     * Clear all drawings
     */
    fun clear() {
        path.reset()
        paths.clear()
        invalidate()
    }

    /**
     * Set drawing color
     */
    fun setDrawingColor(color: Int) {
        paint.color = color
    }

    /**
     * Set stroke width
     */
    fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
    }

    /**
     * Set the letter to display as a placeholder/guide
     */
    fun setPlaceholderLetter(letter: String?) {
        placeholderLetter = letter
        invalidate()
    }

    /**
     * Set placeholder transparency (0-255, where 0 is transparent and 255 is opaque)
     */
    fun setPlaceholderAlpha(alpha: Int) {
        letterPaint.alpha = alpha.coerceIn(0, 255)
        invalidate()
    }

    /**
     * Set placeholder color
     */
    fun setPlaceholderColor(color: Int) {
        letterPaint.color = color
        invalidate()
    }
}
