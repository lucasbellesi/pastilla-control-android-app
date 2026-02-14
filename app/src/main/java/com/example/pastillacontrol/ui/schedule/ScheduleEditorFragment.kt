package com.example.pastillacontrol.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pastillacontrol.R
import com.example.pastillacontrol.data.local.MedicationEntity
import com.example.pastillacontrol.data.local.ScheduleEntity

class ScheduleEditorFragment : Fragment() {
    private val viewModel: ScheduleEditorViewModel by viewModels()
    private var medicationItems: List<MedicationEntity> = emptyList()
    private var scheduleItems: List<ScheduleEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_schedule_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val medicationSpinner = view.findViewById<Spinner>(R.id.spinner_medications)
        val typeSpinner = view.findViewById<Spinner>(R.id.spinner_schedule_type)
        val timeInput = view.findViewById<EditText>(R.id.input_time_of_day)
        val daysMaskInput = view.findViewById<EditText>(R.id.input_days_mask)
        val intervalInput = view.findViewById<EditText>(R.id.input_interval_hours)
        val graceInput = view.findViewById<EditText>(R.id.input_grace_minutes)
        val saveButton = view.findViewById<Button>(R.id.button_save_schedule)
        val listView = view.findViewById<ListView>(R.id.list_schedules)

        typeSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("DAILY", "WEEKLY", "INTERVAL")
        )

        saveButton.setOnClickListener {
            val medication = medicationItems.getOrNull(medicationSpinner.selectedItemPosition)
                ?: return@setOnClickListener
            val type = typeSpinner.selectedItem?.toString() ?: "DAILY"
            val timeOfDay = timeInput.text.toString().trim().ifEmpty { "08:00" }
            val daysMask = daysMaskInput.text.toString().toIntOrNull() ?: 0
            val intervalHours = intervalInput.text.toString().toIntOrNull()
            val graceMinutes = graceInput.text.toString().toIntOrNull() ?: 30

            viewModel.saveSchedule(
                medicationId = medication.id,
                type = type,
                timeOfDay = timeOfDay,
                daysOfWeekMask = daysMask,
                intervalHours = intervalHours,
                graceMinutes = graceMinutes
            )

            daysMaskInput.text?.clear()
            intervalInput.text?.clear()
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val schedule = scheduleItems.getOrNull(position) ?: return@setOnItemLongClickListener true
            viewModel.deleteSchedule(schedule.id)
            true
        }

        viewModel.medications.observe(viewLifecycleOwner) { medications ->
            medicationItems = medications
            medicationSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                medications.map { it.name }
            )
        }

        viewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            scheduleItems = schedules
            listView.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                schedules.map {
                    "${it.type} at ${it.timeOfDay} | grace ${it.graceMinutes}m"
                }
            )
        }
    }
}
