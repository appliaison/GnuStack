package app.niftyapp.mobile.android.niftyapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * Created by Das on 4/26/21.
 */

class NiftyEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "lato-regular.ttf")
        setTypeface(typeface)
    }
}