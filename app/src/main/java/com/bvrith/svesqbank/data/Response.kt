package com.bvrith.svesqbank.data

import java.io.Serializable

data class Question(
    val sno: String?,
    val question: String?,
    val option1: String?,
    val option2: String?,
    val option3: String?,
    val option4: String?,
    val correct_answer: String?,
    val explanation: String?,
    val resource: String?
): Serializable

data class Questions(
    val questions: Array<Question>
)