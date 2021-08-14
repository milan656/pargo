package com.tntra.pargo.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
@SuppressLint("AppCompatCustomView")
class BoldTextView : TextView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    internal fun init() {
        val font = Typeface.createFromAsset(getContext().assets, "fonts/hkgrotesk_bold.ttf")
        typeface = font
    }

}
