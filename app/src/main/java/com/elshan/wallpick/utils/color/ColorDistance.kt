package com.elshan.wallpick.utils.color

import android.graphics.Color
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

fun colorDistance(color1: Int, color2: Int): Double {
    val hsv1 = FloatArray(3)
    val hsv2 = FloatArray(3)
    Color.colorToHSV(color1, hsv1)
    Color.colorToHSV(color2, hsv2)

    val hueDiff = min((hsv1[0] - hsv2[0]).absoluteValue, 360 - (hsv1[0] - hsv2[0]).absoluteValue) / 180.0
    val satDiff = (hsv1[1] - hsv2[1]).absoluteValue
    val valDiff = (hsv1[2] - hsv2[2]).absoluteValue

    return sqrt(hueDiff.pow(2) + satDiff.pow(2) + valDiff.pow(2))
}

