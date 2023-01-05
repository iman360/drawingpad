package noskan.drawingpad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import android.widget.ImageButton


class MainActivity : AppCompatActivity() {
    private lateinit var draw: DrawView
    lateinit var black: Button
    lateinit var blue: Button
    lateinit var red: Button
    lateinit var green: Button
    lateinit var yellow: Button
    lateinit var smallBtn:Button
    lateinit var mediumBtn:Button
    lateinit var largeBtn:Button
    lateinit var deleteBtn: ImageButton
    private val blackColor:Int = -16777216
    private val blueColor:Int = -16776961
    private val redColor:Int = -65536
    private val greenColor:Int = -16711936
    private val yellowColor:Int = -256
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//verifying the views
        draw = findViewById(R.id.draw_view)
        deleteBtn = findViewById(R.id.deleteBtn)
        black = findViewById(R.id.black)
        blue = findViewById(R.id.blue)
        red = findViewById(R.id.red)
        green = findViewById(R.id.green)
        yellow = findViewById(R.id.yellow)
        smallBtn = findViewById(R.id.smallBtn)
        mediumBtn = findViewById(R.id.mediumBtn)
        largeBtn = findViewById(R.id.largeBtn)


//DrawView instantiate
        val vto = draw.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                draw.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = draw.measuredWidth
                val height = draw.measuredHeight
                draw.init(height, width)
            }
        })
//delete button
        deleteBtn.setOnClickListener {
            draw.delete()
        }
//color button
        black.setOnClickListener {
            draw.setColor(blackColor)
        }
        blue.setOnClickListener {
            draw.setColor(blueColor)
        }
        red.setOnClickListener {
            draw.setColor(redColor)
        }
        green.setOnClickListener {
            draw.setColor(greenColor)
        }
        yellow.setOnClickListener {
            draw.setColor(yellowColor)
        }
//brush size
        smallBtn.setOnClickListener {
            draw.setStrokeWidth(10)
        }
        mediumBtn.setOnClickListener {
            draw.setStrokeWidth(25)
        }
        largeBtn.setOnClickListener {
            draw.setStrokeWidth(40)
        }
    }
}