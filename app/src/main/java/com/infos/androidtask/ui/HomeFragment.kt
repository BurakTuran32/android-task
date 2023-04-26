package com.infos.androidtask.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infos.androidtask.base.BaseFragment
import com.infos.androidtask.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val adapter by lazy { HomeAdapter() }
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Recycler.adapter = adapter
        binding.Recycler.layoutManager = LinearLayoutManager(requireContext())


        if (checkNetwork(requireContext())){
            viewModel.task.observe(viewLifecycleOwner){task->
                if(task != null){
                    task.data?.let { adapter.setData(it) }
                }
            }
            Toast.makeText(requireContext(),"data from api",Toast.LENGTH_LONG).show()
        }else{
            adapter.setData(viewModel.getLocal())
            Toast.makeText(requireContext(),"data from local",Toast.LENGTH_LONG).show()
        }

    }

    private fun checkNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }else{
                Toast.makeText(context,"Internet connection is not available.",Toast.LENGTH_LONG).show()
            }
        }else  Toast.makeText(context,"Internet connection is not available.",Toast.LENGTH_LONG).show()
        return false
    }

}