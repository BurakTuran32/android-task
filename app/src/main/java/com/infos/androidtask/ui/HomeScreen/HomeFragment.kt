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
import com.infos.androidtask.data.TaskData
import com.infos.androidtask.databinding.FragmentHomeBinding
import com.infos.androidtask.ui.QrScreen.QrActivity
import com.infos.androidtask.worker.RefreshWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val adapter by lazy { HomeAdapter() }
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



        binding.qrButton.setOnClickListener {
            val intent = Intent(requireContext(), QrActivity::class.java)
            startActivityForResult(intent,QR_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == QR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val resultString = data?.getStringExtra("result") // assuming the key for the string value is "result"
            filter(resultString)
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
        if (text != null) {
            for (item in taskList){
                if (item.task?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true){
                    filteredList.add(item)
                }
                if (item.description?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true){
                    filteredList.add(item)
                }
                if (item.colorCode?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true){
                    filteredList.add(item)
                }
                if (item.title?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true){
                    filteredList.add(item)
                }
                if (item.businessUnit?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true){
                    filteredList.add(item)
                }
                if (item.businessUnitKey?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true){
                    filteredList.add(item)
                }
                if (item.parentTaskId?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true){
                    filteredList.add(item)
                }
                if (item.sort?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true){
                    filteredList.add(item)
                }
                if (item.wageType?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true){
                    filteredList.add(item)
                }
            }
            if (filteredList.isEmpty()){
                Toast.makeText(requireContext(),"No data found",Toast.LENGTH_LONG).show()
            }else{
                adapter.differ.submitList(filteredList)
            }
        }
    }


}