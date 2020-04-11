package de.ntbit.projectearlybird.helper

import android.content.res.Resources

/**
 * [PixelCalculator] is used for calculating the height/width of views on runtime.
 */
class PixelCalculator {

    companion object {

        /**
         * Returns the calculated height for actual displaywidth.
         */
        fun calculateHeightForFullHD() : Int {
            return (1080F/1920F * Resources.getSystem().displayMetrics.widthPixels).toInt()
        }

        /**
         * Returns calculated width for a provided height.
         */
        fun calculateWidthForHeight(height: Int) : Int {
            return (1920F/1080F * height).toInt()
        }
    }
}