package com.tntra.pargo.custom


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.tntra.pargo.R

/**
 * Created by jkadvantage Team on 5/08/19.
 */


/**
 * Custom ImageView for circular images in Android while maintaining the
 * best draw performance and supporting custom borders & selectors.
 */
@SuppressLint("AppCompatCustomView")
class
CircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.styleable.CircularImageViewStyle_circularImageViewDefault
) : ImageView(context, attrs, defStyle) {
    // Border & Selector configuration variables
    private var hasBorder: Boolean = false
    private var hasSelector: Boolean = false
    private var shadowEnabled: Boolean = false
    private var borderWidth: Int = 0
    private var canvasSize: Int = 0
    private var selectorStrokeWidth: Int = 0
    // Objects used for the actual drawing
    private var shader: BitmapShader? = null
    private var image: Bitmap? = null
    private var paint: Paint? = null
    private var paintBorder: Paint? = null
    private var paintSelectorBorder: Paint? = null
    private var selectorFilter: ColorFilter? = null

    init {
        init(context, attrs, defStyle)
    }

    /**
     * Initializes paint objects and sets desired attributes.
     *
     * @param context  Context
     * @param attrs    Attributes
     * @param defStyle Default Style
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {

        // Initialize paint objects
        paint = Paint()
        paint!!.isAntiAlias = true
        paintBorder = Paint()
        paintBorder!!.isAntiAlias = true
        paintSelectorBorder = Paint()
        paintSelectorBorder!!.isAntiAlias = true
        // Attempt applying shadow layers
        applyShadow()
        // load the styled attributes and set their properties
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyle, 0)
        // Check if border and/or border is enabled
        hasBorder = attributes.getBoolean(R.styleable.CircularImageView_border, false)
        hasSelector = attributes.getBoolean(R.styleable.CircularImageView_selector, false)
        // Set border properties if enabled
        if (hasBorder) {
            val defaultBorderSize = (2 * context.resources.displayMetrics.density + 0.5f).toInt()
            setBorderWidth(
                attributes.getDimensionPixelOffset(
                    R.styleable.CircularImageView_border_width,
                    defaultBorderSize
                )
            )
            setBorderColor(attributes.getColor(R.styleable.CircularImageView_border_color, Color.WHITE))
        }
        // Set selector properties if enabled
        if (hasSelector) {
            val defaultSelectorSize = (2 * context.resources.displayMetrics.density + 0.5f).toInt()
            setSelectorColor(attributes.getColor(R.styleable.CircularImageView_selector_color, Color.TRANSPARENT))
            setSelectorStrokeWidth(
                attributes.getDimensionPixelOffset(
                    R.styleable.CircularImageView_selector_stroke_width,
                    defaultSelectorSize
                )
            )
            setSelectorStrokeColor(attributes.getColor(R.styleable.CircularImageView_selector_stroke_color, Color.BLUE))
        }
        // Add shadow if enabled
        if (attributes.getBoolean(R.styleable.CircularImageView_shadow, false))
            setShadow(true)
        // We no longer need our attributes TypedArray, give it back to cache
        attributes.recycle()

    }


    /**
     * Sets the CircularImageView's border width in pixels.
     *
     * @param borderWidth Width in pixels for the border.
     */
    fun setBorderWidth(borderWidth: Int) {
        this.borderWidth = borderWidth
        this.requestLayout()
        this.invalidate()
    }


    /**
     * Sets the CircularImageView's basic border color.
     *
     * @param borderColor The new color (including alpha) to set the border.
     */

    fun setBorderColor(borderColor: Int) {
        if (paintBorder != null)
            paintBorder!!.color = borderColor
        this.invalidate()
    }

    /**
     * Sets the color of the selector to be draw over the
     * CircularImageView. Be sure to provide some opacity.
     *
     * @param selectorColor The color (including alpha) to set for the selector overlay.
     */

    fun setSelectorColor(selectorColor: Int) {
        this.selectorFilter = PorterDuffColorFilter(selectorColor, PorterDuff.Mode.SRC_ATOP)
        this.invalidate()
    }


    /**
     * Sets the stroke width to be drawn around the CircularImageView
     * during click events when the selector is enabled.
     *
     * @param selectorStrokeWidth Width in pixels for the selector stroke.
     */


    fun setSelectorStrokeWidth(selectorStrokeWidth: Int) {
        this.selectorStrokeWidth = selectorStrokeWidth
        this.requestLayout()
        this.invalidate()
    }


    /**
     * Sets the stroke color to be drawn around the CircularImageView
     * during click events when the selector is enabled.
     *
     * @param selectorStrokeColor The color (including alpha) to set for the selector stroke.
     */


    fun setSelectorStrokeColor(selectorStrokeColor: Int) {
        if (paintSelectorBorder != null)
            paintSelectorBorder!!.color = selectorStrokeColor
        this.invalidate()
    }

    /**
     * Enables a dark shadow for this CircularImageView.
     *
     * @param shadowEnabled Set to true to render a shadow or false to disable it.
     */

    fun setShadow(shadowEnabled: Boolean) {

        this.shadowEnabled = true
        if (shadowEnabled) {
            //paint.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
            paintBorder!!.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK)
            paintSelectorBorder!!.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK)
        } else {
            //paint.setShadowLayer(0, 0.0f, 2.0f, Color.BLACK);
            paintBorder!!.setShadowLayer(0f, 0.0f, 2.0f, Color.BLACK)
            paintSelectorBorder!!.setShadowLayer(0f, 0.0f, 2.0f, Color.BLACK)
        }
    }


    /**
     * Enables a dark shadow for this CircularImageView.
     * If the radius is set to 0, the shadow is removed.
     *
     * @param radius
     * @param dx
     * @param dy
     * @param color
     */

    fun setShadow(radius: Float, dx: Float, dy: Float, color: Int) {
        // TODO
    }

    public override fun onDraw(canvas: Canvas) {
        // Don't draw anything without an image
        if (image == null)
            return
        // Nothing to draw (Empty bounds)
        if (image!!.height == 0 || image!!.width == 0)
            return
        // We'll need this later
        val oldCanvasSize = canvasSize
        // Compare canvas sizes
        canvasSize = canvas.width
        if (canvas.height < canvasSize)
            canvasSize = canvas.height
        // Reinitialize shader, if necessary
        if (oldCanvasSize != canvasSize)
            refreshBitmapShader()
        // Apply shader to paint
        paint!!.shader = shader
        // Keep track of selectorStroke/border width
        var outerWidth = 0
        // Get the exact X/Y axis of the view
        var center = canvasSize / 2
        if (hasSelector && isSelected) { // Draw the selector stroke & apply the selector filter, if applicable
            outerWidth = selectorStrokeWidth
            center = (canvasSize - outerWidth * 2) / 2
            paint!!.colorFilter = selectorFilter
            canvas.drawCircle(
                (center + outerWidth).toFloat(),
                (center + outerWidth).toFloat(),
                (canvasSize - outerWidth * 2) / 2 + outerWidth - 4.0f,
                paintSelectorBorder!!
            )
        } else if (hasBorder) { // If no selector was drawn, draw a border and clear the filter instead... if enabled
            outerWidth = borderWidth
            center = (canvasSize - outerWidth * 2) / 2
            paint!!.colorFilter = null
            canvas.drawCircle(
                (center + outerWidth).toFloat(),
                (center + outerWidth).toFloat(),
                (canvasSize - outerWidth * 2) / 2 + outerWidth - 4.0f,
                paintBorder!!
            )
        } else
        // Clear the color filter if no selector nor border were drawn
            paint!!.colorFilter = null
        // Draw the circular image itself
        canvas.drawCircle(
            (center + outerWidth).toFloat(),
            (center + outerWidth).toFloat(),
            (canvasSize - outerWidth * 2) / 2 - 4.0f,
            paint!!
        )
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        // Check for clickable state and do nothing if disabled
        if (!this.isClickable) {
            this.isSelected = false
            return super.onTouchEvent(event)
        }
        // Set selected state based on Motion Event
        when (event.action) {
            MotionEvent.ACTION_DOWN -> this.isSelected = true
            MotionEvent.ACTION_UP, MotionEvent.ACTION_SCROLL, MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_CANCEL -> this.isSelected =
                false
        }
        // Redraw image and return super type
        this.invalidate()
        return super.dispatchTouchEvent(event)

    }


    override fun invalidate(dirty: Rect) {

        super.invalidate(dirty)
        // Don't do anything without a valid drawable
        if (drawable == null)
            return
        // Extract a Bitmap out of the drawable & set it as the main shader
        image = drawableToBitmap(drawable)
        if (shader != null || canvasSize > 0)
            refreshBitmapShader()

    }


    override fun invalidate(l: Int, t: Int, r: Int, b: Int) {

        super.invalidate(l, t, r, b)
        // Don't do anything without a valid drawable
        if (drawable == null)
            return
        // Extract a Bitmap out of the drawable & set it as the main shader
        image = drawableToBitmap(drawable)
        if (shader != null || canvasSize > 0)
            refreshBitmapShader()

    }


    override fun invalidate() {

        super.invalidate()
        // Don't do anything without a valid drawable
        if (drawable == null)
            return
        // Extract a Bitmap out of the drawable & set it as the main shader
        image = drawableToBitmap(drawable)
        if (shader != null || canvasSize > 0)
            refreshBitmapShader()

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)

    }


    private fun measureWidth(measureSpec: Int): Int {

        val result: Int
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            // The parent has determined an exact size for the child.
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize
        } else {
            // The parent has not imposed any constraint on the child.
            result = canvasSize
        }
        return result

    }


    private fun measureHeight(measureSpecHeight: Int): Int {

        val result: Int
        val specMode = View.MeasureSpec.getMode(measureSpecHeight)
        val specSize = View.MeasureSpec.getSize(measureSpecHeight)
        if (specMode == View.MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = canvasSize
        }
        return result + 2

    }

    /**
     * Disable this view's hardware acceleration on Honeycomb
     * and up, as long as edit mode is disabled. (Required for shadow effect)
     */


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun applyShadow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //setLayerType(LAYER_TYPE_SOFTWARE, paint);
            setLayerType(View.LAYER_TYPE_SOFTWARE, paintBorder)
            //setLayerType(LAYER_TYPE_SOFTWARE, paintSelectorBorder);
        }
    }


    /**
     * Convert a drawable object into a Bitmap.
     *
     * @param drawable Drawable to extract a Bitmap from.
     * @return A Bitmap created from the drawable parameter.
     */


    fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null)
        // Don't do anything without a proper drawable
            return null
        else if (drawable is BitmapDrawable)
        // Use the getBitmap() method instead if BitmapDrawable
            return drawable.bitmap

        // Create Bitmap object out of the drawable
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        // Return the created Bitmap
        return bitmap
    }


    /**
     * Reinitializes the shader texture used to fill in
     * the Circle upon drawing.
     */

    fun refreshBitmapShader() {

        val left = 0
        var y = 0
        var w = image!!.width
        var h = image!!.height
        var x = 0
        // decide whether we have to crop the sizes or the top and bottom:
        if (w > h)
        // width is greater than height
        {
            x = w - h shr 1   // crop sides, half on each side
            w = h
        } else {
            y = h - w shr 1   // crop top and bottom
            h = w
        }
        val m = Matrix()
        val scale = canvasSize.toFloat() / w.toFloat()
        m.preScale(scale, scale)   // scale to canvas size

        //shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvasSize, canvasSize, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        shader = BitmapShader(
            Bitmap.createBitmap(image!!, x, y, w, h, m, false),
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
    }

    /**
     * @return Whether or not this view is currently
     * in its selected state.
     */


    override fun isSelected(): Boolean {
        return this.isSelected
    }
}