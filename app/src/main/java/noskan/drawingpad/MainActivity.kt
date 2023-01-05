package noskan.drawingpad

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStream


class MainActivity : AppCompatActivity() {
    private lateinit var draw: DrawView
    lateinit var black: Button
    lateinit var blue: Button
    lateinit var red: Button
    lateinit var green: Button
    lateinit var yellow: Button
    lateinit var smallBtn:ImageButton
    lateinit var mediumBtn:ImageButton
    lateinit var largeBtn:ImageButton
    lateinit var deleteBtn: ImageButton
    lateinit var saveBtn:ImageButton
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
        saveBtn = findViewById(R.id.saveBtn)
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
        //the save button will save the current canvas which is actually a bitmap
        //in form of PNG, in the storage
        saveBtn.setOnClickListener(View.OnClickListener {
            //getting the bitmap from DrawView class
            val bmp: Bitmap? = draw.save()
            //opening a OutputStream to write into the file
            var imageOutStream: OutputStream? = null
            val cv = ContentValues()
            //name of the file
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png")
            //type of the file
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            //location of the file to be saved
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

            //ge the Uri of the file which is to be v=created in the storage
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
            try {
                //open the output stream with the above uri
                imageOutStream = contentResolver.openOutputStream(uri!!)
                //this method writes the files in storage
                bmp?.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)
                //close the output stream after use
                imageOutStream!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
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