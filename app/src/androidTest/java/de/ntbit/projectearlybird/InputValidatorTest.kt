package de.ntbit.projectearlybird

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import de.ntbit.projectearlybird.helper.InputValidator

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class InputValidatorTest {
    @Test
    fun email_validator_simple_correct_email() {
        assert(InputValidator.isValidEmail("this_is_a_valid_email@mail.de"))
        assert(InputValidator.isValidEmail("right.name@mail.de"))
        assert(InputValidator.isValidEmail("111this_is_a_vali.d_email@mail.de"))
        assert(InputValidator.isValidEmail("this_is_a_valid_email@subdomain.mail.de"))
    }

    @Test
    fun email_validator_simple_incorrect_email() {
        assert(!InputValidator.isValidEmail("not_an_email@de"))
        assert(!InputValidator.isValidEmail("this_is_a_not_valid_email@mail.de.de"))
        assert(!InputValidator.isValidEmail("this_is_not_a_\\valid_email@mail.de.de"))
        assert(!InputValidator.isValidEmail("this_is_not_a valid_email@mail.de.de"))
        assert(!InputValidator.isValidEmail("this_is_not_a valid_emailmail.de.de"))
    }
}