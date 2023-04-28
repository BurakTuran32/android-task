package com.infos.androidtask.ui.HomeScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.infos.androidtask.base.BaseFragment
import com.infos.androidtask.data.response.TaskData
import com.infos.androidtask.databinding.FragmentHomeBinding
import com.infos.androidtask.ui.QrScreen.QrActivity
import com.infos.androidtask.worker.RefreshWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val adapter by lazy { HomeAdapter(requireContext()) }
    private val viewModel: HomeViewModel by viewModels()
    private val taskList = arrayListOf<TaskData>()
    val QR_REQUEST_CODE = 123

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refreshDataWorkRequest = PeriodicWorkRequest.Builder(
            RefreshWorker::class.java,
            60,
            TimeUnit.MINUTES
        )
            .build()

        WorkManager
            .getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                RefreshWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                refreshDataWorkRequest
            )

        binding.Recycler.adapter = adapter
        binding.Recycler.layoutManager = LinearLayoutManager(requireContext())


        if (checkNetwork(requireContext())){
            getDataApi()
        }else getDataLocal()


        binding.swipeRefresh.setOnRefreshListener {
            if (checkNetwork(requireContext())){
                getDataApi()
            }else getDataLocal()

            binding.swipeRefresh.isRefreshing = false
        }

        binding.SearchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                filter(msg)
                return false
            }
        })
        binding.SearchView.setOnSearchClickListener {
            binding.textView.visibility = View.GONE
        }

        binding.SearchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                binding.textView.visibility = View.VISIBLE
                taskList.clear()
                if (checkNetwork(requireContext())){
                    getDataApi()
                }else getDataLocal()
                return false
            }
        })

        binding.qrButton.setOnClickListener {
            val intent = Intent(requireContext(), QrActivity::class.java)
            startActivityForResult(intent,QR_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == QR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val resultString = data?.getStringExtra("result")
            filter(resultString)
        }
    }






    private fun checkNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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

        return false
    }

    private fun getDataApi(){
        viewModel.task.observe(viewLifecycleOwner){task->
            if(task != null){
               taskList.addAll(task.data ?: arrayListOf())
                adapter.differ.submitList(taskList)
            }
        }
        Toast.makeText(requireContext(),"data from network",Toast.LENGTH_LONG).show()
    }

    private fun getDataLocal(){
        adapter.differ.submitList(viewModel.getLocal())
        Toast.makeText(requireContext(),"data from local",Toast.LENGTH_LONG).show()
    }

    private fun filter(text: String?){
        val filteredList: ArrayList<TaskData> = ArrayList()
        if (!text.isNullOrEmpty()) {
            for (item in taskList) {
                when {
                    item.task?.lowercase()?.contains(text.lowercase()) == true -> filteredList.add(
                        item
                    )
                    item.description?.lowercase()
                        ?.contains(text.lowercase()) == true -> filteredList.add(item)
                    item.colorCode?.lowercase()
                        ?.contains(text.lowercase()) == true -> filteredList.add(item)
                    item.title?.lowercase()?.contains(text.lowercase()) == true -> filteredList.add(
                        item
                    )
                    item.businessUnit?.lowercase()
                        ?.contains(text.lowercase()) == true -> filteredList.add(item)
                    item.businessUnitKey?.lowercase()
                        ?.contains(text.lowercase()) == true -> filteredList.add(item)
                    item.parentTaskId?.lowercase()
                        ?.contains(text.lowercase()) == true -> filteredList.add(item)
                    item.sort?.lowercase()?.contains(text.lowercase()) == true -> filteredList.add(
                        item
                    )
                    item.wageType?.lowercase()
                        ?.contains(text.lowercase()) == true -> filteredList.add(item)

                }
            }

            if (!filteredList.isEmpty()){
                adapter.differ.submitList(filteredList)
            }

        }
    }

}