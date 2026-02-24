package com.example.pastillacontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pastillacontrol.R
import com.example.pastillacontrol.core.localization.AppLanguageManager

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLanguageSelector(view)

        view.findViewById<Button>(R.id.button_medications).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_medicationsFragment)
        }
        view.findViewById<Button>(R.id.button_schedule).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_scheduleEditorFragment)
        }
        view.findViewById<Button>(R.id.button_history).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }
    }

    private fun setupLanguageSelector(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_language)
        val languageTags = listOf(
            AppLanguageManager.LANGUAGE_ENGLISH,
            AppLanguageManager.LANGUAGE_SPANISH
        )
        val labels = listOf(
            getString(R.string.language_option_english),
            getString(R.string.language_option_spanish)
        )

        spinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            labels
        )

        val savedLanguage = AppLanguageManager.getSavedLanguage(requireContext())
        val selectedIndex = languageTags.indexOf(savedLanguage).takeIf { it >= 0 } ?: 0
        spinner.setSelection(selectedIndex, false)

        spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = languageTags.getOrNull(position) ?: return
                if (selectedLanguage == AppLanguageManager.getSavedLanguage(requireContext())) return
                AppLanguageManager.setLanguage(requireContext(), selectedLanguage)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
        }
    }
}
