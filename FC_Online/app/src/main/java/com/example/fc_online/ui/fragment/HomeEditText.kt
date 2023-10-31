package com.example.fc_online.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fc_online.R
import com.example.fc_online.util.SharedViewModel
import com.example.fc_online.data.UserInfo
import com.example.fc_online.databinding.FragmentHomeEditTextBinding
import com.example.fc_online.ui.adapter.SaveNameAdapter
import com.example.fc_online.util.Constants.KEY
import com.example.fc_online.util.Constants.api
import com.example.fc_online.util.DataRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeEditText : Fragment() {
    private lateinit var binding: FragmentHomeEditTextBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var dataRepository: DataRepository
    private lateinit var adapter: SaveNameAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataRepository = DataRepository(context)
        adapter = SaveNameAdapter(dataRepository.getTexts().toList())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentHomeEditTextBinding.inflate(layoutInflater)
        val view = binding.root

        userEditText()

        binding.rvNameGrid.adapter = adapter
//        binding.rvNameGrid.layoutManager = GridLayoutManager(context,3)
        binding.rvNameGrid.layoutManager = LinearLayoutManager(context)

        return view
    }

    private fun userEditText() {
        binding.homeButton.setOnClickListener {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        val callGetUserInfo = api.getUserInfo("${KEY}", "${binding.homeEditText.text.toString()}")

        callGetUserInfo.enqueue(object: Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                val user = response.body()

                if (binding.homeEditText.text.toString() != user?.nickname) {
                    binding.homeFailText.text = "존재하지 않는 감독명입니다."
                    binding.homeFailText.setTextColor(Color.RED)
                } else {
                    val bundle = bundleOf(
                        "nickname" to user?.nickname,
                        "level" to user?.level,
                        "accessid" to user?.accessId
                    )
                    Navigation.findNavController(binding.root).navigate(R.id.action_homeEditText_to_userInfoText, bundle)

                    sharedViewModel.sharedData = user.accessId
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {

            }

        })
    }

}