package com.tntra.pargo2.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.CheckBox

/**
 * Created by jkadvantage Team on 5/08/19.
 */
@SuppressLint("AppCompatCustomView")
class NormalCheckbox : CheckBox {
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
        val font = Typeface.createFromAsset(getContext().assets, "fonts" +
                "/Lato-Medium.ttf")
        typeface = font
    }

}
