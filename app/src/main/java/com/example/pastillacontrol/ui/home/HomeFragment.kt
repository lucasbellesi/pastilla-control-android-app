package com.example.pastillacontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pastillacontrol.R

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
}
