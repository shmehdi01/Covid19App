package app.shmehdi.covid19app

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.io.IOException
import java.io.InputStream

fun Context.loadJSONFromAsset(fileName: String): String? {
    val json: String?
    json = try {
        val inputStream: InputStream = this.assets!!.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, charset("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return json
}


fun TextView.setCount(count: Int, duration: Long = 2000) {
    val tv = this
    val animator = ValueAnimator()
    animator.setObjectValues(0, count)
    animator.addUpdateListener { animation -> tv.text = "${animation.animatedValue}" }
    animator.duration = duration // here you set the duration of the anim
    animator.start()
}

fun TextView.setDrawable(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    setCompoundDrawablesWithIntrinsicBounds(left,top,right,bottom)
}
 fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}