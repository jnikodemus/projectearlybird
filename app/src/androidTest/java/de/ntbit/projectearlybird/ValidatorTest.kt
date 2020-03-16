package de.ntbit.projectearlybird

import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import de.ntbit.projectearlybird.helper.InputValidator

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ValidatorTest {

    @Test
    fun emailValidator_SimpleCorrectEmail_ReturnsTrue() {
        val inputValidator = InputValidator()
        assert(inputValidator.isValidEmail("nikodemus@nbyte.de"))
    }

    @Test
    fun emailValidator_SimpleIncorrectEmail_ReturnsFalse() {
        val inputValidator = InputValidator()
        assert(!inputValidator.isValidEmail("nikodemus@de"))
    }
}