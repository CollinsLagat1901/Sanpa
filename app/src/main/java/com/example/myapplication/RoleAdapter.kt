package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoleAdapter(
    private val roleList: List<Role>,
    private val listener: OnRoleClickListener
) : RecyclerView.Adapter<RoleAdapter.RoleViewHolder>() {

    interface OnRoleClickListener {
        fun onEdit(role: Role)
        fun onDelete(role: Role)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.role_item, parent, false)
        return RoleViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        val role = roleList[position]
        holder.roleNameTextView.text = role.roleName
        holder.roleDescriptionTextView.text = role.description

        // Set up click listeners for edit and delete buttons
        holder.editButton.setOnClickListener { listener.onEdit(role) }
        holder.deleteButton.setOnClickListener { listener.onDelete(role) }
    }

    override fun getItemCount(): Int {
        return roleList.size
    }

    class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roleNameTextView: TextView = itemView.findViewById(R.id.roleNameTextView)
        val roleDescriptionTextView: TextView = itemView.findViewById(R.id.roleDescriptionTextView)
        val editButton: Button = itemView.findViewById(R.id.editRoleButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteRoleButton)
    }
}
