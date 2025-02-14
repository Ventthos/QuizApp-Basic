package com.ventthos.quizzappbasic

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class Category(
    @StringRes val categoryText: Int,
    @ColorRes val colorCode: Int,
    @DrawableRes val image: Int
) {
    SCIENCE(R.string.topic1, R.color.greenCorrect, R.drawable.science),
    GEOGRAPHY(R.string.topic2, R.color.blue, R.drawable.geography),
    HISTORY(R.string.topic3, R.color.yellow, R.drawable.history),
    MOVIES(R.string.topic4, R.color.purple, R.drawable.movies),
    CULTURE(R.string.topic5, R.color.error_color, R.drawable.culture)
}