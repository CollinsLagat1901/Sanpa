package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class JobAdapter(private val onJobSelected: (String) -> Unit) :
    ListAdapter<Job, JobAdapter.JobViewHolder>(JobDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
        holder.itemView.setOnClickListener {
            onJobSelected(job.id)
        }
    }

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val jobName: TextView = itemView.findViewById(R.id.job_name)
        private val jobDescription: TextView = itemView.findViewById(R.id.job_description)

        fun bind(job: Job) {
            jobName.text = job.name
            jobDescription.text = job.description
        }
    }
}

data class Job(val id: String, val name: String, val description: String, val laborerRequirements: Int)

class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean = oldItem == newItem
}
