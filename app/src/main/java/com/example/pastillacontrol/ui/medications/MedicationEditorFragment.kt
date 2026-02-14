package com.example.pastillacontrol.ui.medications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pastillacontrol.R

class MedicationEditorFragment : Fragment() {
    private val viewModel: MedicationEditorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_medication_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val medicationId = arguments?.getLong(ARG_MEDICATION_ID, 0L) ?: 0L
        val title = view.findViewById<TextView>(R.id.text_editor_title)
        val nameInput = view.findViewById<EditText>(R.id.input_name)
        val dosageAmountInput = view.findViewById<EditText>(R.id.input_dosage_amount)
        val dosageUnitInput = view.findViewById<EditText>(R.id.input_dosage_unit)
        val notesInput = view.findViewById<EditText>(R.id.input_notes)

        title.setText(if (medicationId > 0L) R.string.edit_medication else R.string.add_medication)

        viewModel.loadMedication(medicationId)
        viewModel.medication.observe(viewLifecycleOwner) { medication ->
            medication ?: return@observe
            nameInput.setText(medication.name)
            dosageAmountInput.setText(medication.dosageAmount)
            dosageUnitInput.setText(medication.dosageUnit)
            notesInput.setText(medication.notes)
        }

        viewModel.saveCompleted.observe(viewLifecycleOwner) { saved ->
            if (saved == true) {
                findNavController().popBackStack()
            }
        }

        view.findViewById<Button>(R.id.button_save_medication).setOnClickListener {
            val name = nameInput.text.toString().trim()
            val dosageAmount = dosageAmountInput.text.toString().trim()
            val dosageUnit = dosageUnitInput.text.toString().trim()
            val notes = notesInput.text.toString().trim()

            if (name.isEmpty() || dosageAmount.isEmpty() || dosageUnit.isEmpty()) {
                return@setOnClickListener
            }

            viewModel.saveMedication(
                id = medicationId,
                name = name,
                dosageAmount = dosageAmount,
                dosageUnit = dosageUnit,
                notes = notes
            )
        }
    }

    companion object {
        const val ARG_MEDICATION_ID = "medication_id"
    }
}
