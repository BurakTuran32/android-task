package com.infos.androidtask.ui.HomeScreen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.infos.androidtask.data.TaskData
import com.infos.androidtask.databinding.TaskRowBinding

class HomeAdapter: Adapter<HomeAdapter.TaskHolder>() {
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
            taskTV.text = "Task: ${taskData.task} "
            decTv.text = "Description: ${taskData.description} "
            TitleTV.text = "Title: ${taskData.title} "
            colorTv.text = "ColorCode: ${taskData.colorCode} "
            if (taskData.colorCode != "" && taskData.colorCode != null){
                cardView.setCardBackgroundColor(Color.parseColor(taskData.colorCode))
            }

        }


    }


    override fun getItemCount(): Int = differ.currentList.size
}