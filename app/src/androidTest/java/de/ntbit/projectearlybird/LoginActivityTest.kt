package de.ntbit.projectearlybird

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import de.ntbit.projectearlybird.ui.activity.LoginActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class LoginActivityTest {

    private lateinit var username: String
    private lateinit var password: String

    @get:Rule
    var loginRule: ActivityTestRule<LoginActivity>
            = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun initValidString() {
        username = "patrick"
        password = "123"
    }

    @Test
    fun test_login() {
        // do
        onView(withId(R.id.actLoginEditTextUsername)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.actLoginEditTextPassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.actLoginBtnLogin)).perform(click())

        // check
        // Check that login was successful
    }
}