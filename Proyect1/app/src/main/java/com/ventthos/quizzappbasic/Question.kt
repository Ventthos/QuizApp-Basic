package com.ventthos.quizzappbasic

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

data class Question(
    @StringRes val questionId:Int,
    @ArrayRes val options: Int,
    val answerIndex: Int,
    @StringRes val topic: Int,
    var answered: Boolean = false,
    var answerGottenIndex: Int = -1
)


