package com.example.myapplication

data class Project(
    val projectID: String = "",
    val projectName: String = "",
    val budget: Double = 0.0,
    val engineers: List<String> = emptyList(),
    val laborersNeeded: Int = 0,
    val laborersAssigned: List<String> = emptyList()
)
