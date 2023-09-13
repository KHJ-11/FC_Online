package com.example.fc_online.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.fc_online.R
import com.example.fc_online.data.UserInfo
import com.example.fc_online.databinding.FragmentHomeScreen01Binding
import com.example.fc_online.util.Constants.KEY
import com.example.fc_online.util.Constants.api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeScreen_01 : Fragment() {
    private lateinit var binding: FragmentHomeScreen01Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentHomeScreen01Binding.inflate(layoutInflater)
        val view = binding.root

        userEditText()

        return view
    }

    private fun userEditText() {
        binding.homeButton.setOnClickListener {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        val callGetUserInfo = api.getUserInfo("${KEY}", "${binding.homeEditText.text.toString()}")

        callGetUserInfo.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                val user = response.body()

                if (binding.homeEditText.text.toString() != user?.nickname) {
                    Navigation.findNavController(binding.root).navigate(R.id.action_homeScreen_01_to_userInfo_02)
                } else {
                    val bundle = bundleOf(
                        "nickname" to user?.nickname,
                        "level" to user?.level,
                        "accessid" to user?.accessId
                    )

                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {

            }

        })
    }
}