package de.ntbit.projectearlybird.helper

import android.content.res.Resources

class PixelCalculator {

    companion object {
        fun calculateHeightForFullHD() : Int {
            return (1080F/1920F * Resources.getSystem().displayMetrics.widthPixels).toInt()
        }

        fun calculateWidthForHeight(height: Int) : Int {
            return (1920F/1080F * height).toInt()
        }
    }
}