package com.infos.androidtask.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.infos.androidtask.data.TaskData
import com.infos.androidtask.databinding.TaskRowBinding

class HomeAdapter: Adapter<HomeAdapter.TaskHolder>() {
    private var list = listOf<TaskData>()
    class TaskHolder(val binding : TaskRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context)
        return TaskHolder(TaskRowBinding.inflate(view,parent,false))
    }


    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val taskData = list[position]
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

    fun setData(newList: List<TaskData>){
        if (newList != null){
            list = newList
        }
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = list.size
}