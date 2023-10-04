package com.example.fc_online.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.fc_online.R
import com.example.fc_online.databinding.FragmentUserInfoTextBinding

class UserInfoText : Fragment() {
    private lateinit var binding: FragmentUserInfoTextBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentUserInfoTextBinding.inflate(layoutInflater)
        val view = binding.root

        userInfoText()
        tradeButton()

        return view
    }

    private fun userInfoText() {
        binding.userTextName.text = arguments?.getString("nickname")
        binding.userTextLevel.text = "Lv ${arguments?.getInt("level")}"
    }

    private fun tradeButton() {
        binding.tradeButton.setOnClickListener {
            val bundle = bundleOf(
                "accessid" to arguments?.getString("accessid")
            )
            Navigation.findNavController(binding.root).navigate(R.id.action_userInfoText_to_tradeRecord, bundle)
        }
    }

}