package com.tntra.pargo.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button

/**
 * Created by jkadvantage Team on 5/08/19.
 */
@SuppressLint("AppCompatCustomView")
class NormalButton : Button {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    internal fun init(context: Context) {
        val font = Typeface.createFromAsset(getContext().assets, "fonts/hkgrotesk_mediumlegacy.ttf")
        typeface = font
    }

}
