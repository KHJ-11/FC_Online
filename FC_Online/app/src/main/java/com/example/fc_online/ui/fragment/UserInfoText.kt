package com.example.fc_online.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.fc_online.MainActivity
import com.example.fc_online.util.SharedViewModel
import com.example.fc_online.databinding.FragmentUserInfoTextBinding

class UserInfoText : Fragment() {
    private lateinit var binding: FragmentUserInfoTextBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentUserInfoTextBinding.inflate(layoutInflater)
        val view = binding.root

        userInfoText()

        (activity as MainActivity)

        return view
    }

    private fun userInfoText() {
        binding.userTextName.text = arguments?.getString("nickname")
        binding.userTextLevel.text = "Lv ${arguments?.getInt("level")}"
//        binding.userTextAccessid.text = sharedViewModel.sharedData

    }

}