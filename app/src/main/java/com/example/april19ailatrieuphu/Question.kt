package com.example.april19ailatrieuphu

data class Question(
    val id: Long,
    val question: String,
    val level: Int,
    val caseA: String,
    val caseB: String,
    val caseC: String,
    val caseD: String,
    val trueCase: Int
) {
}