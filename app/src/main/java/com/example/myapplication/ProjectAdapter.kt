package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ProjectItemBinding

class ProjectAdapter(private val projectList: ArrayList<Project>) :
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    var selectedProjectId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ProjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projectList[position]
        holder.bind(project)

        // Handle item click to select project
        holder.itemView.setOnClickListener {
            selectedProjectId = project.projectID // Capture the selected project's ID
        }
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    inner class ProjectViewHolder(private val binding: ProjectItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project) {
            binding.projectName.text = project.projectName
            binding.projectBudget.text = "Budget: ${project.budget}"
        }
    }
}
