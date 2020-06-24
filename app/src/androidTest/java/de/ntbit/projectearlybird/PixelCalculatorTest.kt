package de.ntbit.projectearlybird

import android.content.res.Resources
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import de.ntbit.projectearlybird.helper.PixelCalculator
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class PixelCalculatorTest {

    @Test
    fun test_calculate_height_for_full_hd() {
        Assert.assertEquals((1080F/1920F * Resources
            .getSystem()
            .displayMetrics
            .widthPixels)
            .toInt(),
            PixelCalculator.calculateHeightForFullHD())
    }

    @Test
    fun test_calculate_width_for_height() {
        val heightZero = 0
        val heightOK = 50
        val height720p = 720
        val width720p = 1280
        Assert.assertEquals((1920F/1080F * heightOK).toInt(),
            PixelCalculator.calculateWidthForHeight(heightOK))
        Assert.assertEquals(heightZero, PixelCalculator.calculateWidthForHeight(heightZero))
        Assert.assertEquals(width720p, PixelCalculator.calculateWidthForHeight(height720p))
    }
}