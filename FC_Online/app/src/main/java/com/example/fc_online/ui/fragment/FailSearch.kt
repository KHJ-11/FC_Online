package com.example.fc_online.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.fc_online.R
import com.example.fc_online.databinding.FragmentFailSearchBinding

class FailSearch : Fragment() {
    private lateinit var binding: FragmentFailSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentFailSearchBinding.inflate(layoutInflater)
        val view = binding.root

        backHome()

        return view
    }

    private fun backHome() {
        binding.returnBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_failSearch_to_homeEditText)
        }
    }
}