package com.example.pastillacontrol

import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFlowInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigateToMedicationEditor_displaysFormFields() {
        activityRule.scenario.onActivity { activity ->
            activity.findViewById<Button>(R.id.button_medications).performClick()
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        activityRule.scenario.onActivity { activity ->
            activity.findViewById<Button>(R.id.button_add_medication).performClick()
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        activityRule.scenario.onActivity { activity ->
            val name = activity.findViewById<EditText>(R.id.input_name)
            val dosageAmount = activity.findViewById<EditText>(R.id.input_dosage_amount)
            val dosageUnit = activity.findViewById<EditText>(R.id.input_dosage_unit)
            val notes = activity.findViewById<EditText>(R.id.input_notes)
            val save = activity.findViewById<Button>(R.id.button_save_medication)

            assertNotNull(name)
            assertNotNull(dosageAmount)
            assertNotNull(dosageUnit)
            assertNotNull(notes)
            assertNotNull(save)
            assertTrue(save.isShown)
        }
    }

    @Test
    fun navigateAcrossMainSections_keepsScreensVisible() {
        activityRule.scenario.onActivity { activity ->
            activity.findViewById<Button>(R.id.button_medications).performClick()
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        activityRule.scenario.onActivity { activity ->
            val medicationList = activity.findViewById<ListView>(R.id.list_medications)
            assertNotNull(medicationList)
            assertTrue(medicationList.isShown)
            activity.onBackPressedDispatcher.onBackPressed()
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        activityRule.scenario.onActivity { activity ->
            activity.findViewById<Button>(R.id.button_schedule).performClick()
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        activityRule.scenario.onActivity { activity ->
            val saveSchedule = activity.findViewById<Button>(R.id.button_save_schedule)
            assertNotNull(saveSchedule)
            assertTrue(saveSchedule.isShown)
            activity.onBackPressedDispatcher.onBackPressed()
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        activityRule.scenario.onActivity { activity ->
            activity.findViewById<Button>(R.id.button_history).performClick()
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        activityRule.scenario.onActivity { activity ->
            val historyTitle = activity.findViewById<TextView>(R.id.text_history_title)
            assertNotNull(historyTitle)
            assertTrue(historyTitle.isShown)
        }
    }
}
