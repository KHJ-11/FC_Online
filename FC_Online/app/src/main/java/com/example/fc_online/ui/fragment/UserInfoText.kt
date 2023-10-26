package com.example.fc_online.ui.fragment

import android.content.Context
import android.content.SharedPreferences
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
    private lateinit var sharedPreferences: SharedPreferences
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

        binding.saveButton.setOnClickListener {
            val saveName = binding.userTextName.text.toString()
            if (isFavorite(saveName)) {
                removeFavorite(saveName)

            } else {
                addFavorite(saveName)

            }
        }

    }

    private fun addFavorite(text: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(text, true)
        editor.apply()
    }

    private fun removeFavorite(text: String) {
        val editor = sharedPreferences.edit()
        editor.remove(text)
        editor.apply()
    }

    private fun isFavorite(text: String): Boolean {
        return sharedPreferences.getBoolean(text, false)
    }

}