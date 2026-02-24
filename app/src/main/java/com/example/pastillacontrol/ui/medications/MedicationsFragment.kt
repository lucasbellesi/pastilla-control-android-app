package com.example.pastillacontrol.ui.medications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pastillacontrol.R
import com.example.pastillacontrol.data.local.MedicationEntity

class MedicationsFragment : Fragment() {
    private val viewModel: MedicationsViewModel by viewModels()
    private var items: List<MedicationEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_medications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.list_medications)
        val emptyView = view.findViewById<View>(R.id.text_empty_medications)
        val addButton = view.findViewById<Button>(R.id.button_add_medication)

        listView.emptyView = emptyView

        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_medicationsFragment_to_medicationEditorFragment)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val medicationId = items.getOrNull(position)?.id ?: return@setOnItemClickListener
            val args = Bundle().apply {
                putLong(MedicationEditorFragment.ARG_MEDICATION_ID, medicationId)
            }
            findNavController().navigate(
                R.id.action_medicationsFragment_to_medicationEditorFragment,
                args
            )
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val medicationId = items.getOrNull(position)?.id ?: return@setOnItemLongClickListener true
            viewModel.deleteMedication(medicationId)
            true
        }

        viewModel.medications.observe(viewLifecycleOwner) { medications ->
            items = medications
            val labels = medications.map {
                "${it.name} - ${it.dosageAmount} ${it.dosageUnit}"
            }
            listView.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                labels
            )
        }

        viewModel.refresh()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}
