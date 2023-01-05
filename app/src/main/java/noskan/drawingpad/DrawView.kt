package noskan.drawingpad

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null
    private val mPaint: Paint = Paint()

    //ArrayList to store all the strokes on the Canvas
    private val paths = ArrayList<Stroke>()
    private var currentColor = 0
    private var strokeWidth = 0
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)

    //instantiate the bitmap and object
    fun init(height: Int, width: Int) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap!!)

        //set an initial color of the brush
        currentColor = Color.BLACK
        //set an initial brush size
        strokeWidth = 10
    }

    //sets the current color of stroke
    fun setColor(color: Int) {
        currentColor = color
    }

    //sets the stroke width
    fun setStrokeWidth(width: Int) {
        strokeWidth = width
    }

    fun delete() {
        //check whether the List is empty or not
        //if empty, the remove method will return an error
        if (paths.size != 0) {
            paths.removeAt(paths.size - 1)
            invalidate()
        }
    }

    //returns the current bitmap
    fun save(): Bitmap? {
        return mBitmap
    }

    //drawing method
    override fun onDraw(canvas: Canvas) {
        //save the current state of the canvas before,
        //to draw the background of the canvas
        canvas.save()
        //DEFAULT color of the canvas
        val backgroundColor = Color.WHITE
        mCanvas?.drawColor(backgroundColor)
        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mCanvas?.drawPath(fp.path, mPaint)
        }
        mBitmap?.let { canvas.drawBitmap(it, 0f, 0f, mBitmapPaint) }
        canvas.restore()
    }

    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val fp = Stroke(currentColor, strokeWidth, mPath!!)
        paths.add(fp)
        mPath!!.reset()
        mPath!!.moveTo(x, y)
        mX = x
        mY = y
    }

    //in this method we check if the move of finger on the
    // screen is greater than the Tolerance we have previously defined
    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath!!.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        mPath!!.lineTo(mX, mY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }

    //Constructors to initialise all the attributes
    init {
        //smoothens the drawings of the user
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Color.GREEN
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        //0xff=255 in decimal
        mPaint.alpha = 0xff
    }
}
