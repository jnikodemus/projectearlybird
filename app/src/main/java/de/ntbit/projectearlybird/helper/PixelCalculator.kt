package de.ntbit.projectearlybird.helper

import android.content.res.Resources

/**
 * [PixelCalculator] is used for calculating the height/width of views on runtime.
 */
class PixelCalculator {


    companion object {

        /**
         * Calculates the height in relation to 1080p for the actual display width
         * @return [Int]
         */
        fun calculateHeightForFullHD() : Int {
            return (1080F/1920F * Resources.getSystem().displayMetrics.widthPixels).toInt()
        }

        /**
         * Returns calculated width for a provided height.
         * @return [Int]
         */
        fun calculateWidthForHeight(height: Int) : Int {
            return (1920F/1080F * height).toInt()
        }
    }
}