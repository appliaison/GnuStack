package app.niftyapp.mobile.android.niftyapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by Das on 4/27/21.
 */
class NiftyTextViewBold(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs){

    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "lato-bold.ttf")
        setTypeface(typeface)
    }
}