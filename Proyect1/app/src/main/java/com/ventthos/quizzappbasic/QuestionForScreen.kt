package com.ventthos.quizzappbasic

data class QuestionForScreen(
    val question: Question,
    val optionsSelected: List<Int>,
    val optionsWithHint: MutableList<Int> = mutableListOf()
)
