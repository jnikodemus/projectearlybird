package de.ntbit.projectearlybird.helper

import android.content.res.Resources

class PixelCalculator {

    companion object {
        fun calculateHeightForFullHD() : Int {
            return (1080f/1920f * Resources.getSystem().displayMetrics.widthPixels).toInt()
        }
    }
}