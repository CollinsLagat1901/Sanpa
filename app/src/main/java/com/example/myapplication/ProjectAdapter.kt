package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProjectAdapter(private val projectList: ArrayList<Project>) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_item, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projectList[position]
        holder.projectNameTv.text = project.projectName
        holder.projectIdTv.text = "ID: ${project.projectId}"
        holder.projectBudgetTv.text = "Budget: ${project.projectBudget}"
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectNameTv: TextView = itemView.findViewById(R.id.projectNameTv)
        val projectIdTv: TextView = itemView.findViewById(R.id.projectIdTv)
        val projectBudgetTv: TextView = itemView.findViewById(R.id.projectBudgetTv)
    }
}
