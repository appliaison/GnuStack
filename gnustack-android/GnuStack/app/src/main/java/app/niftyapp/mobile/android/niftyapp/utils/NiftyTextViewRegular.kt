package app.niftyapp.mobile.android.niftyapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by Das on 4/26/21.
 */

class NiftyTextViewRegular(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs){

    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "lato-regular.ttf")
        setTypeface(typeface)
    }
}