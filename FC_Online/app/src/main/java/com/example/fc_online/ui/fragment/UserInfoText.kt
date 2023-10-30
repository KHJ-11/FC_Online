package com.example.fc_online.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fc_online.MainActivity
import com.example.fc_online.data.SaveName
import com.example.fc_online.util.SharedViewModel
import com.example.fc_online.databinding.FragmentUserInfoTextBinding
import com.example.fc_online.ui.adapter.SaveNameAdapter
import com.example.fc_online.util.DataRepository

class UserInfoText : Fragment() {
    private lateinit var binding: FragmentUserInfoTextBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var dataRepository: DataRepository
//    private lateinit var sharedPreferences: SharedPreferences
//    private val SAVE_NAME = "Favorites"
//    private val KEY_TEXT = "textKey"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataRepository = DataRepository(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentUserInfoTextBinding.inflate(layoutInflater)
        val view = binding.root

        userInfoText()

        binding.saveButton.setOnClickListener {
            saveData()
        }

        (activity as MainActivity)

//        sharedPreferences = requireActivity().getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE)
//        val savedText = sharedPreferences.getString(KEY_TEXT, "")
//        binding.test.text = savedText

        return view
    }

    override fun onPause() {
        super.onPause()

//        val sharedPreferences = requireActivity().getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        val textToSave = binding.test.text.toString()
//        editor.putString(KEY_TEXT, textToSave)
//        editor.apply()

    }

    private fun saveData() {
        val data = "test"
        dataRepository.saveData(data)
    }

//    private fun nameData(): MutableList<SaveName> {
//        val nameData = mutableListOf<SaveName>()
//
//        binding.saveButton.setOnClickListener {
//            val saveName = binding.userTextName.text.toString()
//            if (isFavorite(saveName)) {
//                removeFavorite(saveName)
////                Log.e("awdawd",saveName)
////                binding.test.text = ""
//                nameData.remove(SaveName(saveName))
//            } else {
//                addFavorite(saveName)
////                Log.e("123123",saveName)
////                binding.test.text = saveName
//                nameData.add(SaveName(saveName))
//            }
//        }
//
//        return nameData
//    }

    private fun userInfoText() {
        binding.userTextName.text = arguments?.getString("nickname")
        binding.userTextLevel.text = "Lv ${arguments?.getInt("level")}"
//        binding.userTextAccessid.text = sharedViewModel.sharedData

//        binding.saveButton.setOnClickListener {
//            val saveName = binding.userTextName.text.toString()
//            if (isFavorite(saveName)) {
//                removeFavorite(saveName)
//                Log.e("awdawd",saveName)
//                binding.test.text = ""
//            } else {
//                addFavorite(saveName)
//                Log.e("123123",saveName)
//                binding.test.text = saveName
//            }
//        }

    }

//    private fun addFavorite(text: String) {
//        val editor = sharedPreferences.edit()
//        editor.putBoolean(text, true)
//        editor.apply()
//    }
//
//    private fun removeFavorite(text: String) {
//        val editor = sharedPreferences.edit()
//        editor.remove(text)
//        editor.apply()
//    }
//
//    private fun isFavorite(text: String): Boolean {
//        return sharedPreferences.getBoolean(text, false)
//    }

}