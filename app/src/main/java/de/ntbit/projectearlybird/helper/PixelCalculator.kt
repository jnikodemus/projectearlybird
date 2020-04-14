package de.ntbit.projectearlybird.helper

import android.content.res.Resources

/**
 * Checks for calculating pixel height and width
 */
class PixelCalculator {


    companion object {
        /**
         * Calculates the height in relation to 1080p for the actual display width
         * @return calculated height
         */
        fun calculateHeightForFullHD() : Int {
            return (1080F/1920F * Resources.getSystem().displayMetrics.widthPixels).toInt()
        }

        fun calculateWidthForHeight(height: Int) : Int {
            return (1920F/1080F * height).toInt()
        }
    }
}