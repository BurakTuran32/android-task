package com.infos.androidtask.ui.HomeScreen

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.infos.androidtask.R
import com.infos.androidtask.data.response.TaskData
import com.infos.androidtask.databinding.TaskRowBinding

class HomeAdapter(private val context: Context): Adapter<HomeAdapter.TaskHolder>() {
    class TaskHolder(val binding : TaskRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    private val differCallback = object : DiffUtil.ItemCallback<TaskData>() {
        override fun areItemsTheSame(oldItem: TaskData, newItem: TaskData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TaskData, newItem: TaskData): Boolean {
            return oldItem == newItem
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context)
        return TaskHolder(TaskRowBinding.inflate(view,parent,false))
    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val taskData = differ.currentList[position]
        holder.binding.apply {
            taskTV.text = context.getString(R.string.task,taskData.task)
            decTv.text = context.getString(R.string.desc,taskData.description)
            TitleTV.text = context.getString(R.string.title,taskData.title)
            colorTv.text = context.getString(R.string.colorCode,taskData.colorCode)
            if (!taskData.colorCode.isNullOrEmpty()){
                cardView.setCardBackgroundColor(Color.parseColor(taskData.colorCode))
            }

        }


    }


    override fun getItemCount(): Int = differ.currentList.size
}