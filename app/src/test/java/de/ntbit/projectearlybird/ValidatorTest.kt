package de.ntbit.projectearlybird

import de.ntbit.projectearlybird.helper.InputValidator

import org.junit.Test

class ValidatorTest {

    @Test
    fun emailValidator_SimpleCorrectEmail_ReturnsTrue() {
        assert(InputValidator.isValidEmail("nikodemus@nbyte.de"))
    }

    @Test
    fun emailValidator_SimpleIncorrectEmail_ReturnsFalse() {
        assert(!InputValidator.isValidEmail("nikodemus@de"))
    }
}