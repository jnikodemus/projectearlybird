package de.ntbit.projectearlybird

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
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

    @Before
    fun initValidString() {
        username = "patrick"
        password = "123"
    }

    @Test
    fun test_login() {
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)

        // do
        onView(withId(R.id.actLoginEditTextUsername)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.actLoginEditTextPassword)).perform(typeText(password), closeSoftKeyboard())
        //onView(withId(R.id.actLoginBtnLogin)).perform(click())

        // check
        // Check that login was successful
        //onView(withId(R.id.navigation_drawer_layout)).check(matches(isDisplayed()))
    }
}