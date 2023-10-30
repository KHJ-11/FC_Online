package com.example.fc_online.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fc_online.R
import com.example.fc_online.data.SaveName
import com.example.fc_online.data.TradeType
import com.example.fc_online.util.SharedViewModel
import com.example.fc_online.data.UserInfo
import com.example.fc_online.databinding.FragmentHomeEditTextBinding
import com.example.fc_online.ui.adapter.SaveNameAdapter
import com.example.fc_online.ui.adapter.TradeAdapter
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataRepository = DataRepository(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentHomeEditTextBinding.inflate(layoutInflater)
        val view = binding.root

        userEditText()

        val itemList = ArrayList<SaveName>()
//        itemList.add(SaveName("123123"))
//        itemList.add(SaveName("${dataRepository.getData()}"))
        Log.e("123123213",dataRepository.getData())

        val itemAdapter = SaveNameAdapter(itemList)
        binding.nameGridRecyclerview.adapter = itemAdapter
        binding.nameGridRecyclerview.layoutManager = LinearLayoutManager(context)

        return view
    }

    private fun userEditText() {
        binding.homeButton.setOnClickListener {
            getUserInfo()
        }
    }

//    private fun setAdapter(nameList: ArrayList<SaveName>) {
//        val mNameAdapter = SaveNameAdapter(nameList)
//        binding.nameGridRecyclerview.apply {
//            adapter = mNameAdapter
//            layoutManager = GridLayoutManager(context,3)
//        }
//    }

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